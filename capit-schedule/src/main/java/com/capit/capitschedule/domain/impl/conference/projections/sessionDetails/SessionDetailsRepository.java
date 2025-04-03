package com.capit.capitschedule.domain.impl.conference.projections.sessionDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionDetailsRepository extends JpaRepository<SessionDetails, UUID> {
    List<SessionDetails> findByConferenceId(UUID conferenceId);
} 