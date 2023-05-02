package com.hillspet.wearables.jaxrs.resource.impl;

import java.time.LocalDate;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.builders.JaxrsJsonResponseBuilder;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.dto.CustomUserDetails;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.StudyFilter;
import com.hillspet.wearables.jaxrs.resource.StudyResource;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.PushNotificationsAssociated;
import com.hillspet.wearables.request.QuestionnaireAssociated;
import com.hillspet.wearables.request.StudyNotificationRequest;
import com.hillspet.wearables.request.StudyRequest;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.response.PreludeDataByStudyResponse;
import com.hillspet.wearables.response.PreludeListResponse;
import com.hillspet.wearables.response.QuestionnaireListResponse;
import com.hillspet.wearables.response.StudyImageScalesListResponse;
import com.hillspet.wearables.response.StudyListResponse;
import com.hillspet.wearables.response.StudyNotificationResponse;
import com.hillspet.wearables.response.StudyResponse;
import com.hillspet.wearables.security.Authentication;
import com.hillspet.wearables.service.study.PreludeService;
import com.hillspet.wearables.service.study.StudyService;

/**
 * This class if for providing study details.
 * 
 * @author sgorle
 * @since w1.0
 * @version w1.0
 * @version Nov 9, 2020
 */
@Service
public class StudyResourceImpl implements StudyResource {

	private static final Logger LOGGER = LogManager.getLogger(StudyResourceImpl.class);

	@Autowired
	private StudyService studyService;

	@Autowired
	private PreludeService preludeService;

	@Autowired
	private Authentication authentication;

	@Autowired
	private JaxrsJsonResponseBuilder responseBuilder;

	/* ---------------- STUDY SERVICES ------------------------- */

