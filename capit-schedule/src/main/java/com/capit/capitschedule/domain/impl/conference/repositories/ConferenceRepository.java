package com.capit.capitschedule.domain.impl.conference.repositories;

import com.capit.capitschedule.domain.impl.conference.projections.conferenceDetails.ConferenceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConferenceRepository extends JpaRepository<ConferenceDetails, UUID> {
}
