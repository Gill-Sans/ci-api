package com.capit.capitschedule.domain.impl.conference.projections.conferenceDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConferenceDetailsRepository extends JpaRepository<ConferenceDetails, UUID> {

}
