package com.hillspet.wearables.dao.study;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.PetListDTO;
import com.hillspet.wearables.dto.PreludeDataByStudyDTO;
import com.hillspet.wearables.dto.PreludeListDTO;
import com.hillspet.wearables.dto.QuestionnaireListDTO;
import com.hillspet.wearables.dto.StudyDTO;
import com.hillspet.wearables.dto.StudyImageScale;
import com.hillspet.wearables.dto.StudyListDTO;
import com.hillspet.wearables.dto.StudyNotification;
import com.hillspet.wearables.dto.PushNotification;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.StudyFilter;
import com.hillspet.wearables.request.PushNotificationsAssociated;
import com.hillspet.wearables.request.QuestionnaireAssociated;
import com.hillspet.wearables.request.StudyNotificationRequest;
import com.hillspet.wearables.request.StudyRequest;

public interface StudyDao {

	/* ---------------- STUDY SERVICES ------------------------- */

	void addStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException;

	void updateStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException;

	void associatePlan(int studyId, int planId, LocalDate subscriptionDate, Integer userId)
			throws ServiceExecutionException;

	void disassociatePlan(int studyId, int planId, Integer userId) throws ServiceExecutionException;

	List<PetListDTO> getAssociatedPets(int studyId) throws ServiceExecutionException;

	Map<String, Integer> getStudyListCount(StudyFilter filter) throws ServiceExecutionException;

	List<StudyListDTO> getStudies(StudyFilter filter) throws ServiceExecutionException;

	List<StudyListDTO> getStudyList(int userId) throws ServiceExecutionException;

	List<StudyListDTO> getStudyListByUser(int userId) throws ServiceExecutionException;

	List<StudyListDTO> getAllStudyList() throws ServiceExecutionException;

	List<StudyListDTO> getStudiesByPetParentAndPet(int petParentId, int petId) throws ServiceExecutionException;

	StudyDTO getStudyById(int studyId) throws ServiceExecutionException;

	void deleteStudy(int studyId, int modifiedBy) throws ServiceExecutionException;

	void associateQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException;

	void updateStudyQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException;

	void disassociateQuestionnaire(int studyId, int questionnaireId, Integer userId) throws ServiceExecutionException;

	List<QuestionnaireListDTO> getAssociatedQuestionnaires(int studyId) throws ServiceExecutionException;

	List<PreludeListDTO> getPreludeDataList(int studyId) throws ServiceExecutionException;

	List<PreludeListDTO> getAFPreludeDataList(int studyId) throws ServiceExecutionException;

	List<PreludeDataByStudyDTO> getPreludeDataByStudy(String studyName) throws ServiceExecutionException;

	List<PreludeDataByStudyDTO> getAFPreludeDataByStudy(String studyName) throws ServiceExecutionException;

	/* ---------------- STUDY NOTIFICATIONS SERVICES ------------------------- */

	Map<String, Integer> getStudyNotificationCount(BaseFilter filter) throws ServiceExecutionException;

	List<StudyNotification> getStudyNotifications(BaseFilter filter) throws ServiceExecutionException;

	void updateStudyNotificationStatus(StudyNotificationRequest studyNotificationRequest, int modifiedBy)
			throws ServiceExecutionException;

	/*
	 * ---------------- STUDY PUSH NOTIFICATIONS SERVICES -------------------------
	 */
	void associatePushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) throws ServiceExecutionException;

	void updateStudyPushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) throws ServiceExecutionException;

	List<PushNotification> getAssociatedPushNotifications(int studyId) throws ServiceExecutionException;

	void disassociateStudyPushNotifications(int studyId, int notificationId, Integer userId)
			throws ServiceExecutionException;

	List<StudyImageScale> associatedImageScales(int studyId) throws ServiceExecutionException;


}
