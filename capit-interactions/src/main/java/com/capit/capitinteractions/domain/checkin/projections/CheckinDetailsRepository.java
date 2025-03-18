package com.capit.capitinteractions.domain.checkin.projections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckinDetailsRepository extends JpaRepository<CheckinDetails, UUID> {
}
