package com.hillspet.wearables.dao.lookup;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.AgentAction;
import com.hillspet.wearables.dto.AssignedUser;
import com.hillspet.wearables.dto.CategoryTimer;
import com.hillspet.wearables.dto.ContactMethod;
import com.hillspet.wearables.dto.CustomerContactMethod;
import com.hillspet.wearables.dto.CustomerContactReason;
import com.hillspet.wearables.dto.DefectiveSensorAction;
import com.hillspet.wearables.dto.DeviceLocation;
import com.hillspet.wearables.dto.DeviceStatus;
import com.hillspet.wearables.dto.EatingEnthusiasmScale;
import com.hillspet.wearables.dto.ExtractFileCategory;
import com.hillspet.wearables.dto.Frequency;
import com.hillspet.wearables.dto.ImageScoringType;
import com.hillspet.wearables.dto.InventoryStatus;
import com.hillspet.wearables.dto.Issue;
import com.hillspet.wearables.dto.MaterialCategory;
import com.hillspet.wearables.dto.MaterialType;
import com.hillspet.wearables.dto.Menu;
import com.hillspet.wearables.dto.MenuAction;
import com.hillspet.wearables.dto.MobileAppConfig;
import com.hillspet.wearables.dto.MobileAppFBPhoneModel;
import com.hillspet.wearables.dto.MobileAppFeedbackPage;
import com.hillspet.wearables.dto.Occurance;
import com.hillspet.wearables.dto.PetBreed;
import com.hillspet.wearables.dto.PetFeedingTime;
import com.hillspet.wearables.dto.PetName;
import com.hillspet.wearables.dto.PetNameTimer;
import com.hillspet.wearables.dto.PetParentName;
import com.hillspet.wearables.dto.PetParentNameTimer;
import com.hillspet.wearables.dto.PetSpecies;
import com.hillspet.wearables.dto.PetStatus;
import com.hillspet.wearables.dto.PointTracker;
import com.hillspet.wearables.dto.PointTrackerActivity;
import com.hillspet.wearables.dto.PointTrackerMetric;
import com.hillspet.wearables.dto.PointTrackerRejectReason;
import com.hillspet.wearables.dto.PreDefinedInstruction;
import com.hillspet.wearables.dto.PreDefinedQuestion;
import com.hillspet.wearables.dto.PreludeStudy;
import com.hillspet.wearables.dto.Priority;
import com.hillspet.wearables.dto.PushNotification;
import com.hillspet.wearables.dto.QuestionType;
import com.hillspet.wearables.dto.QuestionnaireCategory;
import com.hillspet.wearables.dto.QuestionnaireListDTO;
import com.hillspet.wearables.dto.QuestionnaireType;
import com.hillspet.wearables.dto.Role;
import com.hillspet.wearables.dto.RoleType;
import com.hillspet.wearables.dto.RootCause;
import com.hillspet.wearables.dto.SensorLocation;
import com.hillspet.wearables.dto.SensorName;
import com.hillspet.wearables.dto.ShipmentCompany;
import com.hillspet.wearables.dto.Status;
import com.hillspet.wearables.dto.StudyName;
import com.hillspet.wearables.dto.StudyNameFilter;
import com.hillspet.wearables.dto.TicketAssignedUser;
import com.hillspet.wearables.dto.TicketCategory;
import com.hillspet.wearables.dto.TicketPriority;
import com.hillspet.wearables.dto.TicketStatus;
import com.hillspet.wearables.dto.TicketType;

public interface LookupDao {

	List<RoleType> getRoleTypes() throws ServiceExecutionException;

	List<Menu> getMenus() throws ServiceExecutionException;

	List<MenuAction> getMenuActions() throws ServiceExecutionException;

	List<Role> getRoles() throws ServiceExecutionException;

	List<Role> getActiveRoles() throws ServiceExecutionException;

	List<PetBreed> getPetBreeds() throws ServiceExecutionException;

	List<PetStatus> getPetStatuses() throws ServiceExecutionException;

	List<MobileAppConfig> getMobileAppConfigs() throws ServiceExecutionException;

	/* ------------ Customer Support Lookup Services Start ---------------- */

	List<TicketType> getTicketTypes() throws ServiceExecutionException;

	List<TicketPriority> getTicketPriorities() throws ServiceExecutionException;

	List<TicketStatus> getTicketStatus() throws ServiceExecutionException;

	List<CustomerContactMethod> getCustomerContactMethods() throws ServiceExecutionException;

	List<CustomerContactReason> getCustomerContactReasons() throws ServiceExecutionException;

	List<TicketCategory> getTicketCategory() throws ServiceExecutionException;

	List<Issue> getIssues() throws ServiceExecutionException;

	List<RootCause> getRootCause(Integer issueId) throws ServiceExecutionException;

	List<AgentAction> getAgentAction(Integer testId) throws ServiceExecutionException;

	List<DefectiveSensorAction> getDefectiveSensorAction() throws ServiceExecutionException;

