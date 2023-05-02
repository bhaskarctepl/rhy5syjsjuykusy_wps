package com.hillspet.wearables.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetStudyDTO;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetResponse {

	private PetDTO petDTO;

	@ApiModelProperty(value = "Get Pet details of particular id")
	public PetDTO getPetDTO() {
		return petDTO;
	}

	public void setPetDTO(PetDTO petDTO) {
		this.petDTO = petDTO;
	}

}
