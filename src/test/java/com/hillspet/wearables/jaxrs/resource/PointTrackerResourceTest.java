package com.hillspet.wearables.jaxrs.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointTrackerResourceTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private String obtainAccessToken(String username, String password) throws Exception {
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append("http://localhost:");
        urlStringBuilder.append(randomServerPort);
        urlStringBuilder.append("/wearables/oauth/token?");
        urlStringBuilder.append("username=");
        urlStringBuilder.append(username);
        urlStringBuilder.append("&password=");
        urlStringBuilder.append(password);
        urlStringBuilder.append("&grant_type=");
        urlStringBuilder.append("password");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic YWRtaW5DbGllbnRJZDp3ZWFyYWJsZXNBZG1pbg==");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(urlStringBuilder.toString(), HttpMethod.POST, entity, String.class);
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(response.getBody()).get("access_token").toString();
    }

    // Integration testing
    @Test
    @DisplayName("Testing get point tracker by id success")
    void getPointTrackerById() throws Exception {
       String accessId = this.obtainAccessToken("cteadmin","Admin@123");
       System.out.println(accessId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessId);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + randomServerPort +"/wearables/api/pointTrackers/1" , HttpMethod.GET, entity, String.class);
        String expected = "{ \"status\": { \"success\": true, \"httpStatus\": 200 }, \"response\": { \"noOfElements\": 0, \"totalElements\": 0, \"pointTracker\": { \"studyName\": \"AGL Technology Inc\", \"trackerName\": \"customer\", \"studyId\": 1, \"startDate\": \"2020-10-12\", \"endDate\": \"2021-10-12\", \"status\": 1, \"totalPoints\": \"10\", \"activities\": \"Observations\", \"activityIds\": \"1\", \"pointTrackerAssociatedObject\": [ { \"id\": 1, \"points\": \"10\", \"activites_Names\": \"Observations\" } ] }, \"rows\": null } }";
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
    }



}