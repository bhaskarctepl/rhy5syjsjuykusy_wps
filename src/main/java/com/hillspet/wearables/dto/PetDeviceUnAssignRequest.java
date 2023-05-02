package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDeviceUnAssignRequest {
	
	private Integer petAssetId;
	private Integer reasonId;
	
	private String unAssignedOn;
	
	
	public String getUnAssignedOn() {
		return unAssignedOn;
	}
	public void setUnAssignedOn(String unAssignedOn) {
		this.unAssignedOn = unAssignedOn;
	}
	public Integer getPetAssetId() {
		return petAssetId;
	}
	public void setPetAssetId(Integer petAssetId) {
		this.petAssetId = petAssetId;
	}
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	
}
