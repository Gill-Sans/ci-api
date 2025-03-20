package com.capit.capitschedule.domain.impl.session.repositories;

import com.capit.capitschedule.domain.impl.session.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findAllByConferenceId(UUID conferenceId);
}
