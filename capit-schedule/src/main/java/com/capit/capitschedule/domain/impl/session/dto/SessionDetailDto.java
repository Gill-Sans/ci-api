package com.capit.capitschedule.domain.impl.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetailDto extends SessionSummaryDto {
    private String description;
    private String speaker;
}
