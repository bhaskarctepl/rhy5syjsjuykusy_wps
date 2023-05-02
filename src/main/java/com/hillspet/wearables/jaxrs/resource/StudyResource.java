package com.hillspet.wearables.jaxrs.resource;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.Message;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.StudyFilter;
import com.hillspet.wearables.jaxrs.resource.impl.PushNotificationListResponse;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.StudyNotificationRequest;
import com.hillspet.wearables.request.StudyRequest;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.response.PreludeListResponse;
import com.hillspet.wearables.response.QuestionnaireListResponse;
import com.hillspet.wearables.response.StudyImageScalesListResponse;
import com.hillspet.wearables.response.StudyListResponse;
import com.hillspet.wearables.response.StudyNotificationResponse;
import com.hillspet.wearables.response.StudyResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/study")
@Api(value = "RESTful service that performs study related operations", tags = { "Study Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface StudyResource {

	/* ---------------- STUDY SERVICES ------------------------- */

	@POST
	@ApiOperation(value = "Add Study", notes = "Add a Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response AddStudy(@Valid @ApiParam(name = "addStudyRequest", required = true) StudyRequest studyRequest);

	@PUT
	@ApiOperation(value = "Update Study", notes = "Update a Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updateStudy(
			@Valid @ApiParam(name = "updateStudyRequest", required = true) StudyRequest studyRequest);

	@PUT
	@Path("/associatePlan/{studyId}/{planId}/{subscriptionDate}")
	@ApiOperation(value = "Associate Plan to Study", notes = "Associate Plan to Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associatePlan(@PathParam("studyId") int studyId, @PathParam("planId") int planId,
			@PathParam("subscriptionDate") String subscriptionDate);

	@PUT
	@Path("/disassociatePlan/{studyId}/{planId}")
	@ApiOperation(value = "Disassociate Plan with Study", notes = "Disassociate  Plan with Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response disassociatePlan(@PathParam("studyId") int studyId, @PathParam("planId") int planId);

	@GET
	@Path("/associatedPets/{studyId}")
	@ApiOperation(value = "Get pets associated to a study", notes = "Get pets associated to a study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associatedPets(@PathParam("studyId") int studyId);

	@GET
	@ApiOperation(value = "Get Studies", notes = "Get the Study list based on Filters")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudies(@BeanParam StudyFilter filter);

	@GET
	@Path("/getStudyList")
	@ApiOperation(value = "Get all Studies", notes = "Get all the Study list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyList();

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Get the Study by Id", notes = "Get the Study details by Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyById(@PathParam("id") int studyId);

	@GET
	@Path("getStudies/{petParentId}/{petId}")
	@ApiOperation(value = "Get the Studies by Pet Parent Id and Pet Id", notes = "Get the Studies by Pet Parent Id and Pet Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudiesByPetParentAndPet(@PathParam("petParentId") int petParentId,
			@PathParam("petId") int petId);

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete Study", notes = "Deletes a Study in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response deleteStudy(@PathParam("id") int studyId);

	@PUT
	@Path("/associateQuestionnaire/{studyId}/{questionnaireId}/{startDate}/{endDate}")
	@ApiOperation(value = "Associate Questionnaire to Study", notes = "Associate Questionnaire to Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associateQuestionnaire(@PathParam("studyId") int studyId,
			@PathParam("questionnaireId") int questionnaireId, @PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate);
	
	@PUT
	@Path("/updateStudyQuestionnaire/{studyId}/{questionnaireId}/{startDate}/{endDate}")
	@ApiOperation(value = "Associate Plan to Study", notes = "Associate Questionnaire to Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updateStudyQuestionnaire(@PathParam("studyId") int studyId,
			@PathParam("questionnaireId") int questionnaireId, @PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate);

	@PUT
	@Path("/disassociateQuestionnaire/{studyId}/{questionnaireId}")
	@ApiOperation(value = "Disassociate Questionnaire with Study", notes = "Disassociate  Questionnaire with Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response disassociateQuestionnaire(@PathParam("studyId") int studyId,
			@PathParam("questionnaireId") int questionnaireId);

	@GET
	@Path("/associatedQuestionnaires/{studyId}")
	@ApiOperation(value = "Get questionnaires associated to a study", notes = "Get questionnaires associated to a study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = QuestionnaireListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associatedQuestionnaires(@PathParam("studyId") int studyId);

	/* ---------------- STUDY NOTIFICATIONS SERVICES ------------------------- */

	@GET
	@Path("/getStudyNotifications")
	@ApiOperation(value = "Get Study Notification", notes = "Get the Study Notification list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyNotificationResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyNotifications(@BeanParam BaseFilter filter);

	@PUT
	@Path("/updateStudyNotificationStatus")
	@ApiOperation(value = "Update Study Notification Status", notes = "Enabel/Disable Study Notification in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updateStudyNotificationStatus(
			@Valid @ApiParam(name = "updateStudyNotificationStatusReqeust", required = true) StudyNotificationRequest studyNotificationRequest);
	
	@GET
	@Path("/getStudyListByUser")
	@ApiOperation(value = "Get all Studies by user id", notes = "Get all the Study list By User")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyListByUser();
	
	@GET
	@Path("/getAllStudyList")
	@ApiOperation(value = "Get all Studies by user id", notes = "Get all the Study list By User")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAllStudyList();
	
	@GET
	@Path("/getPreludeDataList/{studyId}")
	@ApiOperation(value = "Get all Prelude data", notes = "Get all the Prelude data list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreludeListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPreludeDataList(@PathParam("studyId") int studyId);
	
	@GET
	@Path("/getPreludeDataByStudy/{studyName}")
	@ApiOperation(value = "Get Prelude Data By Study", notes = "Get all the Prelude Data By Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreludeListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPreludeDataByStudy(@PathParam("studyName") String studyName);
	
	@PUT
	@Path("/associatePushNotifications/{studyId}/{notificationId}/{startDate}/{endDate}")
	@ApiOperation(value = "Associate Notification to Study", notes = "Associate Notification to Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associatePushNotifications(@PathParam("studyId") int studyId,
			@PathParam("notificationId") int notificationId, @PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate);
	
	@PUT
	@Path("/updateStudyPushNotifications/{studyId}/{notificationId}/{startDate}/{endDate}")
	@ApiOperation(value = "Associate Notification to Study", notes = "Associate Notification to Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updateStudyPushNotifications(@PathParam("studyId") int studyId,
			@PathParam("notificationId") int notificationId, @PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate);

	@PUT
	@Path("/disassociatePushNotifications/{studyId}/{notificationId}")
	@ApiOperation(value = "Disassociate Notification with Study", notes = "Disassociate  Notification with Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response disassociatePushNotifications(@PathParam("studyId") int studyId,
			@PathParam("notificationId") int notificationId);

	@GET
	@Path("/associatedPushNotifications/{studyId}")
	@ApiOperation(value = "Get Notifications associated to a study", notes = "Get Notifications associated to a study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PushNotificationListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associatedPushNotifications(@PathParam("studyId") int studyId);
	
	@GET
	@Path("/associatedImageScales/{studyId}")
	@ApiOperation(value = "Get Image Scales associated to a study", notes = "Get Image Scales associated to a study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyImageScalesListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associatedImageScales(@PathParam("studyId") int studyId);
	
	@GET
	@Path("/getAFPreludeDataList/{studyId}")
	@ApiOperation(value = "Get all AF Prelude data", notes = "Get all the AF Prelude data list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreludeListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAFPreludeDataList(@PathParam("studyId") int studyId);
	
	@GET
	@Path("/getAFPreludeDataByStudy/{studyName}")
	@ApiOperation(value = "Get Prelude AF Data By Study", notes = "Get all the Prelude Data By Study")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PreludeListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAFPreludeDataByStudy(@PathParam("studyName") String studyName);
	
	/* @GET
	@Path("/loadPreludeData")
	@ApiOperation(value = "Load Prelude Data", notes = "Load Prelude Data")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response loadPreludeData(); */

}
