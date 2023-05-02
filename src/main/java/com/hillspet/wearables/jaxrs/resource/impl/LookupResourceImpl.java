package com.hillspet.wearables.jaxrs.resource.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.builders.JaxrsJsonResponseBuilder;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.dto.AgentAction;
import com.hillspet.wearables.dto.AssignedUser;
import com.hillspet.wearables.dto.CategoryTimer;
import com.hillspet.wearables.dto.ContactMethod;
import com.hillspet.wearables.dto.CustomerContactMethod;
import com.hillspet.wearables.dto.CustomerContactReason;
import com.hillspet.wearables.dto.DefectiveSensorAction;
import com.hillspet.wearables.dto.DeviceLocation;
import com.hillspet.wearables.dto.DeviceStatus;
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
import com.hillspet.wearables.dto.QuestionType;
import com.hillspet.wearables.dto.QuestionnaireCategory;
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
import com.hillspet.wearables.jaxrs.resource.LookupResource;
import com.hillspet.wearables.response.AgentActionResponse;
import com.hillspet.wearables.response.AssignedUserResponse;
import com.hillspet.wearables.response.CategoryTimerResponse;
import com.hillspet.wearables.response.ContactMethodResponse;
import com.hillspet.wearables.response.CustomerContactMethodResponse;
import com.hillspet.wearables.response.CustomerContactReasonResponse;
import com.hillspet.wearables.response.DefectiveSensorActionResponse;
import com.hillspet.wearables.response.DeviceLocationResponse;
import com.hillspet.wearables.response.DeviceStatusResponse;
import com.hillspet.wearables.response.EatingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.ExtractFileCategoryResponse;
import com.hillspet.wearables.response.FrequencyResponse;
import com.hillspet.wearables.response.ImageScoringTypeResponse;
import com.hillspet.wearables.response.InventoryStatusResponse;
import com.hillspet.wearables.response.IssueResponse;
import com.hillspet.wearables.response.MaterialCategoryResponse;
import com.hillspet.wearables.response.MaterialTypeResponse;
import com.hillspet.wearables.response.MenuActionResponse;
import com.hillspet.wearables.response.MenuResponse;
import com.hillspet.wearables.response.MobileAppConfigResponse;
import com.hillspet.wearables.response.MobileAppFBPhoneModelResponse;
import com.hillspet.wearables.response.MobileAppFeedbackPageResponse;
import com.hillspet.wearables.response.OccuranceResponse;
import com.hillspet.wearables.response.PetBreedResponse;
import com.hillspet.wearables.response.PetFeedingTimeResponse;
import com.hillspet.wearables.response.PetNameResponse;
import com.hillspet.wearables.response.PetNameTimerResponse;
import com.hillspet.wearables.response.PetParentNameResponse;
import com.hillspet.wearables.response.PetParentNameTimerResponse;
import com.hillspet.wearables.response.PetSpeciesResponse;
import com.hillspet.wearables.response.PetStatusResponse;
import com.hillspet.wearables.response.PointTrackerActivityResponse;
import com.hillspet.wearables.response.PointTrackerMetricResponse;
import com.hillspet.wearables.response.PointTrackerResponse;
import com.hillspet.wearables.response.PreDefinedInstructionsResponse;
import com.hillspet.wearables.response.PreDefinedQuestionsResponse;
import com.hillspet.wearables.response.PreludeStudyResponse;
import com.hillspet.wearables.response.PriorityResponse;
import com.hillspet.wearables.response.QuestionTypeResponse;
import com.hillspet.wearables.response.QuestionnaireCategoryListResponse;
import com.hillspet.wearables.response.QuestionnaireListResponse;
import com.hillspet.wearables.response.QuestionnaireTypeResponse;
import com.hillspet.wearables.response.RoleTypeResponse;
import com.hillspet.wearables.response.RolesResponse;
import com.hillspet.wearables.response.RootCauseResponse;
import com.hillspet.wearables.response.SensorLocationResponse;
import com.hillspet.wearables.response.SensorNameResponse;
import com.hillspet.wearables.response.ShipmentCompaniesResponse;
import com.hillspet.wearables.response.StatusResponse;
import com.hillspet.wearables.response.StudyNameFilterResponse;
import com.hillspet.wearables.response.StudyNameResponse;
import com.hillspet.wearables.response.TicketAssignedUserResponse;
import com.hillspet.wearables.response.TicketCategoryResponse;
import com.hillspet.wearables.response.TicketPriorityResponse;
import com.hillspet.wearables.response.TicketStatusResponse;
import com.hillspet.wearables.response.TicketTypeResponse;
import com.hillspet.wearables.response.TrackerRejectReasonResponse;
import com.hillspet.wearables.service.lookup.LookupService;

