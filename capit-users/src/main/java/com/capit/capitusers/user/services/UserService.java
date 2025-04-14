package com.capit.capitusers.user.services;

import com.capit.capitusers.user.entities.User;

public interface UserService {
    /**
     * Gets or creates a user based on auth header containing JWT
     * @param authHeader JWT token in Authorization header
     * @return User entity with local profile data
     */
    User getUserProfileFromAuth(String authHeader);
    
    /**
     * Gets or creates a user based on Keycloak information.
     * If a user with the given keycloakId exists, returns that user.
     * Otherwise creates a new user with the provided information.
     */
    User getOrCreateUser(String keycloakId, String firstName, String lastName, String email);
    
    /**
     * Gets a user by their Keycloak ID
     */
    User getUserByKeycloakId(String keycloakId);
} 