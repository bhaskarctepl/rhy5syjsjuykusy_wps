package com.hillspet.wearables.common.constants;

import static com.hillspet.wearables.common.constants.Severity.THREE;
import static com.hillspet.wearables.common.constants.Severity.TWO;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.GATEWAY_TIMEOUT;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response.Status;

import com.hillspet.wearables.common.exceptions.ErrorDetailInterface;
import com.hillspet.wearables.common.exceptions.SeverityInterface;

/**
 * A set of constants representing error conditions that can be thrown by the
 * framework Application specific error codes must never be defined in this
 * class. This class is for only framework level error code definition.
 *
 * @author vvodyaram
 *
 */
public enum WearablesErrorCode implements ErrorDetailInterface, EnumInterface<String> {

	WEARABLES_INTERNAL_SRVR_ERROR("WEARABLES_SVC_001", THREE, WearablesMessageKey.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR),
	WEARABLES_SERVICE_RESPONSE_INVALID("WEARABLES_SVC_002", TWO, WearablesMessageKey.SERVICE_RESP_INVALID, INTERNAL_SERVER_ERROR),
	WEARABLES_CONSTRAINT_VIOLATION_EXP("WEARABLES_CONS_VIOLATION_EXP", WearablesMessageKey.INTERNAL_SERVER_ERROR),
	WEARABLES_WEB_APP_EXP("WEARABLES_WEB_APP_EXCEPTION", THREE, WearablesMessageKey.WEARABLES_WEB_APP_EXCEPTION, INTERNAL_SERVER_ERROR),
	WEARABLES_SYSTEM_ERROR("WEARABLES_SYSTEM_ERROR", THREE, WearablesMessageKey.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR),
	WEARABLES_INVALID_JSON_INPUT("WEARABLES_JSON_INPUT_INVALID", WearablesMessageKey.INVALID_JSON_INPUT),
	WEARABLES_INVALID_BEAN_CONFIG("WEARABLES_INVALID_BEAN_CONFIG", WearablesMessageKey.INVALID_JSON_INPUT),
	READ_TIMEOUT("WEARABLES_READ_001", THREE, WearablesMessageKey.READ_TIME_OUT, GATEWAY_TIMEOUT),
	CONNECTION_TIMEOUT("WEARABLES_CONN_001", THREE, WearablesMessageKey.CONNECTION_TIME_OUT, GATEWAY_TIMEOUT),
	// WEARABLES request error codes
	APP_ID_NOT_VALID("WEARABLES_REQ_001", WearablesMessageKey.APP_ID_NOT_VALID),
	APP_ENV_NOT_VALID("WEARABLES_REQ_002", WearablesMessageKey.APP_ENV_NOT_VALID),
	APP_ID_NOT_FOUND("WEARABLES_REQ_020", THREE, WearablesMessageKey.APP_ID_NOT_FOUND, NOT_FOUND),

	CONTENT_TYPE_INVALID("WEARABLES_REQ_017", WearablesMessageKey.CONTENT_TYPE_INVALID),
	APP_VERSION_BLANK("WEARABLES_REQ_011", WearablesMessageKey.APP_VERSION_BLANK),
	APP_VERSION_INVALID("WEARABLES_REQ_012", WearablesMessageKey.APP_VERSION_INVALID),
	APP_VERSION_INVALID_LENGTH("WEARABLES_REQ_013", WearablesMessageKey.APP_VERSION_INVALID_LENGTH),
	USR_ID_INVALID_LENGTH("WEARABLES_REQ_014", WearablesMessageKey.USERID_INVALID_LENGTH),
	USR_ID_INVALID("WEARABLES_REQ_015", WearablesMessageKey.USERID_INVALID),
	USR_ID_REQUIRED("WEARABLES_REQ_016", WearablesMessageKey.USR_ID_REQUIRED),

