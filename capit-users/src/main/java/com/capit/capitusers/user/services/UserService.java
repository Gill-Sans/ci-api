package com.capit.capitusers.user.services;

import java.util.UUID;

import com.capit.capitusers.user.dto.UserDetailsDto;
import com.capit.capitusers.user.dto.UserUpdateDto;

public interface UserService {
    /**
     * Gets or creates a user based on auth header containing JWT
     * @param authHeader JWT token in Authorization header
     * @return UserDetailsDto with user profile data
     */
    UserDetailsDto getUserProfileFromAuth(String authHeader);
    
    /**
     * Updates a user's profile information
     * @param authHeader JWT token in Authorization header
     * @param updateDto The data to update
     * @return Updated user details
     */
    UserDetailsDto updateUserProfile(String authHeader, UserUpdateDto updateDto);
} 