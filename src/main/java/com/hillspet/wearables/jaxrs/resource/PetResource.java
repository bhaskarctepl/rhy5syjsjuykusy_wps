package com.hillspet.wearables.jaxrs.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.Message;
import com.hillspet.wearables.dto.PetNote;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.ExternalPetInfoFilter;
import com.hillspet.wearables.dto.filter.PetActivityFactorFilter;
import com.hillspet.wearables.dto.filter.PetEnthusiasmFilter;
import com.hillspet.wearables.dto.filter.PetFilter;
import com.hillspet.wearables.dto.filter.PetImageScaleFilter;
import com.hillspet.wearables.dto.filter.PetObservationMediaFilter;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.AssociateNewStudyRequest;
import com.hillspet.wearables.request.DownloadMediaRequest;
import com.hillspet.wearables.request.PetRequest;
import com.hillspet.wearables.response.ActivityFactorResultResponseList;
import com.hillspet.wearables.response.PetDevicesResponse;
import com.hillspet.wearables.response.PetFeedingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.PetImageScaleResponse;
import com.hillspet.wearables.response.PetNotesResponse;
import com.hillspet.wearables.response.PetObservationMediaListResponse;
import com.hillspet.wearables.response.PetObservationsResponse;
import com.hillspet.wearables.response.PetParentsResponse;
import com.hillspet.wearables.response.PetRedemptionHistoryResponse;
import com.hillspet.wearables.response.PetResponse;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.response.TotalAssetsByStatusReportResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class providing Pet details.
 * 
 * @author vvodyaram
 * @since w1.0
 * @version w1.0
 * @version Dec 7, 2020
 */
