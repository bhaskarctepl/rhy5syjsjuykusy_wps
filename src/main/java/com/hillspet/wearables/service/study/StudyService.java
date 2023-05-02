package com.hillspet.wearables.service.study;

import java.time.LocalDate;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.StudyFilter;
import com.hillspet.wearables.jaxrs.resource.impl.PushNotificationListResponse;
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

/**
 * This is the service class for implementing study notification list details.
 * 
 * @author sgorle
 * @since w1.0
 * @version w1.0
 * 
 */
public interface StudyService {

	/* ---------------- STUDY SERVICES ------------------------- */

	void addStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException;

	void updateStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException;

	void associatePlan(int studyId, int planId, LocalDate subscriptionDate, Integer userId)
			throws ServiceExecutionException;

	void disassociatePlan(int studyId, int planId, Integer userId) throws ServiceExecutionException;

	PetsResponse getAssociatedPets(int studyId) throws ServiceExecutionException;

	StudyListResponse getStudies(StudyFilter filter) throws ServiceExecutionException;

	StudyListResponse getStudyList(int userId) throws ServiceExecutionException;

	StudyListResponse getStudyListByUser(int usrId) throws ServiceExecutionException;

	StudyListResponse getAllStudyList() throws ServiceExecutionException;

	StudyListResponse getStudiesByPetParentAndPet(int petParentId, int petId) throws ServiceExecutionException;

	StudyResponse getStudyById(int studyId) throws ServiceExecutionException;

	void deleteStudy(int studyId, int modifiedBy) throws ServiceExecutionException;

	void associateQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException;

	void updateStudyQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException;

	void disassociateQuestionnaire(int studyId, int questionnaireId, Integer userId) throws ServiceExecutionException;

	QuestionnaireListResponse getAssociatedQuestionnaires(int studyId) throws ServiceExecutionException;

	PreludeListResponse getPreludeDataList(int studyId) throws ServiceExecutionException;
	
	PreludeListResponse getAFPreludeDataList(int studyId) throws ServiceExecutionException;

	PreludeDataByStudyResponse getPreludeDataByStudy(String studyName) throws ServiceExecutionException;

	PreludeDataByStudyResponse getAFPreludeDataByStudy(String studyName) throws ServiceExecutionException;

	/* ---------------- STUDY NOTIFICATIONS SERVICES ------------------------- */

	StudyNotificationResponse getStudyNotifications(BaseFilter filter) throws ServiceExecutionException;

	void updateStudyNotificationStatus(StudyNotificationRequest studyNotificationRequest, int modifiedBy)
			throws ServiceExecutionException;

	/*
	 * ---------------- STUDY PUSH NOTIFICATIONS SERVICES -------------------------
	 */

	void associatePushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) throws ServiceExecutionException;

	void updateStudyPushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) throws ServiceExecutionException;

	void disassociateStudyPushNotifications(int studyId, int notificationId, Integer userId)
			throws ServiceExecutionException;

	PushNotificationListResponse getAssociatedPushNotifications(int studyId) throws ServiceExecutionException;

	StudyImageScalesListResponse associatedImageScales(int studyId) throws ServiceExecutionException;

}