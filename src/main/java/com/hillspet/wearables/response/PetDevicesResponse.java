package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hillspet.wearables.dto.PetDevice;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDevicesResponse extends BaseResultCollection{
	private List<PetDevice> petDevices;

	@ApiModelProperty(value = "Get Pet Devices of particular id")
	@JsonProperty("rows")
	public List<PetDevice> getPetDevices() {
		return petDevices;
	}

	public void setPetDevices(List<PetDevice> petDevices) {
		this.petDevices = petDevices;
	}

}
