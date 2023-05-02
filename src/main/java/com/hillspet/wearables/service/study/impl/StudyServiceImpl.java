package com.hillspet.wearables.service.study.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.study.StudyDao;
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
import com.hillspet.wearables.service.study.StudyService;

@Service
public class StudyServiceImpl implements StudyService {

	private static final Logger LOGGER = LogManager.getLogger(StudyServiceImpl.class);

	@Autowired
	private StudyDao studyDao;

	/* ---------------- STUDY SERVICES ------------------------- */

	@Override
	public void addStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException {
		LOGGER.debug("addStudy called");
		studyDao.addStudy(studyRequest, userId);
		LOGGER.debug("addStudy completed successfully");
	}

	@Override
	public void updateStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException {
		LOGGER.debug("updateStudy called");
		studyDao.updateStudy(studyRequest, userId);
		LOGGER.debug("updateStudy completed successfully");
	}

	@Override
	public void associatePlan(int studyId, int planId, LocalDate subscriptionDate, Integer userId)
			throws ServiceExecutionException {
		LOGGER.debug("associatePlan called");
		studyDao.associatePlan(studyId, planId, subscriptionDate, userId);
		LOGGER.debug("associatePlan completed successfully");

	}

	@Override
	public void disassociatePlan(int studyId, int planId, Integer userId) throws ServiceExecutionException {
		LOGGER.debug("disassociatePlan called");
		studyDao.disassociatePlan(studyId, planId, userId);
		LOGGER.debug("disassociatePlan completed successfully");
	}

	@Override
	public PetsResponse getAssociatedPets(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getAssociatedPets called");
		List<PetListDTO> petsList = studyDao.getAssociatedPets(studyId);

		PetsResponse response = new PetsResponse();
		response.setPets(petsList);

		LOGGER.debug("getAssociatedPets study count is {}", petsList.size());
		LOGGER.debug("getAssociatedPets completed successfully");
		return response;
	}

