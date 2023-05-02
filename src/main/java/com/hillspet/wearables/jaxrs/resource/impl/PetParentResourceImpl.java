package com.hillspet.wearables.jaxrs.resource.impl;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.builders.JaxrsJsonResponseBuilder;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.dto.PetParentDTO;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.jaxrs.resource.PetParentResource;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.PetParentRequest;
import com.hillspet.wearables.response.PetParentListResponse;
import com.hillspet.wearables.response.PetParentResponse;
import com.hillspet.wearables.security.Authentication;
import com.hillspet.wearables.service.petparent.PetParentService;

/**
 * This class providing pet parent details.
 * 
 * @author sgorle
 * @since w1.0
 * @version w1.0
 * @version Nov 9, 2020
 */
@Service
public class PetParentResourceImpl implements PetParentResource {

	private static final Logger LOGGER = LogManager.getLogger(PetParentResourceImpl.class);

	@Autowired
	private PetParentService petParentService;

	@Autowired
	private Authentication authentication;

	@Autowired
	private JaxrsJsonResponseBuilder responseBuilder;

	@Override
	public Response addPetParent(PetParentRequest petParentRequest) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		petParentRequest.setUserId(userId);
		PetParentDTO petParentDto = petParentService.addPetParent(petParentRequest);
		PetParentResponse response = new PetParentResponse();
		response.setPetParentDto(petParentDto);
		// Step 5: build a successful response
		SuccessResponse<PetParentResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updatePetParent(PetParentRequest petParentRequest) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		petParentRequest.setUserId(userId);
		PetParentDTO petParentDto = petParentService.updatePetParent(petParentRequest);
		PetParentResponse response = new PetParentResponse();
		response.setPetParentDto(petParentDto);
		// Step 5: build a successful response
		SuccessResponse<PetParentResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParentList(BaseFilter filter) {
		filter.setUserId(authentication.getAuthUserDetails().getUserId());
		PetParentListResponse response = petParentService.getPetParentList(filter);
		SuccessResponse<PetParentListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParentById(int petParentId) {
		PetParentDTO petParentDto = petParentService.getPetParentById(petParentId);
		PetParentResponse response = new PetParentResponse();
		response.setPetParentDto(petParentDto);
		SuccessResponse<PetParentResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParents() {
		PetParentListResponse response = petParentService.getPetParents();
		SuccessResponse<PetParentListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response deletePetParent(int petParentId) {
		int modifiedBy = authentication.getAuthUserDetails().getUserId();

		// process
		petParentService.deletePetParent(petParentId, modifiedBy);
		// build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}
}
