package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.SensorLocation;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorLocationResponse {
    private List<SensorLocation> sensorLocationList;

    public List<SensorLocation> getSensorLocationList() {
        return sensorLocationList;
    }

    public void setSensorLocationList(List<SensorLocation> sensorLocationList) {
        this.sensorLocationList = sensorLocationList;
    }
}
