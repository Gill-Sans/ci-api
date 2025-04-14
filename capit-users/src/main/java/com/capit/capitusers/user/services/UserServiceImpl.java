package com.capit.capitusers.user.services;

import com.capit.capitusers.user.dto.UserDetailsDto;
import com.capit.capitusers.user.dto.UserUpdateDto;
import com.capit.capitusers.user.entities.User;
import com.capit.capitusers.user.mappers.UserMapper;
import com.capit.capitusers.user.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    
    @Override
    public UserDetailsDto getUserProfileFromAuth(String authHeader) {
        try {
            String token = extractToken(authHeader);
            UserDetailsDto userDetails = extractUserDetailsFromToken(token);
            
            Optional<User> existingUser = userRepository.findByKeycloakId(userDetails.getKeycloakId());
            
            User user;
            user = existingUser.orElseGet(() -> createNewUser(userDetails));
            
            return userMapper.toDto(user);
        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            throw new RuntimeException("Invalid token", e);
        }
    }
    
    protected String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
    
    /**
     * Extracts user details from a JWT token
     * 
     * @param token JWT token
     * @return UserDetailsDto with extracted information
     * @throws Exception If token parsing fails
     */
    protected UserDetailsDto extractUserDetailsFromToken(String token) throws Exception {
        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getDecoder().decode(chunks[1]));
        
        Map<String, Object> claims = objectMapper.readValue(payload, Map.class);
        
        return UserDetailsDto.builder()
                .keycloakId((String) claims.get("sub"))
                .firstName((String) claims.getOrDefault("given_name", ""))
                .lastName((String) claims.getOrDefault("family_name", ""))
                .email((String) claims.getOrDefault("email", ""))
                .build();
    }
    
    @Override
    public UserDetailsDto getOrCreateUser(String keycloakId, String firstName, String lastName, String email) {
        Optional<User> existingUser = userRepository.findByKeycloakId(keycloakId);
        
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                    .keycloakId(keycloakId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .build();
            user = createNewUser(userDetailsDto);
        }
        
        return userMapper.toDto(user);
    }
    
    protected User createNewUser(UserDetailsDto userDetailsDto) {
        User newUser = userMapper.toEntity(userDetailsDto);
        return userRepository.save(newUser);
    }
    
    @Override
    public UserDetailsDto getUserByKeycloakId(String keycloakId) {
        Optional<User> user = userRepository.findByKeycloakId(keycloakId);
        return user.map(userMapper::toDto).orElse(null);
    }
    
    @Override
    public UserDetailsDto updateUserProfile(String authHeader, UserUpdateDto updateDto) {
        String token = extractToken(authHeader);
        try {
            UserDetailsDto userDetails = extractUserDetailsFromToken(token);
            Optional<User> userOptional = userRepository.findByKeycloakId(userDetails.getKeycloakId());
            
            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            
            User user = userOptional.get();

            User updatedUser = userMapper.updateEntityFromUpdateDto(user, updateDto);
            User savedUser = userRepository.save(updatedUser);
            
            return userMapper.toDto(savedUser);
        } catch (Exception e) {
            log.error("Error updating user profile", e);
            throw new RuntimeException("Failed to update user profile", e);
        }
    }
}