	@Override
	public StudyListResponse getStudies(StudyFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getStudies called");
		Map<String, Integer> mapper = studyDao.getStudyListCount(filter);
		int total =	mapper.get("searchedElementsCount");
		int totalCount = mapper.get("totalCount");
		List<StudyListDTO> studyList = total > 0 ? studyDao.getStudies(filter) : new ArrayList<>();
		StudyListResponse response = new StudyListResponse();
		response.setStudies(studyList);
		response.setNoOfElements(studyList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		LOGGER.debug("getStudies study count is {}", studyList);
		LOGGER.debug("getStudies completed successfully");
		return response;
	}

	@Override
	public StudyListResponse getStudyList(int userId) throws ServiceExecutionException {
		LOGGER.debug("getStudyList called");
		List<StudyListDTO> studyList = studyDao.getStudyList(userId);

		StudyListResponse response = new StudyListResponse();
		response.setStudyList(studyList);

		LOGGER.debug("getStudyList study count is {}", studyList);
		LOGGER.debug("getStudyList completed successfully");
		return response;
	}
	
	@Override
	public StudyListResponse getStudyListByUser(int userId) throws ServiceExecutionException {
		LOGGER.debug("getStudyList called");
		List<StudyListDTO> studyList = studyDao.getStudyListByUser(userId);

		StudyListResponse response = new StudyListResponse();
		response.setStudyList(studyList);

		LOGGER.debug("getStudyList study count is {}", studyList);
		LOGGER.debug("getStudyList completed successfully");
		return response;
	}

	@Override
	public StudyListResponse getStudiesByPetParentAndPet(int petParentId, int petId) throws ServiceExecutionException {
		LOGGER.debug("getStudiesByPetParentAndPet called");
		List<StudyListDTO> studyList = studyDao.getStudiesByPetParentAndPet(petParentId, petId);

		StudyListResponse response = new StudyListResponse();
		response.setStudyList(studyList);

		LOGGER.debug("getStudiesByPetParentAndPet study count is {}", studyList);
		LOGGER.debug("getStudiesByPetParentAndPet completed successfully");
		return response;
	}

	@Override
	public StudyResponse getStudyById(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getStudyById called");
		StudyDTO studyDto = studyDao.getStudyById(studyId);

		StudyResponse response = new StudyResponse();
		response.setStudy(studyDto);

		LOGGER.debug("getStudyById completed successfully");
		return response;
	}

	@Override
	public void deleteStudy(int studyId, int modifiedBy) throws ServiceExecutionException {
		LOGGER.debug("deleteStudy called");
		studyDao.deleteStudy(studyId, modifiedBy);
		LOGGER.debug("deleteStudy completed successfully");
	}

	@Override
	public void associateQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException {
		LOGGER.debug("associateQuestionnaire called");
		studyDao.associateQuestionnaire(studyId, questionnaireAssociated, userId);
		LOGGER.debug("associateQuestionnaire completed successfully");

	}

	@Override
	public void updateStudyQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException {
		LOGGER.debug("updateStudyQuestionnaire called");
		studyDao.updateStudyQuestionnaire(studyId, questionnaireAssociated, userId);
		LOGGER.debug("updateStudyQuestionnaire completed successfully");

	}

	@Override
	public void disassociateQuestionnaire(int studyId, int questionnaireId, Integer userId)
			throws ServiceExecutionException {
		LOGGER.debug("disassociateQuestionnaire called");
		studyDao.disassociateQuestionnaire(studyId, questionnaireId, userId);
		LOGGER.debug("disassociateQuestionnaire completed successfully");
	}

	@Override
	public QuestionnaireListResponse getAssociatedQuestionnaires(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getAssociatedQuestionnaires called");
		List<QuestionnaireListDTO> questionnaires = studyDao.getAssociatedQuestionnaires(studyId);

		QuestionnaireListResponse response = new QuestionnaireListResponse();
		response.setQuestionnaires(questionnaires);

		LOGGER.debug("getAssociatedQuestionnaires study count is {}", questionnaires.size());
		LOGGER.debug("getAssociatedQuestionnaires completed successfully");
		return response;
	}

	/* ---------------- STUDY NOTIFICATIONS SERVICES ------------------------- */

	@Override
	public StudyNotificationResponse getStudyNotifications(BaseFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getStudyNotifications called");
		Map<String, Integer> mapper = studyDao.getStudyNotificationCount(filter);
		int total =	mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<StudyNotification> studyNotifications = total > 0 ? studyDao.getStudyNotifications(filter)
				: new ArrayList<>();
		StudyNotificationResponse response = new StudyNotificationResponse();
		response.setStudyNotifications(studyNotifications);
		response.setNoOfElements(studyNotifications.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);

		LOGGER.debug("getStudyNotifications studyNotifications is {}", studyNotifications);
		LOGGER.debug("getStudyNotifications completed successfully");
		return response;
	}

	@Override
	public void updateStudyNotificationStatus(StudyNotificationRequest studyNotificationRequest, int modifiedBy)
			throws ServiceExecutionException {
		LOGGER.debug("updateStudyNotificationStatus called");
		studyDao.updateStudyNotificationStatus(studyNotificationRequest, modifiedBy);
		LOGGER.debug("updateStudyNotificationStatus completed successfully");
	}

	@Override
	public StudyListResponse getAllStudyList() throws ServiceExecutionException {
		LOGGER.debug("getStudyList called");
		List<StudyListDTO> studyList = studyDao.getAllStudyList();
		StudyListResponse response = new StudyListResponse();
		response.setStudyList(studyList);

		LOGGER.debug("getStudyList study count is {}", studyList);
		LOGGER.debug("getStudyList completed successfully");
		return response;
	}
	
	
	@Override
	public PreludeListResponse getPreludeDataList(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getPreludeDataList called");
		List<PreludeListDTO> preludeList = studyDao.getPreludeDataList(studyId);
		PreludeListResponse response = new PreludeListResponse();
		response.setPreludeList(preludeList);;

		LOGGER.debug("getPreludeDataList study count is {}", preludeList);
		LOGGER.debug("getPreludeDataList completed successfully");
		return response;
	}
	
	
	@Override
	public PreludeDataByStudyResponse getPreludeDataByStudy(String studyName) throws ServiceExecutionException {
		LOGGER.debug("getPreludeDataByStudy called");
		List<PreludeDataByStudyDTO> preludeList = studyDao.getPreludeDataByStudy(studyName);
		PreludeDataByStudyResponse response = new PreludeDataByStudyResponse();
		response.setPreludeDataByStudyList(preludeList);

		LOGGER.debug("getPreludeDataByStudy study count is {}", preludeList);
		LOGGER.debug("getPreludeDataByStudy completed successfully");
		return response;
	}

	//Associating study push notifications
	
	@Override
	public void associatePushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) {
		LOGGER.debug("associatePushNotifications called");
		studyDao.associatePushNotifications(studyId, pushNotificationsAssociated, userId);
		LOGGER.debug("associatePushNotifications completed successfully");

	}