@Path("/pets")
@Api(value = "RESTful service that performs pets related operations", tags = { "Pet Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface PetResource {

	@GET
	@Path("/getPets")
	@ApiOperation(value = "Get All the Pets", notes = "Get the Pets list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPets();

	@GET
	@ApiOperation(value = "Get Pets", notes = "Get the Pets list based on filters")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetList(@BeanParam PetFilter filter);

	@POST
	@ApiOperation(value = "Add Pet", notes = "Creates a Pet in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response addPet(@Valid @ApiParam(name = "addPetRequest", required = true) PetRequest petRequest);

	@PUT
	@ApiOperation(value = "Update Pet", notes = "Updates a Pet in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updatePet(@Valid @ApiParam(name = "updatePetRequest", required = true) PetRequest petRequest);

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the Pet Info Component")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetById(@PathParam("id") int petId);

	@GET
	@Path("/{petId}/{studyId}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetByIdAndStudyId(@PathParam("petId") int petId, @PathParam("studyId") int studyId);

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete Pet", notes = "Deletes a Pet in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response deletePet(@PathParam("id") int petId);

	@GET
	@Path("/getPetsByPetParent/{petParentId}")
	@ApiOperation(value = "Get Pets", notes = "Get Pets by Pet Parent in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetsByPetParent(@PathParam("petParentId") int petParentId);

	@GET
	@Path("/getPetObservations")
	@ApiOperation(value = "Get Pets", notes = "Get Pet Observations of a Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetObservationsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetObservations(@BeanParam PetObservationMediaFilter filter);

	@GET
	@Path("/{petId}/getPetParents")
	@ApiOperation(value = "Get Pets", notes = "Get Pet Parents of a Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetParents(@BeanParam PetFilter filter);

	@GET
	@Path("/{petId}/getPetNotes")
	@ApiOperation(value = "Get Pet Notes", notes = "Get Pet Notes of a Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetNotesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetNotes(@BeanParam PetFilter filter);

	@GET
	@Path("/{petId}/getPetDevices")
	@ApiOperation(value = "Get Pet Devices", notes = "Get Devices of a Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetDevicesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetDevices(@PathParam("petId") int petId);

	@POST
	@Path("/{petId}/addPetNote")
	@ApiOperation(value = "Add a Pet Note", notes = "Add notes to Pet in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response addPetNote(@Valid @ApiParam(name = "addPetNoteRequest", required = true) PetNote petNote,
			@PathParam("petId") int petId);

	@GET
	@Path("/getPetObservationMediaList")
	@ApiOperation(value = "Get ObservationMediaList", notes = "Get Pet Observation Media list of a Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetObservationMediaListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetObservationMediaList(@BeanParam PetObservationMediaFilter filter);

	@GET
	@Path("/getPetObservationMedia/{id}")
	@ApiOperation(value = "Get Pet ObservationMedia By Id", notes = "Get a pet Observation Media Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetObservationMediaListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetObservationMediaById(@PathParam("id") int petId);

	@GET
	@Path("/getPetViewPane")
	@ApiOperation(value = "Get Pet List for View Pane", notes = "Get Pet View Pane based")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetViewPane(@BeanParam PetFilter filter);

	@PUT
	@Path("/disassociateStudy/{petId}/{studyId}")
	@ApiOperation(value = "Disassociate Study with Pet", notes = "Disassociate Study with Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response disassociateStudy(@PathParam("petId") int petId, @PathParam("studyId") int studyId);

	/**
	 * Downloads the selected images/videos in zip format.
	 * 
	 * @author rmaram
	 * @param mediaUrl
	 * @return
	 */
	@POST
	@Path("/downloadPetMedia1")
	@ApiOperation(value = "Get download Media", notes = "To download Media")
	@Produces(MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response downloadPetMedia(
			@Valid @ApiParam(name = "downloadMediaRequest", required = true) DownloadMediaRequest downloadMediaRequest)
			throws IOException;

	@POST
	@Path("/downloadPetMedia")
	@ApiOperation(value = "Get Pet List for View Pane", notes = "Get Pet View Pane based")
	@Produces(MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public void downloadPetMedia(
			@Valid @ApiParam(name = "downloadMediaRequest", required = true) DownloadMediaRequest downloadMediaRequest,
			@Context HttpServletResponse response) throws IOException;

	/**
	 * #WPTL-593
	 * 
	 * @author radepu
	 * @param petId
	 * @return
	 */
	@GET
	@Path("/weightHistory/{petId}")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetsResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetWeightHistory(@BeanParam BaseFilter filter, @PathParam("petId") int petId,
			@QueryParam("fromDate") String fromDate, @QueryParam("toDate") String toDate) throws IOException;

	/* This method using for View Pet */
	@GET
	@Path("/getCurrentStudies/{petId}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCurrentStudies(@PathParam("petId") int petId);

	/* This method using for View Pet */
	@GET
	@Path("/getArchiveStudies/{petId}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getArchiveStudies(@PathParam("petId") int petId);

	/* This method using for View Pet */
	@GET
	@Path("/getPetDevicesByStudy/{petId}/{studyId}")
	@ApiOperation(value = "Get Pet Devices", notes = "Get Devices of a Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetDevicesResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetDevicesByStudy(@PathParam("petId") int petId, @PathParam("studyId") int studyId,
			@BeanParam PetFilter filter);

	/* This method using for View Pet */
	@GET
	@Path("/getPetDetailsById/{petId}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetDetailsById(@PathParam("petId") int petId);

	/* This method using for View Pet - Campaign Points Widget */
	@GET
	@Path("/getPetCampaignPoints/{petId}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetCampaignPoints(@PathParam("petId") int petId);

	@GET
	@Path("/getPetCampaignPointsList/{petId}/{activityId}")
	@ApiOperation(value = "Get Pet By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetCampaignPointsList(@PathParam("petId") int petId, @PathParam("activityId") int activityId,
			@BeanParam BaseFilter filter);

	@GET
	@Path("/getPetRedemptionHistory/{petId}")
	@ApiOperation(value = "Get Pet Redemption History By Id", notes = "Get a Pet Details By Id in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetRedemptionHistoryResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetRedemptionHistory(@PathParam("petId") int petId);

	@PUT
	@Path("/redeemRewardPoints/{petId}/{redeemPoints}")
	@ApiOperation(value = "Disassociate Study with Pet", notes = "Disassociate Study with Pet")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response redeemRewardPoints(@PathParam("petId") int petId, @PathParam("redeemPoints") int redeemPoints);

	@GET
	@Path("/getExternalPetInfoList/{studyId}")
	@ApiOperation(value = "Get Pet Location", notes = "Get a Pet Location")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getExternalPetInfoList(@PathParam("studyId") int studyId);

	@GET
	@Path("/getExternalPetInfo")
	@ApiOperation(value = "Get External Pet info", notes = "Get External Pet status")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TotalAssetsByStatusReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getExternalPetInfo(@BeanParam ExternalPetInfoFilter filter);

	@POST
	@Path("/associateNewStudy")
	@ApiOperation(value = "Add a Pet Note", notes = "Add notes to Pet in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response associateNewStudy(
			@Valid @ApiParam(name = "addPetRequest", required = true) AssociateNewStudyRequest petRequest);

	@POST
	@Path("/addWeight")
	@ApiOperation(value = "Add a Weight", notes = "Add notes to Pet in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response addWeight(@Valid @ApiParam(name = "addPetWeight", required = true) AddPetWeight addPetWeight);

	@PUT
	@Path("/updateWeight/{weightId}/{weight}/{unit}")
	@ApiOperation(value = "Updates the pet weight", notes = "Updates the pet weight based on the Weight Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updateWeight(@PathParam("weightId") int weightId, @PathParam("weight") String weight,
			@PathParam("unit") String unit);

	@GET
	@Path("/getPetFeedingEnthusiasmScaleDtls")
	@ApiOperation(value = "Get Pets eating enthusiasm scale and feeding time details", notes = "Get Pets eating enthusiasm scale and feeding time details based on filters")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetFeedingEnthusiasmScaleResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetFeedingEnthusiasmScaleDtls(@BeanParam PetEnthusiasmFilter filter);

	@GET
	@Path("/getPetImageScaleDtls")
	@ApiOperation(value = "Get Pets image scale scoring details", notes = "Get Pets Pets image scale scoring details based on filters")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetImageScaleResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetImageScaleDtls(@BeanParam PetImageScaleFilter filter);
	
	@GET
	@Path("/getPetActivityFactorResult")
	@ApiOperation(value = "Get Pets activity factor results", notes = "Get Pets activity factor results list.")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = ActivityFactorResultResponseList.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetActivityFactorResult(@BeanParam PetActivityFactorFilter filter);
	

}
