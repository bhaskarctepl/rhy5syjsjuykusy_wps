package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.RootCause;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RootCauseResponse {
    List<RootCause> rootCauseList ;

    public List<RootCause> getRootCauseList() {
        return rootCauseList;
    }

    public void setRootCauseList(List<RootCause> rootCauseList) {
        this.rootCauseList = rootCauseList;
    }
}
