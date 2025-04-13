package com.capit.capitusers.user.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private String ComanyBranch;
}