/**
 * @author vvodyaram
 *
 */
@Service
public class LookupResourceImpl implements LookupResource {

	private static final Logger LOGGER = LogManager.getLogger(LookupResourceImpl.class);

	@Autowired
	private LookupService lookupService;

	@Autowired
	private JaxrsJsonResponseBuilder responseBuilder;

	@Override
	public Response getRoleTypes() {
		List<RoleType> roleTypes = lookupService.getRoleTypes();
		RoleTypeResponse response = new RoleTypeResponse();
		response.setRoleTypes(roleTypes);
		SuccessResponse<RoleTypeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMenus() {
		List<Menu> menus = lookupService.getMenus();
		MenuResponse response = new MenuResponse();
		response.setMenus(menus);
		SuccessResponse<MenuResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMenuActions() {
		List<MenuAction> menuActions = lookupService.getMenuActions();
		MenuActionResponse response = new MenuActionResponse();
		response.setMenuActions(menuActions);
		SuccessResponse<MenuActionResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getRoles() {
		List<Role> roles = lookupService.getRoles();
		RolesResponse response = new RolesResponse();
		response.setRoles(roles);
		SuccessResponse<RolesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getActiveRoles() {
		List<Role> roles = lookupService.getActiveRoles();
		RolesResponse response = new RolesResponse();
		response.setRoles(roles);
		SuccessResponse<RolesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetBreeds() {
		List<PetBreed> breeds = lookupService.getPetBreeds();
		PetBreedResponse response = new PetBreedResponse();
		response.setBreeds(breeds);
		SuccessResponse<PetBreedResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetStatuses() {
		List<PetStatus> petStatuses = lookupService.getPetStatuses();
		PetStatusResponse response = new PetStatusResponse();
		response.setPetStatuses(petStatuses);
		SuccessResponse<PetStatusResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMobileAppConfigs() {
		List<MobileAppConfig> mobileAppConfigs = lookupService.getMobileAppConfigs();
		MobileAppConfigResponse response = new MobileAppConfigResponse();
		response.setMobileAppConfigs(mobileAppConfigs);
		SuccessResponse<MobileAppConfigResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/* ------------ Customer Support Lookup Services Start ---------------- */

	@Override
	public Response getTicketTypes() {
		List<TicketType> ticketTypes = lookupService.getTicketTypes();
		TicketTypeResponse response = new TicketTypeResponse();
		response.setTicketTypes(ticketTypes);
		SuccessResponse<TicketTypeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getTicketPriorities() {
		List<TicketPriority> ticketPriorities = lookupService.getTicketPriorities();
		TicketPriorityResponse response = new TicketPriorityResponse();
		response.setTicketPriorities(ticketPriorities);
		SuccessResponse<TicketPriorityResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getTicketStatus() {
		List<TicketStatus> ticketStatus = lookupService.getTicketStatus();
		TicketStatusResponse response = new TicketStatusResponse();
		response.setTicketStatus(ticketStatus);
		SuccessResponse<TicketStatusResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getCustomerContactMethods() {
		List<CustomerContactMethod> customerContactMethods = lookupService.getCustomerContactMethods();
		CustomerContactMethodResponse response = new CustomerContactMethodResponse();
		response.setCustomerContactMethods(customerContactMethods);
		SuccessResponse<CustomerContactMethodResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getCustomerContactReasons() {
		List<CustomerContactReason> customerContactReasons = lookupService.getCustomerContactReasons();
		CustomerContactReasonResponse response = new CustomerContactReasonResponse();
		response.setCustomerContactReasons(customerContactReasons);
		SuccessResponse<CustomerContactReasonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getTicketCategory() {
		List<TicketCategory> ticketCategories = lookupService.getTicketCategory();
		TicketCategoryResponse response = new TicketCategoryResponse();
		response.setTicketCategories(ticketCategories);
		SuccessResponse<TicketCategoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getIssues() {
		List<Issue> issuesList = lookupService.getIssues();
		IssueResponse response = new IssueResponse();
		response.setIssueList(issuesList);
		SuccessResponse<IssueResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getRootCause(Integer issueId) {
		List<RootCause> rootCauseList = lookupService.getRootCause(issueId);
		RootCauseResponse response = new RootCauseResponse();
		response.setRootCauseList(rootCauseList);
		SuccessResponse<RootCauseResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAgentAction(Integer typeId) {
		List<AgentAction> agentActionList = lookupService.getAgentAction(typeId);
		AgentActionResponse response = new AgentActionResponse();
		response.setAgentActionList(agentActionList);
		SuccessResponse<AgentActionResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getDefectiveSensorAction() {
		List<DefectiveSensorAction> defectiveSensorActionList = lookupService.getDefectiveSensorAction();
		DefectiveSensorActionResponse response = new DefectiveSensorActionResponse();
		response.setDefectiveSensorActionList(defectiveSensorActionList);
		SuccessResponse<DefectiveSensorActionResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetName() {
		List<PetName> petNameList = lookupService.getPetName();
		PetNameResponse response = new PetNameResponse();
		response.setPetNameList(petNameList);
		SuccessResponse<PetNameResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParentName(Integer petId) {
		List<PetParentName> petNameList = lookupService.getPetParentName(petId);
		PetParentNameResponse response = new PetParentNameResponse();
		response.setPetNameList(petNameList);
		SuccessResponse<PetParentNameResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudyName(Integer petId) {
		List<StudyName> studyNameList = lookupService.getStudyName(petId);
		StudyNameResponse response = new StudyNameResponse();
		response.setStudyNameList(studyNameList);
		SuccessResponse<StudyNameResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getActiveStudy() {
		List<StudyName> studyNameList = lookupService.getActiveStudy();
		StudyNameResponse response = new StudyNameResponse();
		response.setStudyNameList(studyNameList);
		SuccessResponse<StudyNameResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getSensorName(Integer studyId, Integer petId) {
		List<SensorName> sensorNameList = lookupService.getSensorName(studyId, petId);
		SensorNameResponse response = new SensorNameResponse();
		response.setSensorNameList(sensorNameList);
		SuccessResponse<SensorNameResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getSensorLocation() {
		List<SensorLocation> sensorLocationList = lookupService.getSensorLocation();
		SensorLocationResponse response = new SensorLocationResponse();
		response.setSensorLocationList(sensorLocationList);
		SuccessResponse<SensorLocationResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getInventoryStatus(Integer deviceId) {
		List<InventoryStatus> inventoryStatusList = lookupService.getInventoryStatus(deviceId);
		InventoryStatusResponse response = new InventoryStatusResponse();
		response.setInventoryStatusList(inventoryStatusList);
		SuccessResponse<InventoryStatusResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPriority() {
		List<Priority> priorityList = lookupService.getPriority();
		PriorityResponse response = new PriorityResponse();
		response.setPriorityList(priorityList);
		SuccessResponse<PriorityResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAssignedTo() {
		List<AssignedUser> assignedUserList = lookupService.getAssignedTo();
		AssignedUserResponse response = new AssignedUserResponse();
		response.setAssignedUserList(assignedUserList);
		SuccessResponse<AssignedUserResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStatus() {
		List<Status> statusList = lookupService.getStatus();
		StatusResponse response = new StatusResponse();
		response.setStatusList(statusList);
		SuccessResponse<StatusResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getContactMethod() {
		List<ContactMethod> contactMethodList = lookupService.getContactMethod();
		ContactMethodResponse response = new ContactMethodResponse();
		response.setContactMethodList(contactMethodList);
		SuccessResponse<ContactMethodResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getDeviceType() {
		List<String> deviceTypeList = lookupService.getDeviceType();
		SuccessResponse<List<String>> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(deviceTypeList);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getDeviceModel(String deviceType) {
		List<String> deviceModelList = lookupService.getDeviceModel(deviceType);
		SuccessResponse<List<String>> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(deviceModelList);
		return responseBuilder.buildResponse(successResponse);
	}

	/* ------------ Customer Support Lookup Services End ---------------- */
	/* ------------ Questionnaire Lookup Services Start ---------------- */
	@Override
	public Response getQuestionType() {
		List<QuestionType> questionsTypes = lookupService.getQuestionType();
		QuestionTypeResponse response = new QuestionTypeResponse();
		response.setQuestionTypes(questionsTypes);
		SuccessResponse<QuestionTypeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPreDefinedInstructions() {
		List<PreDefinedInstruction> predefinedInstructions = lookupService.getPreDefinedInstructions();
		PreDefinedInstructionsResponse response = new PreDefinedInstructionsResponse();
		response.setPreDefinedInstructions(predefinedInstructions);
		SuccessResponse<PreDefinedInstructionsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPreDefinedQuestions(String searchText) {
		List<PreDefinedQuestion> preDefinedQuestions = lookupService.getPreDefinedQuestions(searchText);
		PreDefinedQuestionsResponse response = new PreDefinedQuestionsResponse();
		response.setPreDefinedQuestions(preDefinedQuestions);
		SuccessResponse<PreDefinedQuestionsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaires(String searchText) {
		QuestionnaireListResponse response = lookupService.getQuestionnaires(searchText);
		SuccessResponse<QuestionnaireListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetQuestionnaires(int petId) {
		QuestionnaireListResponse response = lookupService.getPetQuestionnaires(petId);
		SuccessResponse<QuestionnaireListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/* ------------ Questionnaire Lookup Services End ---------------- */

	/* ------------ PointTracker Lookup Services Start ---------------- */
	@Override
	public Response getPointTrackerActivities() {
		List<PointTrackerActivity> pointTrackerActivities = lookupService.getPointTrackerActivities();
		PointTrackerActivityResponse response = new PointTrackerActivityResponse();
		response.setPointTrackerActivities(pointTrackerActivities);
		SuccessResponse<PointTrackerActivityResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}
	
	@Override
	public Response getPetBehaviors(int speciesId, int behaviorTypeId) {		
		List<PointTrackerMetric> pointTrackerMetrics = lookupService.getPetBehaviors(speciesId, behaviorTypeId);
		PointTrackerMetricResponse response = new PointTrackerMetricResponse();
		response.setPointTrackerMetrics(pointTrackerMetrics);
		SuccessResponse<PointTrackerMetricResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPointTrackerMetrics(int petId) {
		List<PointTrackerMetric> pointTrackerMetrics = lookupService.getPointTrackerMetrics(petId);
		PointTrackerMetricResponse response = new PointTrackerMetricResponse();
		response.setPointTrackerMetrics(pointTrackerMetrics);
		SuccessResponse<PointTrackerMetricResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPointTrackerMetricsById(int trackerPetPointsId) {
		List<PointTrackerMetric> pointTrackerMetrics = lookupService.getPointTrackerMetricsById(trackerPetPointsId);
		PointTrackerMetricResponse response = new PointTrackerMetricResponse();
		response.setPointTrackerMetrics(pointTrackerMetrics);
		SuccessResponse<PointTrackerMetricResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);

	}

	@Override
	public Response getTrackerRejectReasons() {
		List<PointTrackerRejectReason> trackerRejectReasonList = lookupService.getTrackerRejectReasons();
		TrackerRejectReasonResponse response = new TrackerRejectReasonResponse();
		response.setTrackerRejectReasons(trackerRejectReasonList);
		SuccessResponse<TrackerRejectReasonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}
	/* ------------ PointTracker Lookup Services End ---------------- */

	@Override
	public Response getDeviceLocations() {
		List<DeviceLocation> deviceLocations = lookupService.getDeviceLocations();
		DeviceLocationResponse response = new DeviceLocationResponse();
		response.setDeviceLocations(deviceLocations);
		SuccessResponse<DeviceLocationResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/*
	 * ------------ Asset Management Status Lookup Services Start ----------------
	 */
	@Override
	public Response getDeviceStatuses() {
		List<DeviceStatus> deviceStatuses = lookupService.getDeviceStatuses();
		DeviceStatusResponse response = new DeviceStatusResponse();
		response.setDeviceStatuses(deviceStatuses);
		;
		SuccessResponse<DeviceStatusResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/* ------------ Asset Management Status Lookup Services End ---------------- */

	/*
	 * ------------ Mobile App Feedback Pages Lookup Services Start ----------------
	 */
	@Override
	public Response getMobileAppFeedbackPages() {
		List<MobileAppFeedbackPage> mobileAppFeedbackPages = lookupService.getMobileAppFeedbackPages();
		MobileAppFeedbackPageResponse response = new MobileAppFeedbackPageResponse();
		response.setMobileAppFeedbackPages(mobileAppFeedbackPages);
		SuccessResponse<MobileAppFeedbackPageResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMobileAppPhoneModels() {
		List<MobileAppFBPhoneModel> mobileAppFeedbackPages = lookupService.getMobileAppPhoneModels();
		MobileAppFBPhoneModelResponse response = new MobileAppFBPhoneModelResponse();
		response.setMobileAppFBPhoneModel(mobileAppFeedbackPages);
		SuccessResponse<MobileAppFBPhoneModelResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	/*
	 * ------------ Mobile App Feedback Pages Lookup Services End ----------------
	 */
	@Override
	public Response getAllStudyName() {
		List<StudyNameFilter> studyNameList = lookupService.getAllStudyName();
		StudyNameFilterResponse response = new StudyNameFilterResponse();
		response.setStudyNameList(studyNameList);
		SuccessResponse<StudyNameFilterResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAllActiveStudyName() {
		List<StudyNameFilter> studyNameList = lookupService.getAllActiveStudyName();
		StudyNameFilterResponse response = new StudyNameFilterResponse();
		response.setStudyNameList(studyNameList);
		SuccessResponse<StudyNameFilterResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetSpecies() {
		List<PetSpecies> species = lookupService.getPetSpecies();
		PetSpeciesResponse response = new PetSpeciesResponse();
		response.setSpecies(species);
		SuccessResponse<PetSpeciesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getAssignedToUsers() {
		List<TicketAssignedUser> assignedUserList = lookupService.getAssignedToUsers();
		TicketAssignedUserResponse response = new TicketAssignedUserResponse();
		response.setTicketAssignedUserList(assignedUserList);
		SuccessResponse<TicketAssignedUserResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParentNameTimer() {
		List<PetParentNameTimer> petParentNameList = lookupService.getPetParentNameTimer();
		PetParentNameTimerResponse response = new PetParentNameTimerResponse();
		response.setPetParentNameList(petParentNameList);
		SuccessResponse<PetParentNameTimerResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetNameTimer() {
		List<PetNameTimer> petNameList = lookupService.getPetNameTimer();
		PetNameTimerResponse response = new PetNameTimerResponse();
		response.setPetNameList(petNameList);
		SuccessResponse<PetNameTimerResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getCategoryNameTimer() {
		List<CategoryTimer> categoryList = lookupService.getCategoryNameTimer();
		CategoryTimerResponse response = new CategoryTimerResponse();
		response.setCategoryList(categoryList);
		SuccessResponse<CategoryTimerResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPreludeStudyName() {
		List<PreludeStudy> preludeStudyList = lookupService.getPreludeStudyName();
		PreludeStudyResponse response = new PreludeStudyResponse();
		response.setPreludeStudyList(preludeStudyList);
		SuccessResponse<PreludeStudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getExtractFileCategory() {
		List<ExtractFileCategory> extractFileCategoryList = lookupService.getExtractFileCategory();
		ExtractFileCategoryResponse response = new ExtractFileCategoryResponse();
		response.setExtractFileCategoryList(extractFileCategoryList);
		SuccessResponse<ExtractFileCategoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getCampaignList() {
		List<PointTracker> pointTrackers = lookupService.getCampaignList();
		PointTrackerResponse response = new PointTrackerResponse();
		response.setCampaigns(pointTrackers);
		SuccessResponse<PointTrackerResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaireOccurances() {
		List<Occurance> occurances = lookupService.getQuestionnaireOccurances();
		OccuranceResponse response = new OccuranceResponse();
		response.setOccurances(occurances);
		SuccessResponse<OccuranceResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaireFrequencies() {
		List<Frequency> frequencies = lookupService.getQuestionnaireFrequencies();
		FrequencyResponse response = new FrequencyResponse();
		response.setFrequencies(frequencies);
		SuccessResponse<FrequencyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getStudyPushNotifications(String searchText) {
		PushNotificationListResponse response = lookupService.getStudyPushNotifications(searchText);
		SuccessResponse<PushNotificationListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMaterialTypeList() {
		List<MaterialType> mList = lookupService.getMaterialTypeList();
		MaterialTypeResponse response = new MaterialTypeResponse();
		response.setMaterialTypes(mList);
		SuccessResponse<MaterialTypeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getMaterialCategoryList() {
		List<MaterialCategory> cList = lookupService.getMaterialCategoryList();
		MaterialCategoryResponse response = new MaterialCategoryResponse();
		response.setMaterialCategories(cList);
		SuccessResponse<MaterialCategoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetEatingEnthusiasmScales() {
		EatingEnthusiasmScaleResponse eatingEnthusiasmScaleResponse = lookupService.getPetEatingEnthusiasmScales();
		SuccessResponse<EatingEnthusiasmScaleResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(eatingEnthusiasmScaleResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetFeedingTimes() {
		PetFeedingTimeResponse petFeedingTimeResponse = lookupService.getPetFeedingTimes();
		SuccessResponse<PetFeedingTimeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petFeedingTimeResponse);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getImageScoringTypes() {
		List<ImageScoringType> imageScoringTypes = lookupService.getImageScoringTypes();
		ImageScoringTypeResponse response = new ImageScoringTypeResponse();
		response.setImageScoringTypes(imageScoringTypes);
		SuccessResponse<ImageScoringTypeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getShipmentCompanies() {
		LOGGER.info("In LookupResourceImpl, getShipmentCompanies method");
		List<ShipmentCompany> shipmentCompanies = lookupService.getShipmentCompanies();
		ShipmentCompaniesResponse response = new ShipmentCompaniesResponse();
		response.setShipmentCompanies(shipmentCompanies);
		SuccessResponse<ShipmentCompaniesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaireTypes() {
		LOGGER.info("In LookupResourceImpl, getQuestionnaireTypes method");
		List<QuestionnaireType> questionnaireTypes = lookupService.getQuestionnaireTypes();
		QuestionnaireTypeResponse response = new QuestionnaireTypeResponse();
		response.setQuestionnaireTypes(questionnaireTypes);
		SuccessResponse<QuestionnaireTypeResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getQuestionnaireCategoryList(Integer questionnaireTypeId) {
		LOGGER.info("In LookupResourceImpl, getQuestionnaireCategoryList method");
		List<QuestionnaireCategory> questionnaireCategories = lookupService.getQuestionnaireCategory(questionnaireTypeId);
		QuestionnaireCategoryListResponse response = new QuestionnaireCategoryListResponse();
		response.setQuestionnaireCategories(questionnaireCategories);
		SuccessResponse<QuestionnaireCategoryListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}
}
