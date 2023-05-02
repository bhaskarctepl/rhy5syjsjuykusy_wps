package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetWeightHistoryDTO;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetWeightHistoryResponse extends BaseResultCollection {
	private List<PetWeightHistoryDTO> weightList;

	@ApiModelProperty(value = "Get Pet Weight details of particular pet id.")
	public List<PetWeightHistoryDTO> getWeightList() {
		return weightList;
	}

	public void setWeightList(List<PetWeightHistoryDTO> weightList) {
		this.weightList = weightList;
	}


}
