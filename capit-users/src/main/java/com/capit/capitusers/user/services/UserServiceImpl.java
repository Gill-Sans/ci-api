package com.capit.capitusers.user.services;

import com.capit.capitusers.user.dto.UserDetailsDto;
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
    public User getUserProfileFromAuth(String authHeader) {
        try {
            String token = extractToken(authHeader);
            UserDetailsDto userDetails = extractUserDetailsFromToken(token);
            return getOrCreateUser(
                    userDetails.getKeycloakId(),
                    userDetails.getFirstName(),
                    userDetails.getLastName(), 
                    userDetails.getEmail()
            );
        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            throw new RuntimeException("Invalid token", e);
        }
    }
    
    /**
     * Extracts the raw JWT token from Authorization header
     * 
     * @param authHeader Authorization header value
     * @return JWT token without 'Bearer ' prefix
     */
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
    public User getOrCreateUser(String keycloakId, String firstName, String lastName, String email) {
        UserDetailsDto userDetailsDto = createUserDetailsDto(keycloakId, firstName, lastName, email);
        Optional<User> existingUser = findUserByKeycloakId(keycloakId);
        
        if (existingUser.isPresent()) {
            return updateExistingUser(existingUser.get(), userDetailsDto);
        } else {
            return createNewUser(userDetailsDto);
        }
    }
    
    /**
     * Creates a UserDetailsDto from individual fields
     */
    protected UserDetailsDto createUserDetailsDto(String keycloakId, String firstName, String lastName, String email) {
        return UserDetailsDto.builder()
                .keycloakId(keycloakId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }
    
    /**
     * Finds a user by their Keycloak ID
     */
    protected Optional<User> findUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }
    
    /**
     * Updates an existing user with new details
     */
    protected User updateExistingUser(User existingUser, UserDetailsDto userDetailsDto) {
        userMapper.updateEntity(existingUser, userDetailsDto);
        return userRepository.save(existingUser);
    }
    
    /**
     * Creates a new user from the details provided
     */
    protected User createNewUser(UserDetailsDto userDetailsDto) {
        User newUser = userMapper.toEntity(userDetailsDto);
        return userRepository.save(newUser);
    }
    
    @Override
    public User getUserByKeycloakId(String keycloakId) {
        return findUserByKeycloakId(keycloakId).orElse(null);
    }
}
