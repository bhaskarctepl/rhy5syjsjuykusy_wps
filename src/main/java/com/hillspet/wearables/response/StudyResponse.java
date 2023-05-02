package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hillspet.wearables.dto.StudyDTO;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyResponse extends BaseResultCollection {

	private StudyDTO study;

	@JsonProperty("rows")
	@ApiModelProperty(value = "Study details by Study Id")
	public StudyDTO getStudy() {
		return study;
	}

	public void setStudy(StudyDTO study) {
		this.study = study;
	}
}
