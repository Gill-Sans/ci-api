package com.capit.capitschedule.domain.impl.conference.aggregates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    private String country;
    private String street;
    private String number;
    private String zip;
    private String state;
    private String locationDetails;
}
