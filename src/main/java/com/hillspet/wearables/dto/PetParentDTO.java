package com.hillspet.wearables.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetParentDTO {

	private Integer petParentId;
	private String petParentName;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String shippingAddress;
	private Integer status;

	private List<PetsAssociatedDTO> petsAssociated;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<PetsAssociatedDTO> getPetsAssociated() {
		return petsAssociated;
	}

	public void setPetsAssociated(List<PetsAssociatedDTO> petsAssociated) {
		this.petsAssociated = petsAssociated;
	}
}
