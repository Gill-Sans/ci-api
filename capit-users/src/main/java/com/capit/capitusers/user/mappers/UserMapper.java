package com.capit.capitusers.user.mappers;

import com.capit.capitusers.user.dto.UserDetailsDto;
import com.capit.capitusers.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    /**
     * Creates a new User entity from UserDetailsDto
     */
    public User toEntity(UserDetailsDto dto) {
        User user = new User();
        user.setKeycloakId(dto.getKeycloakId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return user;
    }
    
    /**
     * Updates an existing User entity with data from UserDetailsDto
     */
    public void updateEntity(User user, UserDetailsDto dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
    }
} 