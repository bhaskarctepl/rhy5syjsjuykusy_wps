package com.hillspet.wearables.jaxrs.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.Message;
import com.hillspet.wearables.jaxrs.resource.impl.PushNotificationListResponse;
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
import com.hillspet.wearables.response.PetStatusResponse;
import com.hillspet.wearables.response.PointTrackerActivityResponse;
import com.hillspet.wearables.response.PointTrackerMetricResponse;
import com.hillspet.wearables.response.PointTrackerResponse;
import com.hillspet.wearables.response.PreDefinedInstructionsResponse;
import com.hillspet.wearables.response.PreDefinedQuestionsResponse;
import com.hillspet.wearables.response.PreludeStudyResponse;
import com.hillspet.wearables.response.PriorityResponse;
import com.hillspet.wearables.response.QuestionTypeResponse;
import com.hillspet.wearables.response.QuestionnaireListResponse;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/lookup")
@Api(value = "RESTful service that used to get all lookup related data", tags = { "Lookup Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface LookupResource {

	@GET
	@Path("/getRoleTypes")
	@ApiOperation(value = "Get Role Type List Lookup", notes = "Gets the Role Types List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = RoleTypeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getRoleTypes();

	@GET
	@Path("/getMenus")
	@ApiOperation(value = "Get Menu List Lookup", notes = "Gets the Menu List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MenuResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMenus();

	@GET
	@Path("/getMenuActions")
	@ApiOperation(value = "Get Menu Action List Lookup", notes = "Gets the Menu Action List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MenuActionResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMenuActions();

	@GET
	@Path("/getRoles")
	@ApiOperation(value = "Get Roles List Lookup", notes = "Gets the Roles List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = RolesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getRoles();

	@GET
	@Path("/getActiveRoles")
	@ApiOperation(value = "Get Active Roles List Lookup", notes = "Gets the Roles List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = RolesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getActiveRoles();

	@GET
	@Path("/getPetBreeds")
	@ApiOperation(value = "Get Pet Breed List Lookup", notes = "Gets the Pet Breed List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetBreedResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetBreeds();

	@GET
	@Path("/getPetStatuses")
	@ApiOperation(value = "Get Pet Status List Lookup", notes = "Gets the Pet Status List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetStatusResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetStatuses();

	@GET
	@Path("/getMobileAppConfigs")
	@ApiOperation(value = "Get Mobile App Config List Lookup", notes = "Gets the Mobile App Config List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MobileAppConfigResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMobileAppConfigs();

	/* ------------ Customer Support Lookup Services Start ---------------- */

	@GET
	@Path("/getTicketTypes")
	@ApiOperation(value = "Get Ticket Type List Lookup", notes = "Gets the Ticket Type List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketTypeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTicketTypes();

	@GET
	@Path("/getTicketPriorities")
	@ApiOperation(value = "Get Ticket Priority List Lookup", notes = "Gets the Ticket Priority List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketPriorityResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTicketPriorities();

	@GET
	@Path("/getTicketStatus")
	@ApiOperation(value = "Get Ticket Status List Lookup", notes = "Gets the Ticket Status List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketStatusResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTicketStatus();

	@GET
	@Path("/getCustomerContactMethods")
	@ApiOperation(value = "Get Customer Contact Method List Lookup", notes = "Gets the Customer Contact Method List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CustomerContactMethodResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCustomerContactMethods();

	@GET
	@Path("/getCustomerContactReasons")
	@ApiOperation(value = "Get Customer Contact Reason List Lookup", notes = "Gets the Customer Contact Reason List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CustomerContactReasonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCustomerContactReasons();

	// depracated, use getIssueId
	@GET
	@Path("/getTicketCategory")
	@ApiOperation(value = "Get Ticket Category List Lookup", notes = "Gets the Ticket Category List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketCategoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTicketCategory();

	@GET
	@Path("/getIssue")
	@ApiOperation(value = "Get Issues List Lookup", notes = "Gets the Issues List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = IssueResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getIssues();

	@GET
	@Path("/getRootCause/{issueId}")
	@ApiOperation(value = "Get Root Cause List Lookup", notes = "Gets the Root Cause List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = RootCauseResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getRootCause(@PathParam("issueId") Integer issueId);

	@GET
	@Path("/getAgentAction/{typeId}")
	@ApiOperation(value = "Get Agent/System Action - Initial/Secondary/Tertiary List Lookup", notes = "Gets the Agent/System Action - Initial(1)/Secondary(2)/Tertiary(3) List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = AgentActionResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAgentAction(@PathParam("typeId") Integer typeId);

	@GET
	@Path("/getDefectiveSensorAction")
	@ApiOperation(value = "Get Defective Sensor Action List Lookup", notes = "Gets the Defective Sensor Action List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DefectiveSensorActionResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDefectiveSensorAction();

	@GET
	@Path("/getPetName")
	@ApiOperation(value = "Get Pet Name List Lookup", notes = "Gets the Pet Name List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetNameResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetName();

	@GET
	@Path("/getPetParentName/{petId}")
	@ApiOperation(value = "Get Pet Parent Name List Lookup", notes = "Gets all Pet Parent Name if pet not selected List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentNameResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetParentName(@PathParam("petId") Integer petId);

	@GET
	@Path("/getStudyName/{petId}")
	@ApiOperation(value = "Get Study Name List Lookup", notes = "Gets all study Name if pet not selected List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyNameResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyName(@PathParam("petId") Integer petId);

	@GET
	@Path("/getActiveStudy")
	@ApiOperation(value = "Get Active Study Name List Lookup", notes = "Gets active study Name List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyNameResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getActiveStudy();

	@GET
	@Path("/getSensorName/{studyId}/{petId}")
	@ApiOperation(value = "Get Sensor Name List Lookup", notes = "Gets the Sensor Name List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SensorNameResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getSensorName(@PathParam("studyId") Integer studyId, @PathParam("petId") Integer petId);

	@GET
	@Path("/getSensorLocation")
	@ApiOperation(value = "Get Sensor Location List Lookup", notes = "Gets the Sensor Location List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = SensorLocationResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getSensorLocation();

	@GET
	@Path("/getInventoryStatus/{deviceId}")
	@ApiOperation(value = "Get Inventory Status List Lookup", notes = "Gets all Inventory Status id device is not selected List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = InventoryStatusResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getInventoryStatus(@PathParam("deviceId") Integer deviceId);

	@GET
	@Path("/getPriority")
	@ApiOperation(value = "Get Priority List Lookup", notes = "Gets the Priority List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PriorityResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPriority();

	@GET
	@Path("/getUsers")
	@ApiOperation(value = "Get Assigned to Lookup", notes = "Gets the Assigned to List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = AssignedUserResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAssignedTo();

	@GET
	@Path("/getStatus")
	@ApiOperation(value = "Get Status Lookup", notes = "Gets the status List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StatusResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStatus();

	@GET
	@Path("/getContactMethod")
	@ApiOperation(value = "Get Contact Method Lookup", notes = "Gets the Contact Method List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ContactMethodResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getContactMethod();

	@GET
	@Path("/getDeviceType")
	@ApiOperation(value = "Get Device Type Lookup", notes = "Gets the Device Type List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketCategoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceType();

	@GET
	@Path("/getDeviceModel/{deviceType}")
	@ApiOperation(value = "Get Device Model Lookup", notes = "Gets the Device Model List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketCategoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceModel(@PathParam("deviceType") String deviceType);

	/* ------------ Customer Support Lookup Services End ---------------- */
	/* ------------ Questionnaire Lookup Services Start ---------------- */
	@GET
	@Path("/getQuestionType")
	@ApiOperation(value = "Get Question Type List Lookup", notes = "Gets the Question Type List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionTypeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getQuestionType();

	@GET
	@Path("/getPreDefinedInstructions")
	@ApiOperation(value = "Get Pre Defined Instructions List Lookup", notes = "Gets the Pre Defined Instructions List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreDefinedInstructionsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPreDefinedInstructions();

	@GET
	@Path("/getPreDefinedQuestions")
	@ApiOperation(value = "Get Pre Defined Questions List Lookup", notes = "Gets the Pre Defined Questions List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreDefinedQuestionsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPreDefinedQuestions(@QueryParam("searchText") String searchText);

	@GET
	@Path("/getQuestionnaires")
	@ApiOperation(value = "Gets Questionnaires", notes = "Questionnaires")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionnaireListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getQuestionnaires(@QueryParam("searchText") String searchText);

	@GET
	@Path("/getPetQuestionnaires/{petId}")
	@ApiOperation(value = "Gets Pet Questionnaires", notes = "Pet Questionnaires")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionnaireListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetQuestionnaires(@PathParam("petId") int petId);

	/* ------------ Questionnaire Lookup Services End ---------------- */

	/* ------------ PointTracker Lookup Services Start ---------------- */
	@GET
	@Path("/getPointTrackerActivities")
	@ApiOperation(value = "Get Point Tracker Activities List Lookup", notes = "Gets the Point Tracker Activities  List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PointTrackerActivityResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPointTrackerActivities();

	/* ------------ PointTracker Lookup Services End ---------------- */

	@GET
	@Path("/getDeviceLocations")
	@ApiOperation(value = "Get Device Location List Lookup", notes = "Gets the Device Location List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceLocationResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceLocations();
	
	@GET
	@Path("/getPetBehaviors/{speciesId}")
	@ApiOperation(value = "Get Pet Behaviors List By species and behavior type", notes = "Get Pet Behaviors List By species and behavior type")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PointTrackerMetricResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetBehaviors(@PathParam("speciesId") int speciesId,	@QueryParam("behaviorTypeId") int behaviorTypeId);

	@GET
	@Path("/getPointTrackerMetrics")
	@ApiOperation(value = "Get Mobile App Feedback Pages List Lookup", notes = "Gets the  Mobile App Feedback Pages List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PointTrackerMetricResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPointTrackerMetrics(@QueryParam("petId") int petId);

	@GET
	@Path("/getPointTrackerMetrics/{id}")
	@ApiOperation(value = "Gets the point tracker reject reasons Lookup", notes = "Gets the point tracker reject reasons list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PointTrackerMetricResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPointTrackerMetricsById(@PathParam("id") int trackerPetPointsId);

	@GET
	@Path("/getTrackerRejectReasons")
	@ApiOperation(value = "Gets the point tracker reject reasons Lookup", notes = "Gets the point tracker reject reasons list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TrackerRejectReasonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTrackerRejectReasons();

	/*
	 * ------------ Asset Management Status Lookup Services Start ----------------
	 */
	@GET
	@Path("/getDeviceStatuses")
	@ApiOperation(value = "Get Device Status List Lookup", notes = "Gets the  Device Status List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceStatusResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceStatuses();
	/* ------------ Asset Management Status Lookup Services End ---------------- */

	/*
	 * ------------ Mobile App Feedback Pages Lookup Services Start ----------------
	 */
	@GET
	@Path("/getMobileAppFeedbackPages")
	@ApiOperation(value = "Get Mobile App Feedback Pages List Lookup", notes = "Gets the  Mobile App Feedback Pages List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MobileAppFeedbackPageResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMobileAppFeedbackPages();

	@GET
	@Path("/getMobileAppPhoneModels")
	@ApiOperation(value = "Get Mobile App Phone Models List Lookup", notes = "Gets Get Mobile App Phone Models List List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MobileAppFBPhoneModelResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMobileAppPhoneModels();
	/*
	 * ------------ Mobile App Feedback Pages Lookup Services End ----------------
	 */

	@GET
	@Path("/getAllStudyName")
	@ApiOperation(value = "Get Study Name List Lookup", notes = "Gets all study Name")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyNameFilterResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAllStudyName();

	@GET
	@Path("/getAllActiveStudyName")
	@ApiOperation(value = "Get Study Name List Lookup", notes = "Gets all study Name")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyNameFilterResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAllActiveStudyName();

	@GET
	@Path("/getPetSpecies")
	@ApiOperation(value = "Get Pet Speecies List Lookup", notes = "Gets the Pet Speecies List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetBreedResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetSpecies();

	@GET
	@Path("/getAssignedToUsers")
	@ApiOperation(value = "Get Assigned to Users Lookup", notes = "Gets the Assigned to Users List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TicketAssignedUserResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAssignedToUsers();

	@GET
	@Path("/getPetParentNameTimer")
	@ApiOperation(value = "Get Pet Parent Name Timer List Lookup", notes = "Gets the Pet Parent Name Timer List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentNameTimerResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetParentNameTimer();

	@GET
	@Path("/getPetNameTimer")
	@ApiOperation(value = "Get Pet Name Timer List Lookup", notes = "Gets the Pet Name Timer List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetNameTimerResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetNameTimer();

	@GET
	@Path("/getCategoryNameTimer")
	@ApiOperation(value = "Get Category Name Timer List Lookup", notes = "Gets the Category Name Timer List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CategoryTimerResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCategoryNameTimer();

	@GET
	@Path("/getPreludeStudyName")
	@ApiOperation(value = "Get Category Name Timer List Lookup", notes = "Gets the Prelude Study Name List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreludeStudyResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPreludeStudyName();

	@GET
	@Path("/getExtractFileCategory")
	@ApiOperation(value = "Get Extract File Category Name List Lookup", notes = "Gets the Extract File  Category Name List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ExtractFileCategoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getExtractFileCategory();

	@GET
	@Path("/getCampaignList")
	@ApiOperation(value = "Get Campaign List", notes = "Gets the Campaign List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PointTrackerResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCampaignList();

	@GET
	@Path("/getQuestionnaireOccurances")
	@ApiOperation(value = "Get Questionnaire Occurance List Lookup", notes = "Gets the Questionnaire Occurance List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = OccuranceResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getQuestionnaireOccurances();

	@GET
	@Path("/getQuestionnaireFrequencies")
	@ApiOperation(value = "Get Questionnaire Frequency List Lookup", notes = "Gets the Questionnaire Frequency List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = FrequencyResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getQuestionnaireFrequencies();

	@GET
	@Path("/getStudyPushNotifications")
	@ApiOperation(value = "Gets Study Push Notifications", notes = "Study Push Notifications")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PushNotificationListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyPushNotifications(@QueryParam("searchText") String searchText);

	@GET
	@Path("/getMaterialTypeList")
	@ApiOperation(value = "Get Campaign List", notes = "Gets the Campaign List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MaterialTypeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMaterialTypeList();

	@GET
	@Path("/getMaterialCategoryList")
	@ApiOperation(value = "Get Campaign List", notes = "Gets the Campaign List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = MaterialCategoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getMaterialCategoryList();

	@GET
	@Path("/getPetEatingEnthusiasmScales")
	@ApiOperation(value = "Get Pet Speecies List Lookup", notes = "Gets the Pet Eating Enthusiasm Scale List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = EatingEnthusiasmScaleResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetEatingEnthusiasmScales();

	@GET
	@Path("/getPetFeedingTimes")
	@ApiOperation(value = "Get Pet Speecies List Lookup", notes = "Gets the Pet Feeding Time List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetFeedingTimeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetFeedingTimes();

	@GET
	@Path("/getImageScoringTypes")
	@ApiOperation(value = "Get Pet Image Scoring Type List Lookup", notes = "Gets the Pet Image Scoring Type List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ImageScoringTypeResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getImageScoringTypes();
	
	@GET
	@Path("/getShipmentCompanies")
	@ApiOperation(value = "Get Shipment Company List Lookup", notes = "Gets the shipment companies List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ShipmentCompaniesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getShipmentCompanies();
	
	@GET
	@Path("/getQuestionnaireTypes")
	@ApiOperation(value = "Get Questionnaire Type List Lookup", notes = "Gets the Questionnaire Type List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ShipmentCompaniesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getQuestionnaireTypes();
	
	@GET
	@Path("/getQuestionnaireCategoryList/{questionnaireTypeId}")
	@ApiOperation(value = "Get Questionnaire Category List List Lookup by Type", notes = "Gets the Questionnaire Category List")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ShipmentCompaniesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getQuestionnaireCategoryList(@PathParam("questionnaireTypeId") Integer questionnaireTypeId);
}
