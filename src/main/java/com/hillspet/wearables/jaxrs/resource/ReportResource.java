package com.hillspet.wearables.jaxrs.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.hillspet.wearables.dto.AssetByStudyWidgetFilter;
import com.hillspet.wearables.dto.TotalAssetsByStausWidgetFilter;
import com.hillspet.wearables.dto.filter.*;
import com.hillspet.wearables.response.*;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.response.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author rmaram
 * @since w1.0
 * @version w1.0
 */
@Path("/reports")
@Api(value = "RESTful service that performs report related operations", tags = { "Report Management" })
@Produces({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
@Consumes({ MediaType.APPLICATION_JSON_VALUE, Constants.MEDIA_TYPE_APPLICATION_JSON_INITIAL_VERSION1 })
public interface ReportResource {

	@GET
	@Path("/getDeviceDetailsReport")
	@ApiOperation(value = "Get Device Details Report", notes = "Gets the Device Details Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceDetailsReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceDetailsReport(@BeanParam BaseFilter filter);

	@GET
	@Path("/getDeviceHistoryReport")
	@ApiOperation(value = "Get Device History Report", notes = "Gets the Device History Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceHistoryReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceHistoryReport(@BeanParam BaseFilter filter);

	@GET
	@Path("/getDeviceInventoryReport")
	@ApiOperation(value = "Get Device Inventory Report", notes = "Gets the Device Inventory Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceInventoryReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceInventoryReport(@BeanParam BaseFilter filter);

	@GET
	@Path("/getDeviceMalfunctionReport")
	@ApiOperation(value = "Get Device Malfunction Report", notes = "Gets the Device Malfunction Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceMalfunctionReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceMalfunctionReport(@BeanParam BaseFilter filter);
	
	@GET
	@Path("/getDeviceTrackingReport")
	@ApiOperation(value = "Get Device Tracking Report", notes = "Gets the Device Tracking Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = DeviceTrackingReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getDeviceTrackingReport(@BeanParam BaseFilter filter);
	
	
	@GET
	@Path("/getStudyBasedReport")
	@ApiOperation(value = "Get Device Tracking Report", notes = "Gets the Study Based Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyBasedReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getStudyBasedReport(@BeanParam BaseFilter filter);
	
	@GET
	@Path("/getAssetsDevicesByStudyReport")
	@ApiOperation(value = "Get Assets Devices By Study Report", notes = "Gets the Assets Devices By Study Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyBasedReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAssetsDevicesByStudyReport(@BeanParam AssetByStudyWidgetFilter filter);

	@GET
	@Path("/getAssetsDevicesMalfunctions")
	@ApiOperation(value = "Get Assets Devices Malfunctions Report", notes = "Gets the Assets Devices Malfunctions Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = StudyBasedReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getAssetsDevicesMalfunctionsReport(@BeanParam PointTrackerFilter filter);
	
	
	@GET
	@Path("/getPointTrackerReport")
	@ApiOperation(value = "Get Point Tracker Report", notes = "Gets the Point Tracker Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = PointTrackerResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getPointTrackerReport(@BeanParam PointTrackerFilter filter);

	@GET
	@Path("/getCustomerSupportTicketReport")
	@ApiOperation(value = "Get Customer Support Ticket Report", notes = "Gets Customer Support Ticket Report")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CustomerSupportListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCustomerSupportTicketReport(@BeanParam SupportFilter filter);

	@GET
	@Path("/getCustomerSupportIssueWidget")
	@ApiOperation(value = "Get Customer Support Issue widget", notes = "Get Customer Support Issue widget")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CustomerSupportIssueWidgetResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCustomerSupportIssueWidget(@BeanParam IssueWidgetFilter filter );

	@GET
	@Path("/getCustomerSupportIssueByStudyWidget")
	@ApiOperation(value = "Get Customer Support issue by study widget", notes = "Get Customer Support issue by study widget")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = CustomerSupportListResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getCustomerSupportIssueByStudyWidget(@BeanParam IssueByStudyWidgetFilter filter);


	@GET
	@Path("/getTotalAssets")
	@ApiOperation(value = "Get Total Assets", notes = "Get Total Assets")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TotalAssetsReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTotalAssets();
	
	@GET
	@Path("/getTotalAssetsByStatus")
	@ApiOperation(value = "Get Total Assets by status", notes = "Get Total Assets by status")
	@ApiResponses(value = {
			@ApiResponse(code = HttpStatus.SC_OK, message = "Successful Response", response = TotalAssetsByStatusReportResponse.class),
			@ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Bad Request", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Not Found", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_FORBIDDEN, message = "Forbidden", response = Message.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Runtime Error or Internal Server Error", response = Message.class) })
	public Response getTotalAssetsByStatus(@BeanParam TotalAssetsByStausWidgetFilter filter);

}
