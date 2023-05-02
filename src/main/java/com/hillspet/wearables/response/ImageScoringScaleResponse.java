package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hillspet.wearables.dto.ImageScoringScale;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageScoringScaleResponse extends BaseResultCollection {

	private ImageScoringScale imageScoringScale;

	public void setImageScoringScale(ImageScoringScale imageScoringScale) {
		this.imageScoringScale = imageScoringScale;
	}

	@ApiModelProperty(value = "Get Image Scoring Scale by particular id")
	public ImageScoringScale getImageScoringScale() {
		return imageScoringScale;
	}
}
