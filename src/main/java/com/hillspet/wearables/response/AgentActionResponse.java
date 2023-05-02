package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.AgentAction;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentActionResponse {
    private List<AgentAction> agentActionList;

    public List<AgentAction> getAgentActionList() {
        return agentActionList;
    }

    public void setAgentActionList(List<AgentAction> agentActionList) {
        this.agentActionList = agentActionList;
    }
}
