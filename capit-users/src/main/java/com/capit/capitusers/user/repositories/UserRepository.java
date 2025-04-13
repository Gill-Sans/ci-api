package com.capit.capitusers.user.repositories;

import com.capit.capitusers.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
