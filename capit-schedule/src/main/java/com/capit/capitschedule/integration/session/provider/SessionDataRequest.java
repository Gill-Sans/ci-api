package com.capit.capitschedule.integration.session.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDataRequest {
    private String conferenceId;
    private String url;
    private HttpMethod method;
    private Map<String, String> additionalParams;
}
