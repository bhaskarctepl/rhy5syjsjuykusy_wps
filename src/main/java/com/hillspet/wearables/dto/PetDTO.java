package com.hillspet.wearables.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

/**
 * @author vvodyaram
 *
 */
@ApiModel(description = "Contains all information that needed to create Pet", value = "PetRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetDTO extends BaseDTO {

	private Integer petId;
	private String petName;
	private String photoName;
	private Integer breedId;
	private String breedName;
	private String gender;
	private Double weight;
	private String weightUnit;
	private LocalDate dateOfBirth;
	private Boolean isDobUnknown;
	private Boolean isMixed;
	private Boolean isNeutered;
	private Boolean isDeceased;
	private Boolean isActive;
	private Boolean isExternalPet;
	private Integer speciesId;
	private String speciesName;
	private String selectStudyId;

	private Integer questionnaireAttempted;
	private Integer studyId;
	private String studyName;
	private LocalDate startDate;
	private LocalDate endDate;

	private List<PetDevice> petDevices;
	private List<PetParentDTO> petParents;
	private Integer petStatusId;
	private String petStatus;
	private Integer userId;
	private String petImage;
	private String petPhotoUrl;
	private List<PetNote> petNotes;
	private String feedingPreferences;

	public String getSelectStudyId() {
		return selectStudyId;
	}

	public void setSelectStudyId(String selectStudyId) {
		this.selectStudyId = selectStudyId;
	}

	public String getSpeciesName() {
		return speciesName;
	}

	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

	public Boolean getIsExternalPet() {
		return isExternalPet;
	}

	public void setIsExternalPet(Boolean isExternalPet) {
		this.isExternalPet = isExternalPet;
	}

	public List<PetNote> getPetNotes() {
		return petNotes;
	}

	public void setPetNotes(List<PetNote> petNotes) {
		this.petNotes = petNotes;
	}

	public String getPetPhotoUrl() {
		return petPhotoUrl;
	}

	public void setPetPhotoUrl(String petPhotoUrl) {
		this.petPhotoUrl = petPhotoUrl;
	}

	public String getPetImage() {
		return petImage;
	}

	public void setPetImage(String petImage) {
		this.petImage = petImage;
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

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public Integer getBreedId() {
		return breedId;
	}

	public void setBreedId(Integer breedId) {
		this.breedId = breedId;
	}

	public String getBreedName() {
		return breedName;
	}

	public void setBreedName(String breedName) {
		this.breedName = breedName;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Boolean getIsMixed() {
		return isMixed;
	}

	public void setIsMixed(Boolean isMixed) {
		this.isMixed = isMixed;
	}

	public Boolean getIsNeutered() {
		return isNeutered;
	}

	public void setIsNeutered(Boolean isNeutered) {
		this.isNeutered = isNeutered;
	}

	public Boolean getIsDeceased() {
		return isDeceased;
	}

	public void setIsDeceased(Boolean isDeceased) {
		this.isDeceased = isDeceased;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getQuestionnaireAttempted() {
		return questionnaireAttempted;
	}

	public void setQuestionnaireAttempted(Integer questionnaireAttempted) {
		this.questionnaireAttempted = questionnaireAttempted;
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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public List<PetDevice> getPetDevices() {
		return petDevices;
	}

	public void setPetDevices(List<PetDevice> petDevices) {
		this.petDevices = petDevices;
	}

	public List<PetParentDTO> getPetParents() {
		return petParents;
	}

	public void setPetParents(List<PetParentDTO> petParents) {
		this.petParents = petParents;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFeedingPreferences() {
		return feedingPreferences;
	}

	public void setFeedingPreferences(String feedingPreferences) {
		this.feedingPreferences = feedingPreferences;
	}

	public Boolean getIsDobUnknown() {
		return isDobUnknown;
	}

	public void setIsDobUnknown(Boolean isDobUnknown) {
		this.isDobUnknown = isDobUnknown;
	}

}
