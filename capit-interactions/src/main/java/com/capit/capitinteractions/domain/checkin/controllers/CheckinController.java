package com.capit.capitinteractions.domain.checkin.controllers;

import com.capit.capitinteractions.domain.checkin.requests.CheckinRequest;
import com.capit.capitinteractions.domain.checkin.dto.CheckinDto;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import com.capit.capitinteractions.domain.checkin.service.CheckinService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CheckinDto> createCheckin(@RequestBody CheckinRequest request) {
        CheckinDto dto = checkinService.createCheckin(request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckinDto> getCheckinById(@PathVariable UUID id) {
        return checkinService.getCheckinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CheckinDto>> getCheckinsByUserId(@PathVariable UUID userId) {
        List<CheckinDto> dtos = checkinService.getCheckinsByUserId(userId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<CheckinDto>> getCheckinsBySessionId(@PathVariable UUID sessionId) {
        List<CheckinDto> dtos = checkinService.getCheckinsBySessionId(sessionId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<List<CheckinDto>> getCheckinsByConferenceId(@PathVariable UUID conferenceId) {
        List<CheckinDto> dtos = checkinService.getCheckinsByConferenceId(conferenceId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyCheckin(
            @RequestParam UUID userId,
            @RequestParam UUID sessionId) {
        boolean exists = checkinService.checkinExists(userId, sessionId);
        return ResponseEntity.ok(exists);
    }
}
