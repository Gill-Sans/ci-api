package com.capit.capitinteractions.api.checkin.controllers;

import com.capit.capitinteractions.api.checkin.requests.CreateCheckinRequest;
import com.capit.capitinteractions.domain.checkin.services.CheckinServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interaction/check-ins")
public class CheckinController {
    private final CheckinServcie checkinServcie;

    @PostMapping
    public ResponseEntity<String> createCheckin(@RequestBody CreateCheckinRequest request) {
        return ResponseEntity.ok(checkinServcie.createCheckin(request));
    }
}
