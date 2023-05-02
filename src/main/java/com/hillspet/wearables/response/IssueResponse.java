package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.Issue;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueResponse {
    private List<Issue> issueList;

    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }
}
