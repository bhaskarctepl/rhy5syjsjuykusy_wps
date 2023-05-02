package com.hillspet.wearables.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetParentListDTO extends BaseDTO {

	private Integer petParentId;
	private String petParentName;
	private String email;
	private String phoneNumber;
	private String shippingAddress;
	private String petNames;
	private String studyNames;
	private Integer numberOfPets;
	private boolean isOnStudyPetExist;
	private String petParentFirstName;
	private String petParentLastName;
	

	public boolean isOnStudyPetExist() {
		return isOnStudyPetExist;
	}

	public void setOnStudyPetExist(boolean isOnStudyPetExist) {
		this.isOnStudyPetExist = isOnStudyPetExist;
	}

	public String getPetParentFirstName() {
		return petParentFirstName;
	}

	public void setPetParentFirstName(String petParentFirstName) {
		this.petParentFirstName = petParentFirstName;
	}

	public String getPetParentLastName() {
		return petParentLastName;
	}

	public void setPetParentLastName(String petParentLastName) {
		this.petParentLastName = petParentLastName;
	}

	public Integer getPetParentId() {
		return petParentId;
	}

	public void setPetParentId(Integer petParentId) {
		this.petParentId = petParentId;
	}

	public String getPetParentName() {
		return petParentName;
	}

	public void setPetParentName(String petParentName) {
		this.petParentName = petParentName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPetNames() {
		return petNames;
	}

	public void setPetNames(String petNames) {
		this.petNames = petNames;
	}

	public String getStudyNames() {
		return studyNames;
	}

	public void setStudyNames(String studyNames) {
		this.studyNames = studyNames;
	}

	public Integer getNumberOfPets() {
		return numberOfPets;
	}

	public void setNumberOfPets(Integer numberOfPets) {
		this.numberOfPets = numberOfPets;
	}

}
