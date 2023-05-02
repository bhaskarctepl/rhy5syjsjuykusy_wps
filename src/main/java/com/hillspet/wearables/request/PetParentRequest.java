package com.hillspet.wearables.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author sgorle
 *
 */
@ApiModel(description = "Contains all information that needed to create a pet parent", value = "PetParentRequest")
@JsonInclude(value = Include.NON_NULL)
public class PetParentRequest {

	@ApiModelProperty(value = "petParentId", required = false, example = "1")
	private Integer petParentId;

	@ApiModelProperty(value = "petParentName", required = true, example = "John")
	private String petParentName;

	@ApiModelProperty(value = "email", required = true, example = "John@gmail.com")
	private String email;

	@ApiModelProperty(value = "phoneNumber", required = true, example = "+1 (846) 546-5468")
	private String phoneNumber;

	@ApiModelProperty(value = "shippingAddress", required = false, example = "Mr John Smith. 132, My Street, Kingston, New York 12401")
	private String shippingAddress;

	@ApiModelProperty(value = "petsAssociated", required = true)
	private List<Integer> petsAssociated;

	@ApiModelProperty(value = "status", required = true, example = "1")
	private Integer status;

	private Integer userId;
	@ApiModelProperty(value = "petParentFirstName", required = true)
	private String petParentFirstName;
	@ApiModelProperty(value = "petParentLastName", required = true)
	private String petParentLastName;

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

	public List<Integer> getPetsAssociated() {
		return petsAssociated;
	}

	public void setPetsAssociated(List<Integer> petsAssociated) {
		this.petsAssociated = petsAssociated;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
