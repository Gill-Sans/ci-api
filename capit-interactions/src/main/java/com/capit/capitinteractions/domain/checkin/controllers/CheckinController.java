package com.capit.capitinteractions.domain.checkin.controllers;

import com.capit.capitinteractions.domain.checkin.requests.CreateCheckinRequest;
import com.capit.capitinteractions.domain.checkin.dto.CheckinDto;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import com.capit.capitinteractions.domain.checkin.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interaction/check-ins")
public class CheckinController {
    private final CheckinService checkinService;

    @PostMapping
    public ResponseEntity<CheckinDto> createCheckin(@RequestBody CreateCheckinRequest request) {
        try {
            Checkin checkin = checkinService.createCheckin(request);
            return ResponseEntity.ok(CheckinDto.fromEntity(checkin));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CheckinDto> getCheckinById(@PathVariable UUID id) {
        return checkinService.getCheckinById(id)
                .map(CheckinDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CheckinDto>> getCheckinsByUserId(@PathVariable UUID userId) {
        List<CheckinDto> checkins = checkinService.getCheckinsByUserId(userId)
                .stream()
                .map(CheckinDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(checkins);
    }
    
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<CheckinDto>> getCheckinsBySessionId(@PathVariable UUID sessionId) {
        List<CheckinDto> checkins = checkinService.getCheckinsBySessionId(sessionId)
                .stream()
                .map(CheckinDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(checkins);
    }
    
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyCheckin(
            @RequestParam UUID userId, 
            @RequestParam UUID sessionId) {
        boolean hasCheckedIn = checkinService.checkinExists(userId, sessionId);
        return ResponseEntity.ok(hasCheckedIn);
    }
}
