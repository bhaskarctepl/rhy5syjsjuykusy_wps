package com.hillspet.wearables.dto.filter;

import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiParam;

public class PetFilter extends BaseFilter {

	@QueryParam("studyId")
	@ApiParam(name = "studyId", type = "integer", value = "Search based on Study drop down")
	private String studyId;
	
	@QueryParam("petId")
	@ApiParam(name = "petId", type = "integer", value = "Search Pet Id", required = false)
	private int petId;

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	
	

	

}
