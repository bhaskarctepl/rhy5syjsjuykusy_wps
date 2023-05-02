package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.Status;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusResponse {
    private List<Status> statusList;

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }
}
