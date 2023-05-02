package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.DefectiveSensorAction;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectiveSensorActionResponse {
    private List<DefectiveSensorAction> defectiveSensorActionList;

    public List<DefectiveSensorAction> getDefectiveSensorActionList() {
        return defectiveSensorActionList;
    }

    public void setDefectiveSensorActionList(List<DefectiveSensorAction> defectiveSensorActionList) {
        this.defectiveSensorActionList = defectiveSensorActionList;
    }
}
