package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetListDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer petId;
	private String petName;
	private String petPhoto;
	private String breedName;
	private Integer studyId;
	private String studyName;
	private String sensorDetails;
	private String gender;
	private Integer petStatusId;
	private String petStatus;
	private String petPhotoUrl;
	private Integer selectStudyId;
	
	private Integer totalActivePets;
	private Integer totalActiveStudies;
	
	private Integer petStudyId;
	

	public Integer getPetStudyId() {
		return petStudyId;
	}

	public void setPetStudyId(Integer petStudyId) {
		this.petStudyId = petStudyId;
	}

	public Integer getTotalActivePets() {
		return totalActivePets;
	}

	public void setTotalActivePets(Integer totalActivePets) {
		this.totalActivePets = totalActivePets;
	}

	public Integer getTotalActiveStudies() {
		return totalActiveStudies;
	}

	public void setTotalActiveStudies(Integer totalActiveStudies) {
		this.totalActiveStudies = totalActiveStudies;
	}

	public Integer getSelectStudyId() {
		return selectStudyId;
	}

	public void setSelectStudyId(Integer selectStudyId) {
		this.selectStudyId = selectStudyId;
	}

	public String getPetPhotoUrl() {
		return petPhotoUrl;
	}

	public void setPetPhotoUrl(String petPhotoUrl) {
		this.petPhotoUrl = petPhotoUrl;
	}

	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetPhoto() {
		return petPhoto;
	}

	public void setPetPhoto(String petPhoto) {
		this.petPhoto = petPhoto;
	}

	public String getBreedName() {
		return breedName;
	}

	public void setBreedName(String breedName) {
		this.breedName = breedName;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public void setStudyId(Integer studyId) {
		this.studyId = studyId;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public String getSensorDetails() {
		return sensorDetails;
	}

	public void setSensorDetails(String sensorDetails) {
		this.sensorDetails = sensorDetails;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getPetStatusId() {
		return petStatusId;
	}

	public void setPetStatusId(Integer petStatusId) {
		this.petStatusId = petStatusId;
	}

	public String getPetStatus() {
		return petStatus;
	}

	public void setPetStatus(String petStatus) {
		this.petStatus = petStatus;
	}

}
