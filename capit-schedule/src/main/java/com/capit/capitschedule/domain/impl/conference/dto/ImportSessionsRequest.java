package com.capit.capitschedule.domain.impl.conference.dto;

import com.capit.capitschedule.integration.session.strategy.SessionImportStrategyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Objects;

/**
 * Request object for importing sessions into a conference.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImportSessionsRequest {
	private SessionImportStrategyType strategyType;
	private String strategyName;
	private String url;
	private HttpMethod httpMethod;
	private Map<String, String> additionalParams;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImportSessionsRequest that = (ImportSessionsRequest) o;
		return Objects.equals(strategyType, that.strategyType) &&
			   Objects.equals(strategyName, that.strategyName) &&
			   Objects.equals(url, that.url) &&
			   Objects.equals(httpMethod, that.httpMethod) &&
			   Objects.equals(additionalParams, that.additionalParams);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(strategyType, strategyName, url, httpMethod, additionalParams);
	}
}