	@Override
	public void updateStudyPushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) {
		LOGGER.debug("updateStudyPushNotifications called");
		studyDao.updateStudyPushNotifications(studyId, pushNotificationsAssociated, userId);
		LOGGER.debug("updateStudyPushNotifications completed successfully");

	}

	@Override
	public void disassociateStudyPushNotifications(int studyId, int notificationId, Integer userId) {
		LOGGER.debug("disassociateStudyPushNotifications called");
		studyDao.disassociateStudyPushNotifications(studyId, notificationId, userId);
		LOGGER.debug("disassociateStudyPushNotifications completed successfully");
	}

	@Override
	public PushNotificationListResponse getAssociatedPushNotifications(int studyId) {
		LOGGER.debug("getAssociatedPushNotifications called");
		List<PushNotification> pushNotificationList = studyDao.getAssociatedPushNotifications(studyId);

		PushNotificationListResponse response = new PushNotificationListResponse();
		response.setPushNotifications(pushNotificationList); 

		LOGGER.debug("getAssociatedPushNotifications study count is {}", pushNotificationList.size());
		LOGGER.debug("getAssociatedPushNotifications completed successfully");
		return response;
	}

	@Override
	public StudyImageScalesListResponse associatedImageScales(int studyId) throws ServiceExecutionException {
		LOGGER.debug("associatedImageScales called");
		List<StudyImageScale> imageScaleList = studyDao.associatedImageScales(studyId);

		StudyImageScalesListResponse response = new StudyImageScalesListResponse();
		response.setStudyImageScaleList(imageScaleList); 

		LOGGER.debug("associatedImageScales sclaes count is {}", imageScaleList.size());
		LOGGER.debug("associatedImageScales completed successfully");
		return response;
	}

	@Override
	public PreludeListResponse getAFPreludeDataList(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getAFPreludeDataList called");
		List<PreludeListDTO> preludeList = studyDao.getAFPreludeDataList(studyId);
		PreludeListResponse response = new PreludeListResponse();
		response.setPreludeList(preludeList);;

		LOGGER.debug("getAFPreludeDataList study count is {}", preludeList);
		LOGGER.debug("getAFPreludeDataList completed successfully");
		return response;
	}

	@Override
	public PreludeDataByStudyResponse getAFPreludeDataByStudy(String studyName) throws ServiceExecutionException {
		LOGGER.debug("getAFPreludeDataByStudy called");
		List<PreludeDataByStudyDTO> preludeList = studyDao.getAFPreludeDataByStudy(studyName);
		PreludeDataByStudyResponse response = new PreludeDataByStudyResponse();
		response.setPreludeDataByStudyList(preludeList);

		LOGGER.debug("getAFPreludeDataByStudy study count is {}", preludeList);
		LOGGER.debug("getAFPreludeDataByStudy completed successfully");
		return response;
	}
}
