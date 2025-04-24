package com.capit.capitusers.user.services;

import com.capit.capitusers.user.dto.UserDetailsDto;
import com.capit.capitusers.user.dto.UserUpdateDto;
import com.capit.capitusers.user.entities.User;
import com.capit.capitusers.user.repositories.UserRepository;
import com.capit.exceptions.BaseRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final UserEventProducer userEventProducer;
    
    @Override
    public UserDetailsDto getUserProfileFromAuth(String authHeader) {
        String token = extractToken(authHeader);
        UserDetailsDto userDetails = extractUserDetailsFromToken(token);

        Optional<User> existingUser = userRepository.findById(userDetails.getId());

        User user = existingUser.orElseGet(() -> createNewUser(userDetails));

        return modelMapper.map(user, UserDetailsDto.class);
    }
    
    protected String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
    
    protected UserDetailsDto extractUserDetailsFromToken(String token) {
        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getDecoder().decode(chunks[1]));

        Map<String, Object> claims;
        try {
            claims = objectMapper.readValue(payload, Map.class);
        } catch (JsonProcessingException e) {
            throw new BaseRuntimeException("Failed to parse token payload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return UserDetailsDto.builder()
                .id(UUID.fromString((String) claims.get("sub")))
                .firstName((String) claims.getOrDefault("given_name", ""))
                .lastName((String) claims.getOrDefault("family_name", ""))
                .email((String) claims.getOrDefault("email", ""))
                .build();
    }
    
    @Override
    public UserDetailsDto getOrCreateUser(UUID userId, String firstName, String lastName, String email) {
        Optional<User> existingUser = userRepository.findById(userId);
        
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .build();
            user = createNewUser(userDetailsDto);
        }
        
        return modelMapper.map(user, UserDetailsDto.class);
    }
    
    protected User createNewUser(UserDetailsDto userDetailsDto) {
        User newUser = modelMapper.map(userDetailsDto, User.class);
        User savedUser = userRepository.save(newUser);
        
        userEventProducer.publishUserCreatedEvent(savedUser);
        
        return savedUser;
    }
    
    @Override
    public UserDetailsDto getUserByKeycloakId(String keycloakId) {
        User user = userRepository.findById(UUID.fromString(keycloakId))
            .orElseThrow(() -> new BaseRuntimeException("User not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(user, UserDetailsDto.class);
    }
    
    @Override
    public UserDetailsDto updateUserProfile(String authHeader, UserUpdateDto updateDto) {
        String token = extractToken(authHeader);
        try {
            UserDetailsDto userDetails = extractUserDetailsFromToken(token);
            Optional<User> userOptional = userRepository.findById(userDetails.getId());
            
            if (userOptional.isEmpty()) {
                throw new BaseRuntimeException("User not found", HttpStatus.NOT_FOUND);
            }
            
            User user = userOptional.get();

            modelMapper.map(updateDto, user);
            User savedUser = userRepository.save(user);
            
            return modelMapper.map(savedUser, UserDetailsDto.class);
        } catch (Exception e) {
            log.error("Error updating user profile", e);
            throw new RuntimeException("Failed to update user profile", e);
        }
    }
}
