package com.hillspet.wearables.dto.filter;

import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiParam;

public class ImageScaleFilter extends BaseFilter {

	@ApiParam(name = "classificationId", value = "Search based on classification drop down")
	@QueryParam("classificationId")
	private String classificationId;

	@ApiParam(name = "scoringTypeId", value = "Search based on scoring Type drop down")
	@QueryParam("scoringTypeId")
	private String scoringTypeId;

	public ImageScaleFilter() {

	}

	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public String getScoringTypeId() {
		return scoringTypeId;
	}

	public void setScoringTypeId(String scoringTypeId) {
		this.scoringTypeId = scoringTypeId;
	}

}
