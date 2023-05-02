package com.hillspet.wearables.dto.filter;

import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiParam;

public class PetActivityFactorFilter extends BaseFilter {

	@QueryParam("petId")
	@ApiParam(name = "petId", type = "integer", value = "Search based on pet id")
	private int petId;

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}
	
	
}
