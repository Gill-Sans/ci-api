package com.capit.capitschedule.domain.impl.conference.aggregates;

public record Address(
        String country,
        String street,
        String number,
        String zip,
        String state,
        String locationDetails
) {}
