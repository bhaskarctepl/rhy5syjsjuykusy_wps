package com.hillspet.wearables.request;

import io.swagger.annotations.ApiModelProperty;

public class ImageScoringScalesAssociated {

	@ApiModelProperty(value = "imageScoringId", required = true)
	private Integer imageScoringId;

	@ApiModelProperty(value = "imageScaleName", required = false)
	private String imageScaleName;

	@ApiModelProperty(value = "frequencyId", required = true)
	private Integer frequencyId;

	@ApiModelProperty(value = "startDate", required = true)
	private String startDate;

	@ApiModelProperty(value = "endDate", required = true)
	private String endDate;

	@ApiModelProperty(value = "frequencyName", required = false)
	private String frequencyName;

	public Integer getImageScoringId() {
		return imageScoringId;
	}

	public void setImageScoringId(Integer imageScoringId) {
		this.imageScoringId = imageScoringId;
	}

	public String getImageScaleName() {
		return imageScaleName;
	}

	public void setImageScaleName(String imageScaleName) {
		this.imageScaleName = imageScaleName;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFrequencyName() {
		return frequencyName;
	}

	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}

}
