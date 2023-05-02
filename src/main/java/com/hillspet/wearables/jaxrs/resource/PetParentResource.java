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
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.PetParentRequest;
import com.hillspet.wearables.response.PetParentListResponse;
import com.hillspet.wearables.response.PetParentResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/petParents")
@Api(value = "RESTful service that performs pet parent related operations", tags = { "Pet Parent Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface PetParentResource {

	@POST
	@ApiOperation(value = "Add Pet Parent", notes = "Add a pet parent")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response addPetParent(
			@Valid @ApiParam(name = "addPetparentRequest", required = true) PetParentRequest petParentRequest);

	@PUT
	@ApiOperation(value = "Update Pet Parent", notes = "Update a Pet Parent")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response updatePetParent(
			@Valid @ApiParam(name = "updatePetparentRequest", required = true) PetParentRequest petParentRequest);

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Get the Pet Parent details by Id", notes = "Get the Pet Parent details by Id")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetParentById(@PathParam("id") int petParentId);

	@GET
	@ApiOperation(value = "Get Pet Parent", notes = "Get the Pet Parent list")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetParentList(@BeanParam BaseFilter filter);

	@GET
	@Path("/getPetParents")
	@ApiOperation(value = "Get the Pet Parents", notes = "Get the Pet Parents")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PetParentListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPetParents();

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete Pet Parent", notes = "Deletes a Pet Parent in the application")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CommonResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response deletePetParent(@PathParam("id") int petParentId);

}