	List<PetName> getPetName() throws ServiceExecutionException;

	List<PetParentName> getPetParentName(Integer petId) throws ServiceExecutionException;

	List<StudyName> getStudyName(Integer petId) throws ServiceExecutionException;

	List<StudyName> getActiveStudy() throws ServiceExecutionException;

	List<SensorName> getSensorName(Integer studyId, Integer petId) throws ServiceExecutionException;

	List<SensorLocation> getSensorLocation() throws ServiceExecutionException;

	List<InventoryStatus> getInventoryStatus(Integer deviceId) throws ServiceExecutionException;

	List<Priority> getPriority() throws ServiceExecutionException;

	List<AssignedUser> getAssignedTo() throws ServiceExecutionException;

	List<Status> getStatus() throws ServiceExecutionException;

	List<ContactMethod> getContactMethod() throws ServiceExecutionException;

	List<String> getDeviceType() throws ServiceExecutionException;

	List<String> getDeviceModel(String deviceType) throws ServiceExecutionException;

	List<TicketAssignedUser> getAssignedToUsers() throws ServiceExecutionException;

	/* ------------ Questionnaire Lookup Services Start ---------------- */

	List<QuestionType> getQuestionType() throws ServiceExecutionException;

	List<PreDefinedInstruction> getPreDefinedInstructions() throws ServiceExecutionException;

	List<PreDefinedQuestion> getPreDefinedQuestions(String searchText) throws ServiceExecutionException;

	List<QuestionnaireListDTO> getQuestionnaires(String searchText) throws ServiceExecutionException;

	List<QuestionnaireListDTO> getPetQuestionnaires(int petId) throws ServiceExecutionException;

	/* ------------ PointTracker Lookup Services Start ---------------- */
	List<PointTrackerActivity> getPointTrackerActivities() throws ServiceExecutionException;

	List<PointTrackerMetric> getPetBehaviors(int speciesId, int behaviorTypeId) throws ServiceExecutionException;
	
	List<PointTrackerMetric> getPointTrackerMetrics(int petId) throws ServiceExecutionException;

	List<PointTrackerMetric> getPointTrackerMetricsById(int trackerPetPointsId) throws ServiceExecutionException;

	List<PointTrackerRejectReason> getTrackerRejectReasons() throws ServiceExecutionException;

	List<DeviceLocation> getDeviceLocations() throws ServiceExecutionException;

	List<DeviceLocation> getDeviceLocationsBulkUpload() throws ServiceExecutionException;

	/*
	 * ------------ Asset Management Status Lookup Services Start ----------------
	 */
	List<DeviceStatus> getDeviceStatuses() throws ServiceExecutionException;

	/* ------------ Asset Management Status Lookup Services End ---------------- */

	/*
	 * ------------ Mobile App Feedback Pages Lookup Services Start ----------------
	 */
	List<MobileAppFeedbackPage> getMobileAppFeedbackPages() throws ServiceExecutionException;

	List<MobileAppFBPhoneModel> getMobileAppFeedbackPhoneModels() throws ServiceExecutionException;
	/*
	 * ------------ Mobile App Feedback Pages Lookup Services End ----------------
	 */

	List<StudyNameFilter> getAllStudyName() throws ServiceExecutionException;

	List<StudyNameFilter> getAllActiveStudyName() throws ServiceExecutionException;

	List<PetSpecies> getPetSpecies() throws ServiceExecutionException;

	List<PetParentNameTimer> getPetParentNameTimer() throws ServiceExecutionException;

	List<PetNameTimer> getPetNameTimer() throws ServiceExecutionException;

	List<CategoryTimer> getCategoryNameTimer() throws ServiceExecutionException;

	List<PreludeStudy> getPreludeStudyName() throws ServiceExecutionException;

	List<ExtractFileCategory> getExtractFileCategory() throws ServiceExecutionException;

	List<PointTracker> getCampaignList() throws ServiceExecutionException;

	List<MaterialType> getMaterialTypeList() throws ServiceExecutionException;

	List<MaterialCategory> getMaterialCategoryList() throws ServiceExecutionException;

	List<Occurance> getQuestionnaireOccurances() throws ServiceExecutionException;

	List<Frequency> getQuestionnaireFrequencies() throws ServiceExecutionException;

	List<PushNotification> getStudyPushNotifications(String searchText) throws ServiceExecutionException;

	List<EatingEnthusiasmScale> getPetEatingEnthusiasmScales() throws ServiceExecutionException;

	List<PetFeedingTime> getPetFeedingTimes() throws ServiceExecutionException;

	List<ImageScoringType> getImageScoringTypes() throws ServiceExecutionException;

	List<ShipmentCompany> getShipmentCompanies() throws ServiceExecutionException;

	List<QuestionnaireType> getQuestionnaireTypes() throws ServiceExecutionException;

	List<QuestionnaireCategory> getQuestionnaireCategory(Integer questionnaireTypeId) throws ServiceExecutionException;

}
