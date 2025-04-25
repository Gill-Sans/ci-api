package com.capit.capitinteractions.domain.checkin.repository;

import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, UUID> {
    List<Checkin> findByUserId(UUID userId);
    List<Checkin> findBySessionId(UUID sessionId);
    List<Checkin> findByConferenceId(UUID conferenceId);
    boolean existsByUserIdAndSessionId(UUID userId, UUID sessionId);
} 