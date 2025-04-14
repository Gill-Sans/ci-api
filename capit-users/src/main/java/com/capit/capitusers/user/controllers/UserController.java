package com.capit.capitusers.user.controllers;

import com.capit.capitusers.user.entities.User;
import com.capit.capitusers.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    /**
     * Endpoint that gets or creates a user profile based on the JWT token
     */
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization header is required");
        }
        
        try {
            User user = userService.getUserProfileFromAuth(authHeader);
            
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User profile could not be created");
            }
            
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error retrieving user profile", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid token or unable to process request");
        }
    }
} 