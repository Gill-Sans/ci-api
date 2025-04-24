package com.capit.capitusers.user.controllers;

import com.capit.capitusers.user.dto.UserDetailsDto;
import com.capit.capitusers.user.dto.UserUpdateDto;
import com.capit.capitusers.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/details")
    public ResponseEntity<UserDetailsDto> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        UserDetailsDto userDetails = userService.getUserProfileFromAuth(authHeader);
        return ResponseEntity.ok(userDetails);
    }

    @PatchMapping("/details")
    public ResponseEntity<UserDetailsDto> updateUserDetails(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserUpdateDto updateDto) {
        UserDetailsDto updatedDetails = userService.updateUserProfile(authHeader, updateDto);
        return ResponseEntity.ok(updatedDetails);
    }
} 