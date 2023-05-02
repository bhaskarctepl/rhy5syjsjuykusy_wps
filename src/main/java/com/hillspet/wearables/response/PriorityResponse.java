package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.Priority;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriorityResponse {
    private List<Priority> priorityList;

    public List<Priority> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<Priority> priorityList) {
        this.priorityList = priorityList;
    }
}