	// Required Error Codes
	USER_NAME_REQUIRED("WEARABLES_REQ_ERR_001", THREE, WearablesMessageKey.USER_NAME_REQUIRED, BAD_REQUEST),
	EMAIL_REQUIRED("WEARABLES_REQ_ERR_002", THREE, WearablesMessageKey.EMAIL_REQUIRED, BAD_REQUEST),
	MOBILE_NUMBER_REQUIRED("WEARABLES_REQ_ERR_003", THREE, WearablesMessageKey.MOBILE_NUMBER_REQUIRED, BAD_REQUEST),
	ORDER_NUMBER_REQUIRED("WEARABLES_REQ_ERR_004", THREE, WearablesMessageKey.ORDER_NUMBER_REQUIRED, BAD_REQUEST),
	PASSWORD_REQUIRED("WEARABLES_REQ_ERR_005", THREE, WearablesMessageKey.PASSWORD_REQUIRED, BAD_REQUEST),
	NEW_PASSWORD_REQUIRED("WEARABLES_REQ_ERR_006", THREE, WearablesMessageKey.NEW_PASSWORD_REQUIRED, BAD_REQUEST),
	CONFIRM_PASSWORD_REQUIRED("WEARABLES_REQ_ERR_007", THREE, WearablesMessageKey.CONFIRM_PASSWORD_REQUIRED, BAD_REQUEST),
	USER_ID_REQUIRED("WEARABLES_REQ_ERR_008", THREE, WearablesMessageKey.USER_ID_REQUIRED, BAD_REQUEST),
	
	DEVICE_NUMBER_REQUIRED("WEARABLES_REQ_ERR_009", THREE, WearablesMessageKey.DEVICE_NUMBER_REQUIRED, BAD_REQUEST),
	FIRMWARE_VERSION_NUMBER_REQUIRED("WEARABLES_REQ_ERR_010", THREE, WearablesMessageKey.FIRMWARE_VERSION_NUMBER_REQUIRED, BAD_REQUEST),

	INVALID_EMAIL("WEARABLES_INVAL_ERR_006", THREE, WearablesMessageKey.INVALID_EMAIL, BAD_REQUEST),
	INVALID_PASSWORD("WEARABLES_INVAL_ERR_001", THREE, WearablesMessageKey.INVALID_PASSWORD, BAD_REQUEST),
	INVALID_NEW_PASSWORD("WEARABLES_INVAL_ERR_002", THREE, WearablesMessageKey.INVALID_NEW_PASSWORD, BAD_REQUEST),
	INVALID_CONFIRM_PASSWORD("WEARABLES_INVAL_ERR_003", THREE, WearablesMessageKey.INVALID_CONFIRM_PASSWORD, BAD_REQUEST),
	INVALID_PASSWORD_NEW_PASSWORD("WEARABLES_INVAL_ERR_004", THREE, WearablesMessageKey.INVALID_PASSWORD_NEW_PASSWORD, BAD_REQUEST),
	INVALID_NEW_CONFIRM_PASSWORD("WEARABLES_INVAL_ERR_005", THREE, WearablesMessageKey.INVALID_NEW_CONFIRM_PASSWORD, BAD_REQUEST),
	INVALID_PASSWORD_LENGTH("WEARABLES_INVAL_ERR_007", THREE, WearablesMessageKey.INVALID_PASSWORD_LENGTH, BAD_REQUEST),
	INVALID_NEW_PASSWORD_LENGTH("WEARABLES_INVAL_ERR_008", THREE, WearablesMessageKey.INVALID_NEW_PASSWORD_LENGTH, BAD_REQUEST),
	INVALID_CONFIRM_PASSWORD_LENGTH("WEARABLES_INVAL_ERR_009", THREE, WearablesMessageKey.INVALID_CONFIRM_PASSWORD_LENGTH, BAD_REQUEST),
	