	@Override
	public Response AddStudy(StudyRequest studyRequest) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		studyService.addStudy(studyRequest, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updateStudy(StudyRequest studyRequest) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		studyService.updateStudy(studyRequest, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associatePlan(int studyId, int planId, String subscriptionDate) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		studyService.associatePlan(studyId, planId, LocalDate.parse(subscriptionDate), userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response disassociatePlan(int studyId, int planId) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		studyService.disassociatePlan(studyId, planId, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associatedPets(int studyId) {
		PetsResponse response = studyService.getAssociatedPets(studyId);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudies(StudyFilter filter) {
		CustomUserDetails userDetails = authentication.getAuthUserDetails();
		filter.setIsSuper(userDetails.getIsSuperAdmin());
		filter.setUserId(userDetails.getUserId());
		filter.setRoleTypeId(userDetails.getRoleTypeId());
		StudyListResponse response = studyService.getStudies(filter);
		SuccessResponse<StudyListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudyById(int studyId) {
		StudyResponse response = studyService.getStudyById(studyId);
		SuccessResponse<StudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudyList() {
		int userId = authentication.getAuthUserDetails().getUserId();
		StudyListResponse response = studyService.getStudyList(userId);
		SuccessResponse<StudyListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudiesByPetParentAndPet(int petParentId, int petId) {
		StudyListResponse response = studyService.getStudiesByPetParentAndPet(petParentId, petId);
		SuccessResponse<StudyListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response deleteStudy(int studyId) {
		int modifiedBy = authentication.getAuthUserDetails().getUserId();

		// process
		studyService.deleteStudy(studyId, modifiedBy);
		// build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associateQuestionnaire(int studyId, int questionnaireId, String startDate, String endDate) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		QuestionnaireAssociated questionnaireAssociated = new QuestionnaireAssociated();
		questionnaireAssociated.setQuestionnaireId(questionnaireId);
		questionnaireAssociated.setStartDate(startDate);
		questionnaireAssociated.setEndDate(endDate);
		studyService.associateQuestionnaire(studyId, questionnaireAssociated, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updateStudyQuestionnaire(int studyId, int questionnaireId, String startDate, String endDate) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		QuestionnaireAssociated questionnaireAssociated = new QuestionnaireAssociated();
		questionnaireAssociated.setQuestionnaireId(questionnaireId);
		questionnaireAssociated.setStartDate(startDate);
		questionnaireAssociated.setEndDate(endDate);
		studyService.updateStudyQuestionnaire(studyId, questionnaireAssociated, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response disassociateQuestionnaire(int studyId, int questionnaireId) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		studyService.disassociateQuestionnaire(studyId, questionnaireId, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associatedQuestionnaires(int studyId) {
		QuestionnaireListResponse response = studyService.getAssociatedQuestionnaires(studyId);
		SuccessResponse<QuestionnaireListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/* ---------------- STUDY NOTIFICATIONS SERVICES ------------------------- */

	@Override
	public Response getStudyNotifications(BaseFilter filter) {
		Integer userId = authentication.getAuthUserDetails().getUserId();
		Integer roleTypeId = authentication.getAuthUserDetails().getRoleTypeId();
		filter.setUserId(userId);
		filter.setRoleTypeId(roleTypeId);
		StudyNotificationResponse response = studyService.getStudyNotifications(filter);
		SuccessResponse<StudyNotificationResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updateStudyNotificationStatus(StudyNotificationRequest studyNotificationRequest) {
		studyService.updateStudyNotificationStatus(studyNotificationRequest,
				authentication.getAuthUserDetails().getUserId());

		// Step 1: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Study notification status has been updated successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudyListByUser() {
		Integer userId = authentication.getAuthUserDetails().getUserId();

		StudyListResponse response = studyService.getStudyListByUser(userId);
		SuccessResponse<StudyListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAllStudyList() {

		StudyListResponse response = studyService.getAllStudyList();
		SuccessResponse<StudyListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPreludeDataList(int studyId) {

		PreludeListResponse response = studyService.getPreludeDataList(studyId);
		SuccessResponse<PreludeListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPreludeDataByStudy(String studyName) {

		PreludeDataByStudyResponse response = studyService.getPreludeDataByStudy(studyName);
		SuccessResponse<PreludeDataByStudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	// Study Push Notifications
	@Override
	public Response associatePushNotifications(int studyId, int notificationId, String startDate, String endDate) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		PushNotificationsAssociated pushNotificationsAssociated = new PushNotificationsAssociated();
		pushNotificationsAssociated.setNotificationId(notificationId);
		pushNotificationsAssociated.setStartDate(LocalDate.parse(startDate));
		pushNotificationsAssociated.setEndDate(LocalDate.parse(endDate));
		studyService.associatePushNotifications(studyId, pushNotificationsAssociated, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updateStudyPushNotifications(int studyId, int notificationId, String startDate, String endDate) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		PushNotificationsAssociated pushNotificationsAssociated = new PushNotificationsAssociated();
		pushNotificationsAssociated.setNotificationId(notificationId);
		pushNotificationsAssociated.setStartDate(LocalDate.parse(startDate));
		pushNotificationsAssociated.setEndDate(LocalDate.parse(endDate));
		studyService.updateStudyPushNotifications(studyId, pushNotificationsAssociated, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response disassociatePushNotifications(int studyId, int notificationId) {
		// Step 1: process
		Integer userId = authentication.getAuthUserDetails().getUserId();
		studyService.disassociateStudyPushNotifications(studyId, notificationId, userId);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associatedPushNotifications(int studyId) {
		PushNotificationListResponse response = studyService.getAssociatedPushNotifications(studyId);
		SuccessResponse<PushNotificationListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associatedImageScales(int studyId) {
		StudyImageScalesListResponse response = studyService.associatedImageScales(studyId);
		SuccessResponse<StudyImageScalesListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAFPreludeDataList(int studyId) {

		PreludeListResponse response = studyService.getAFPreludeDataList(studyId);
		SuccessResponse<PreludeListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAFPreludeDataByStudy(String studyName) {

		PreludeDataByStudyResponse response = studyService.getAFPreludeDataByStudy(studyName);
		SuccessResponse<PreludeDataByStudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	//@Override
	public Response loadPreludeData() {

		preludeService.loadPreludeData();
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(new CommonResponse());
		return responseBuilder.buildResponse(successResponse);
	}

}
