package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.InventoryStatus;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryStatusResponse {
    private List<InventoryStatus> inventoryStatusList;

    public List<InventoryStatus> getInventoryStatusList() {
        return inventoryStatusList;
    }

    public void setInventoryStatusList(List<InventoryStatus> inventoryStatusList) {
        this.inventoryStatusList = inventoryStatusList;
    }
}