	FIRMWARE_VERSION_ALREADY_EXISTS("WEARABLES_INVAL_ERR_010", THREE, WearablesMessageKey.FIRMWARE_VERSION_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_ALREADY_EXISTS("WEARABLES_INVAL_ERR_011", THREE, WearablesMessageKey.DEVICE_ALREADY_EXISTS, BAD_REQUEST),
	USER_NAME_ALREADY_EXISTS("WEARABLES_INVAL_ERR_012", THREE, WearablesMessageKey.USER_NAME_ALREADY_EXISTS, BAD_REQUEST),
	ROLE_ALREADY_EXISTS("WEARABLES_INVAL_ERR_013", THREE, WearablesMessageKey.ROLE_ALREADY_EXISTS, BAD_REQUEST),
	STUDY_ALREADY_ASSIGNED_TO_PET("WEARABLES_INVAL_ERR_029", THREE, WearablesMessageKey.STUDY_ALREADY_ASSIGNED_TO_PET, BAD_REQUEST),
	STUDY_ALREADY_EXIST_TO_PET("WEARABLES_INVAL_ERR_046", THREE, WearablesMessageKey.STUDY_ALREADY_ASSIGNED_TO_PET, BAD_REQUEST),
	TRACKER_NAME_ALREADY_EXISTS("WEARABLES_INVAL_ERR_014", THREE, WearablesMessageKey.TRACKER_NAME_ALREADY_EXISTS, BAD_REQUEST),
	PLAN_NAME_ALREADY_EXISTS("WEARABLES_INVAL_ERR_015", THREE, WearablesMessageKey.PLAN_NAME_ALREADY_EXISTS, BAD_REQUEST),
	STUDY_NAME_ALREADY_EXISTS("WEARABLES_INVAL_ERR_016", THREE, WearablesMessageKey.STUDY_NAME_ALREADY_EXISTS, BAD_REQUEST),
	STUY_PUSH_NOTIFICATION_ALREADY_EXISTS("WEARABLES_INVAL_ERR_050", THREE, WearablesMessageKey.STUDY_PUSH_NOTIFICATION_NAME_ALREADY_EXISTS, BAD_REQUEST),
	PLAN_ALREADY_MAPPED_TO_STUDY("WEARABLES_INVAL_ERR_017", THREE, WearablesMessageKey.PLAN_ALREADY_MAPPED_TO_STUDY, BAD_REQUEST),
	PLAN_ALREADY_MAPPED_TO_PET("WEARABLES_INVAL_ERR_018", THREE, WearablesMessageKey.PLAN_ALREADY_MAPPED_TO_PET, BAD_REQUEST),
	PET_PARENT_EMAIL_ALREADY_EXISTS("WEARABLES_INVAL_ERR_019", THREE, WearablesMessageKey.PET_PARENT_EMAIL_ALREADY_EXISTS, BAD_REQUEST),
	SUPPORT_TICKET_ALREADY_CLOSED("WEARABLES_INVAL_ERR_020", THREE, WearablesMessageKey.SUPPORT_TICKET_ALREADY_CLOSED, BAD_REQUEST),
	FAVOURITE_ALREADY_EXISTS("WEARABLES_INVAL_ERR_021", THREE, WearablesMessageKey.FAVOURITE_ALREADY_EXISTS, BAD_REQUEST),
	FAVOURITE_DOES_NOT_EXISTS("WEARABLES_INVAL_ERR_022", THREE, WearablesMessageKey.FAVOURITE_DOES_NOT_EXISTS, BAD_REQUEST),
	INVALID_PLAN_STUDY_ASSOCAITION("WEARABLES_INVAL_ERR_023", THREE, WearablesMessageKey.INVALID_PLAN_STUDY_ASSOCAITION, BAD_REQUEST),
	QUESTIONNAIRE_NAME_ALREADY_EXISTS("WEARABLES_INVAL_ERR_024", THREE, WearablesMessageKey.QUESTIONNAIRE_NAME_ALREADY_EXISTS, BAD_REQUEST),
	QUESTIONNAIRE_INSTRUCTION_ALREADY_EXISTS("WEARABLES_INVAL_ERR_025", THREE, WearablesMessageKey.QUESTIONNAIRE_INSTRUCTION_ALREADY_EXISTS, BAD_REQUEST),
	QUESTIONNAIRE_INSTRUCTION_ID_INVALID("WEARABLES_INVAL_ERR_026", THREE, WearablesMessageKey.QUESTIONNAIRE_INSTRUCTION_ID_INVALID, BAD_REQUEST),
	QUESTIONNAIRE_QUESTION_ALREADY_EXISTS("WEARABLES_INVAL_ERR_027", THREE, WearablesMessageKey.QUESTIONNAIRE_QUESTION_ALREADY_EXISTS, BAD_REQUEST),
	QUESTIONNAIRE_QUESTION_ID_INVALID("WEARABLES_INVAL_ERR_028", THREE, WearablesMessageKey.QUESTIONNAIRE_QUESTION_ID_INVALID, BAD_REQUEST),
	PET_NOTE_ALREADY_EXISTS("WEARABLES_INVAL_ERR_029", THREE, WearablesMessageKey.PET_NOTE_ALREADY_EXISTS, BAD_REQUEST),
	INVALID_STUDY_QUESTIONNAIRE_ASSOCAITION("WEARABLES_INVAL_ERR_030", THREE, WearablesMessageKey.INVALID_STUDY_QUESTIONNAIRE_ASSOCAITION, BAD_REQUEST),
	QUESTIONNAIRE_ALREADY_MAPPED_TO_STUDY("WEARABLES_INVAL_ERR_031", THREE, WearablesMessageKey.QUESTIONNAIRE_ALREADY_MAPPED_TO_STUDY, BAD_REQUEST),
	PET_PARENT_ALREADY_EXISTS("WEARABLES_INVAL_ERR_032", THREE, WearablesMessageKey.PET_PARENT_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_ALREADY_MAPPED("WEARABLES_INVAL_ERR_033", THREE, WearablesMessageKey.DEVICE_ALREADY_MAPPED, BAD_REQUEST),
	PLAN_MAPPED_TO_STUDY_ACTIVE("WEARABLES_INVAL_ERR_034", THREE, WearablesMessageKey.PLAN_MAPPED_TO_STUDY_ACTIVE, BAD_REQUEST),
	EMAIL_ALREADY_EXISTS("WEARABLES_INVAL_ERR_035", THREE, WearablesMessageKey.EMAIL_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_TYPES_SHOULD_BE_SAME("WEARABLES_INVAL_ERR_036", THREE, WearablesMessageKey.DEVICE_TYPES_SHOULD_BE_SAME, BAD_REQUEST),
	DEVICE_MODELS_SHOULD_BE_SAME("WEARABLES_INVAL_ERR_037", THREE, WearablesMessageKey.DEVICE_MODELS_SHOULD_BE_SAME, BAD_REQUEST),
	PET_ON_STUDY("WEARABLES_INVAL_ERR_039", THREE, WearablesMessageKey.PET_CANNOT_DELETE, BAD_REQUEST),
	STUDY_CANNOT_DELETE_ACTIVE("WEARABLES_INVAL_ERR_040", THREE, WearablesMessageKey.STUDY_CANNOT_DELETE_ACTIVE, BAD_REQUEST),
	DEVICE_MODEL_ALREADY_EXISTS("WEARABLES_INVAL_ERR_041", THREE, WearablesMessageKey.DEVICE_MODEL_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_MFG_SERIAL_NUMBER_ALREADY_EXISTS("WEARABLES_INVAL_ERR_042", THREE, WearablesMessageKey.DEVICE_MFG_SERIAL_NUMBER_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_MFG_MFG_MAC_ADDR_ALREADY_EXISTS("WEARABLES_INVAL_ERR_043", THREE, WearablesMessageKey.DEVICE_MFG_MFG_MAC_ADDR_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_WIFI_MAC_ADDR_ALREADY_EXISTS("WEARABLES_INVAL_ERR_053", THREE, WearablesMessageKey.DEVICE_WIFI_MAC_ADDR_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_CANNOT_DELETE("WEARABLES_INVAL_ERR_044",  THREE, WearablesMessageKey.DEVICE_CANNOT_DELETE, BAD_REQUEST),
	FIRMWARE_CANNOT_DELETE("WEARABLES_INVAL_ERR_045",  THREE, WearablesMessageKey.FIRMWARE_CANNOT_DELETE, BAD_REQUEST),
	FIRMWARE_VERSION_REFERENCED("WEARABLES_INVAL_ERR_047",  THREE, WearablesMessageKey.FIRMWARE_VERSION_REFERENCED, BAD_REQUEST),
	REPORT_NAME_ALREADY_EXISTS("WEARABLES_INVAL_ERR_048", THREE, WearablesMessageKey.REPORT_NAME_ALREADY_EXISTS, BAD_REQUEST),
	STUDY_CAMPAIGN_EXISTS("WEARABLES_INVAL_ERR_049", THREE, WearablesMessageKey.STUDY_CAMPAIGN_EXISTS, BAD_REQUEST),
	
	ALREADY_BEING_REFERENCED("WEARABLES_INVAL_ERR_038", THREE, WearablesMessageKey.ALREADY_BEING_REFERENCED, BAD_REQUEST),
	IMAGE_SCORING_SCALE_ALREADY_EXISTS("WEARABLES_INVAL_ERR_051", THREE, WearablesMessageKey.IMAGE_SCORING_SCALE_ALREADY_EXISTS, BAD_REQUEST),
	
	ROLE_CANNOT_INACTIVE("WEARABLES_INACT_ERR_001", THREE, WearablesMessageKey.ROLE_CANNOT_INACTIVE, BAD_REQUEST),
	
	ROLE_CANNOT_DELETE("WEARABLES_DEL_ERR_001", THREE, WearablesMessageKey.ROLE_CANNOT_DELETE, BAD_REQUEST),
	USER_CANNOT_DELETE("WEARABLES_DEL_ERR_002", THREE, WearablesMessageKey.USER_CANNOT_DELETE, BAD_REQUEST),
	ROLE_CANNOT_DELETE_ACTIVE("WEARABLES_DEL_ERR_003", THREE, WearablesMessageKey.ROLE_CANNOT_DELETE_ACTIVE, BAD_REQUEST),
	
	// Not Found Error Codes
	USERNAME_NOT_FOUND("WEARABLES_NOT_FOUND_ERR_001", THREE, WearablesMessageKey.USERNAME_NOT_FOUND, NOT_FOUND),
	INCORRECT_PASSWORD("WEARABLES_INCRCT_ERR_001", THREE, WearablesMessageKey.INCORRECT_PASSWORD, BAD_REQUEST),
	INCORRECT_NEW_PASSWORD("WEARABLES_INCRCT_ERR_002", THREE, WearablesMessageKey.INCORRECT_NEW_PASSWORD, BAD_REQUEST),

	AUTHORIZATION_FAILED("WEARABLES_TKN_003", TWO, WearablesMessageKey.AUTHORIZATION_FAILED, FORBIDDEN),
	AUTHENTICATION_FAILED("WEARABLES_TKN_004", TWO, WearablesMessageKey.AUTHENTICATION_FAILED, UNAUTHORIZED),

	WEARABLES_INVALID_CONDITION("WEARABLES_VAL_001", TWO, WearablesMessageKey.WEARABLES_VAL_ERROR, BAD_REQUEST),
	CACHE_REGION_REQUIRED("WEARABLES_CACHE_001", WearablesMessageKey.CACHE_REGION_REQUIRED),
	CACHE_REGION_INVALID("WEARABLES_CACHE_002", WearablesMessageKey.CACHE_REGION_INVALID),
	CACHE_REGION_KEY_INVALID("WEARABLES_CACHE_003", WearablesMessageKey.CACHE_REGION_KEY_INVALID),

	// Executor service error codes
	EXECUTOR_SERVICE_INPROGRESS("EXEC_SVC_001", WearablesMessageKey.EXEC_SVC_INPROGRESS), 
	
	
	PUSH_NOTIFICATION_ALREADY_MAPPED_TO_STUDY("WEARABLES_INVAL_ERR_053", THREE, WearablesMessageKey.PUSH_NOTIFICATION_ALREADY_MAPPED_TO_STUDY, BAD_REQUEST),
	INVALID_STUDY_PUSH_NOTIFICATION_ASSOCAITION("WEARABLES_INVAL_ERR_054", THREE, WearablesMessageKey.INVALID_STUDY_PUSH_NOTIFICATION_ASSOCAITION, BAD_REQUEST),
	DEVICE_SHIPMENT_ALREADY_EXISTS("WEARABLES_INVAL_ERR_055", THREE, WearablesMessageKey.DEVICE_SHIPMENT_ALREADY_EXISTS, BAD_REQUEST),
	DEVICE_SHIPMENT_TRACKING_NUMBER_ALREADY_EXISTS("WEARABLES_INVAL_ERR_056", THREE, WearablesMessageKey.DEVICE_SHIPMENT_TRACKING_NUMBER_ALREADY_EXISTS, BAD_REQUEST);
	
	private String code;

	private MessageKeyInterface key;

	private SeverityInterface severity;

	private Status errorStatus;

	private WearablesErrorCode(String code, SeverityInterface severity, MessageKeyInterface msg, Status status) {
		this.code = code;
		this.severity = severity;
		this.key = msg;
		this.errorStatus = status;
	}

	private WearablesErrorCode(String code, MessageKeyInterface msg) {
		this.code = code;
		this.severity = THREE;
		this.key = msg;
		this.errorStatus = BAD_REQUEST;

	}

	/**
	 * Lookup method to return the Enum corresponding to the code
	 *
	 * @param code
	 * @return
	 */
	public static Optional<WearablesErrorCode> get(@NotNull String code) {
		final List<WearablesErrorCode> errorCodes = Arrays.asList(values());
		return errorCodes.stream().filter(errorCode -> errorCode.getCode().equalsIgnoreCase(code)).findFirst();
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getErrorCode() {
		return code;
	}

	@Override
	public MessageKeyInterface getKey() {
		return key;
	}

	@Override
	public SeverityInterface getSeverity() {
		return severity;
	}

	@Override
	public Integer getStatus() {
		return errorStatus.getStatusCode();
	}

	@Override
	public String getErrorMessage() {
		return code;
	}

	/**
	 * @return the errorStatus
	 */
	public Status getErrorStatus() {
		return errorStatus;
	}

}