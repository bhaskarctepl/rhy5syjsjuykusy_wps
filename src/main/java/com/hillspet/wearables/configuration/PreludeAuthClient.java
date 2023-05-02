package com.hillspet.wearables.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author: sgorle
 * @date: 14-09-2022
 * 
 */
@Component
public class PreludeAuthClient {

	private static final Logger LOGGER = LogManager.getLogger(PreludeAuthClient.class);
	private final RestTemplate restTemplate;

	private final String krebAuthTokenURL = "https://krebcat.preludedynamics.com/vision/api?username=cte-clinicals&password=Hills1234&get=auth";
	private final String authTokenURL = "/api?username=cte-clinicals&password=Hills1234&get=auth";

	public PreludeAuthClient(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.basicAuthentication("cte-clinicals", "Hills1234").build();
	}

	public String getPreludeAuthToken(String studyUrl) {
		
		System.out.println("================================================= "+studyUrl + authTokenURL);
		
		final ResponseEntity<String> responseEntity;

		if (studyUrl.isEmpty() || studyUrl.contains("krebcat.preludedynamics.com")) {
			responseEntity = restTemplate.getForEntity(krebAuthTokenURL, String.class);
		} else {
			responseEntity = restTemplate.getForEntity(studyUrl + authTokenURL, String.class);
		}
		return responseEntity.getBody();
	}
}