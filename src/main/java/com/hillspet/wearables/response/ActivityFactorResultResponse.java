package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityFactorResultResponse {

	private String studyName;
	private String extPetValue;
	private String afCalculatedDate;
	private String algorithmName;
	private String algorithmVerion;
	private String traditionalEe;
	private String estimatedEnergyExpenditure;
	private String estimatedStepCount;
	private String estimatedAf;
	private String recommendedFeedAmt;
	private String feedUnits;

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public String getExtPetValue() {
		return extPetValue;
	}

	public void setExtPetValue(String extPetValue) {
		this.extPetValue = extPetValue;
	}

	public String getAfCalculatedDate() {
		return afCalculatedDate;
	}

	public void setAfCalculatedDate(String afCalculatedDate) {
		this.afCalculatedDate = afCalculatedDate;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmVerion() {
		return algorithmVerion;
	}

	public void setAlgorithmVerion(String algorithmVerion) {
		this.algorithmVerion = algorithmVerion;
	}

	public String getTraditionalEe() {
		return traditionalEe;
	}

	public void setTraditionalEe(String traditionalEe) {
		this.traditionalEe = traditionalEe;
	}

	public String getEstimatedEnergyExpenditure() {
		return estimatedEnergyExpenditure;
	}

	public void setEstimatedEnergyExpenditure(String estimatedEnergyExpenditure) {
		this.estimatedEnergyExpenditure = estimatedEnergyExpenditure;
	}

	public String getEstimatedStepCount() {
		return estimatedStepCount;
	}

	public void setEstimatedStepCount(String estimatedStepCount) {
		this.estimatedStepCount = estimatedStepCount;
	}

	public String getEstimatedAf() {
		return estimatedAf;
	}

	public void setEstimatedAf(String estimatedAf) {
		this.estimatedAf = estimatedAf;
	}

	public String getRecommendedFeedAmt() {
		return recommendedFeedAmt;
	}

	public void setRecommendedFeedAmt(String recommendedFeedAmt) {
		this.recommendedFeedAmt = recommendedFeedAmt;
	}

	public String getFeedUnits() {
		return feedUnits;
	}

	public void setFeedUnits(String feedUnits) {
		this.feedUnits = feedUnits;
	}

}
