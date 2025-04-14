package com.capit.capitusers.user.mappers;

import com.capit.capitusers.user.dto.UserDetailsDto;
import com.capit.capitusers.user.dto.UserUpdateDto;
import com.capit.capitusers.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public User toEntity(UserDetailsDto dto) {
        User user = new User();
        user.setKeycloakId(dto.getKeycloakId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setCompanyBranch(dto.getCompanyBranch());
        return user;
    }

    public User updateEntityFromUpdateDto(User user, UserUpdateDto updateDto) {
        if (updateDto.getFirstName() != null) {
            user.setFirstName(updateDto.getFirstName());
        }
        
        if (updateDto.getLastName() != null) {
            user.setLastName(updateDto.getLastName());
        }
        
        if (updateDto.getEmail() != null) {
            user.setEmail(updateDto.getEmail());
        }
        
        if (updateDto.getCompanyBranch() != null) {
            user.setCompanyBranch(updateDto.getCompanyBranch());
        }
        
        return user;
    }
    
    public UserDetailsDto toDto(User user) {
        return UserDetailsDto.builder()
                .keycloakId(user.getKeycloakId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .companyBranch(user.getCompanyBranch())
                .build();
    }
} 