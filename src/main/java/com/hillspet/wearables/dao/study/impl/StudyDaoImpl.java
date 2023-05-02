package com.hillspet.wearables.dao.study.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.constants.SQLConstants;
import com.hillspet.wearables.common.constants.StringLiterals;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.study.StudyDao;
import com.hillspet.wearables.dto.PetListDTO;
import com.hillspet.wearables.dto.PreludeDataByStudyDTO;
import com.hillspet.wearables.dto.PreludeListDTO;
import com.hillspet.wearables.dto.PreludeMandatory;
import com.hillspet.wearables.dto.PushNotification;
import com.hillspet.wearables.dto.QuestionnaireListDTO;
import com.hillspet.wearables.dto.StudyDTO;
import com.hillspet.wearables.dto.StudyImageScale;
import com.hillspet.wearables.dto.StudyListDTO;
import com.hillspet.wearables.dto.StudyNotification;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.StudyFilter;
import com.hillspet.wearables.request.ActivityFactorConfig;
import com.hillspet.wearables.request.ImageScoringScalesAssociated;
import com.hillspet.wearables.request.PlanSubscribed;
import com.hillspet.wearables.request.PreludeAssociated;
import com.hillspet.wearables.request.PushNotificationsAssociated;
import com.hillspet.wearables.request.QuestionnaireAssociated;
import com.hillspet.wearables.request.StudyNotificationRequest;
import com.hillspet.wearables.request.StudyRequest;
import com.hillspet.wearables.response.StudyNotesResponse;

@Repository
public class StudyDaoImpl extends BaseDaoImpl implements StudyDao {

	private static final Logger LOGGER = LogManager.getLogger(StudyDaoImpl.class);
	
	@Value("${gcp.env}")
	private String environment;
	
	private String loadPreludeCFUrl = System.getenv("LOAD_PRELUDE_CR_URL");
	
	/* ---------------- STUDY SERVICES ------------------------- */

	@Override
	public void addStudy(StudyRequest studyRequest, Integer userId) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_study_name", new ObjectMapper().writeValueAsString(studyRequest.getStudyName()));
			inputParams.put("p_principle_investigator", studyRequest.getPrincipleInvestigator());
			inputParams.put("p_description", studyRequest.getDescription());
			inputParams.put("p_start_date", studyRequest.getStartDate());
			inputParams.put("p_end_date", studyRequest.getEndDate());
			inputParams.put("p_is_active", studyRequest.getStatus());
			inputParams.put("p_notes", studyRequest.getNotes());
			inputParams.put("p_plans_subscribed",
					StringUtils.join(studyRequest.getPlansSubscribed(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_mobile_app_configs",
					StringUtils.join(studyRequest.getMobileAppConfigs(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_weight_unit", studyRequest.getWeightUnit());
			inputParams.put("p_entsm_scale_start_date", studyRequest.getEntsmScaleStartDate());
			inputParams.put("p_entsm_scale_end_date", studyRequest.getEntsmScaleEndDate());
			inputParams.put("p_questionnaire_json",
					new ObjectMapper().writeValueAsString(studyRequest.getQuestionnairesAssociated()));
			inputParams.put("p_push_notifications_associated", StringUtils
					.join(studyRequest.getPushNotificationsAssociated(), StringLiterals.SEPERATOR.getCode()));
			inputParams.put("p_notification_enabled", studyRequest.getIsNotificationEnable());
			inputParams.put("p_is_external", studyRequest.getIsExternal());
			inputParams.put("p_image_scale_json",
					new ObjectMapper().writeValueAsString(studyRequest.getImageScoringSaclesAssociated()));
			inputParams.put("p_url", studyRequest.getExternalLink());
			inputParams.put("p_created_by", userId);

			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_INSERT, inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				if (studyRequest.getIsExternal() == 1 && !studyRequest.getExternalLink().isEmpty()) {
					Integer studyId = (int) outParams.get("last_insert_id");
					LOGGER.info("Study has been created successfully, Study id is ", studyId);
					try {
						postDataToEndpointAysnc(
								loadPreludeCFUrl,
								studyId.toString(), environment);
					} catch (ExecutionException e) {
						LOGGER.error("error while executing addStudy ", e);
						throw new ServiceExecutionException(e.getMessage());
					} catch (InterruptedException e) {
						LOGGER.error("error while executing addStudy ", e);
						throw new ServiceExecutionException(e.getMessage());
					} catch (TimeoutException e) {
						LOGGER.error("error while executing addStudy ", e);
						throw new ServiceExecutionException(e.getMessage());
					}
				}
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("addStudy service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.STUDY_NAME_ALREADY_EXISTS,
									studyRequest.getStudyName())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing addStudy ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateStudy(StudyRequest studyRequest, Integer userId) {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_study_id", studyRequest.getStudyId());
			inputParams.put("p_study_name", new ObjectMapper().writeValueAsString(studyRequest.getStudyName()));

			if (!StringUtils.isBlank(studyRequest.getExternalLink())) {
				inputParams.put("p_url", studyRequest.getExternalLink());
			} else {
				inputParams.put("p_url", "");
			}

			inputParams.put("p_principle_investigator", studyRequest.getPrincipleInvestigator());
			inputParams.put("p_description", studyRequest.getDescription());
			inputParams.put("p_start_date", studyRequest.getStartDate());
			inputParams.put("p_end_date", studyRequest.getEndDate());
			inputParams.put("p_is_active", studyRequest.getStatus());
			inputParams.put("p_notes", studyRequest.getNotes());
			inputParams.put("p_plans_subscribed",
					StringUtils.join(studyRequest.getPlansSubscribed(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_mobile_app_configs",
					StringUtils.join(studyRequest.getMobileAppConfigs(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_weight_unit", studyRequest.getWeightUnit());
			inputParams.put("p_entsm_scale_start_date", studyRequest.getEntsmScaleStartDate());
			inputParams.put("p_entsm_scale_end_date", studyRequest.getEntsmScaleEndDate());
			inputParams.put("p_questionnaire_json",
					new ObjectMapper().writeValueAsString(studyRequest.getQuestionnairesAssociated()));
			inputParams.put("p_push_notifications_associated",
					StringUtils.join(studyRequest.getPushNotificationsAssociated(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_prelude_associated",
					StringUtils.join(studyRequest.getPreludeAssociated(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_prelude_mandatory_associated",
					StringUtils.join(studyRequest.getPreludeMandatory(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_notification_enabled", studyRequest.getIsNotificationEnable());
			inputParams.put("p_image_scale_json",
					new ObjectMapper().writeValueAsString(studyRequest.getImageScoringSaclesAssociated()));
			inputParams.put("p_is_external", studyRequest.getIsExternal());
			inputParams.put("p_owner_name", studyRequest.getOwnerLastName());
			inputParams.put("p_owner_email", studyRequest.getOwnerEmail());
			inputParams.put("p_pet_name", studyRequest.getPetName());
//			inputParams.put("p_url", studyRequest.getExternalLink());
			inputParams.put("p_patient_id", studyRequest.getPatientId());
			inputParams.put("p_modified_by", userId);
			inputParams.put("p_activity_factor_config_json",
					new ObjectMapper().writeValueAsString(studyRequest.getActivityFactorConfig()));

			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_UPDATE, inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// postDataToEndpoint("https://us-central1-ct-wearables-portal-pf.cloudfunctions.net/cftest/studyId=?",studyUrl);
				// add query param here.
				LOGGER.info("Study has been updated successfully, Study id is ", studyRequest.getStudyId());
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("updateStudy service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.STUDY_NAME_ALREADY_EXISTS,
									studyRequest.getStudyName())));
				} else {
					LOGGER.error("error while executing updateStudy =============", errorMsg);
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing updateStudy ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	public HttpURLConnection postDataToEndpointSync(String endpoint) {

		HttpURLConnection httpClient = null;

		try {
			if (endpoint != null && !endpoint.equals("")) {
				httpClient = (HttpURLConnection) new URL(endpoint).openConnection();
				httpClient.setRequestMethod("GET");
				// httpClient.setRequestProperty("StudyURL", "500");
				httpClient.setDoOutput(true);
				LOGGER.error("\n Sent 'POST' request to URL : " + endpoint + " \n Response Code: "
						+ httpClient.getResponseCode());
			}
		} catch (MalformedURLException e) {
			LOGGER.error("MalformedURLException in postDataToEndpoint method:" + e.getMessage());
		} catch (ProtocolException e) {
			LOGGER.error("ProtocolException in postDataToEndpoint method:" + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception in postDataToEndpoint method:" + e.getMessage());
		}

		return httpClient;
	}

	/**
	 * Calls Cloud Function Asynchronously
	 * 
	 * @param endpoint
	 * @param output
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	@SuppressWarnings("deprecation")
	public DeferredResult<String> postDataToEndpointAysnc(String endpoint, String studyId, String env)
			throws ExecutionException, InterruptedException, TimeoutException {
		LOGGER.info("STARTED postDataToEndpointAysnc");
		String bearerToken = "";
		HttpHeaders headers = new HttpHeaders();
		AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccessControlRequestMethod(HttpMethod.POST);
		LOGGER.info("BEFORE TRY");

		try {
			// GoogleCredentials googleCredentials =
			// GoogleCredentials.getApplicationDefault();
			// googleCredentials.refreshAccessToken().getTokenValue();
			// LOGGER.info("googleCredentials ::" + googleCredentials);

			/****************************** NEW MOVE ***************************/

			ServiceAccountCredentials saCreds = ServiceAccountCredentials
					.fromStream(new ClassPathResource("ct-wearables-portal-pf-dba36e55aed3.json").getInputStream());

			/*
			 * ServiceAccountCredentials saCreds = ServiceAccountCredentials .fromStream(
			 * new ByteArrayInputStream(cfAuthJsonText.getBytes()) );
			 */

			saCreds = (ServiceAccountCredentials) saCreds
					.createScoped(Arrays.asList("https://www.googleapis.com/auth/iam"));
			IdTokenCredentials tokenCredential = IdTokenCredentials.newBuilder().setIdTokenProvider(saCreds)
					.setTargetAudience(loadPreludeCFUrl)
					.build();
			LOGGER.info("tokenCredential " + tokenCredential);
			LOGGER.info("tokenCredential " + tokenCredential.refreshAccessToken());
			bearerToken = tokenCredential.refreshAccessToken().getTokenValue();
			/****************************** NEW MOVE end ***************************/

			LOGGER.info("BEARER Token NWE ::" + bearerToken);
		} catch (IOException e) {
			LOGGER.info("<<<< EXCEPTION OCCURED >>>>>");
			e.printStackTrace();
		}
		headers.set("Authorization", "Bearer " + bearerToken);

		LOGGER.info("postDataToEndpoint::StudyId, ENV" + studyId + ":" + env);
		HttpEntity<String> requestEntity = new HttpEntity<String>(
				"{\"StudyId\":\"" + studyId + "\", \"Env\":\"" + env + "\"}", headers);

		// final DeferredResult<String> result = new DeferredResult<>();
		ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.postForEntity(endpoint, requestEntity,
				String.class);

		future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {

			@Override
			public void onFailure(Throwable ex) {
				LOGGER.error("onFaileusre::" + ex.getMessage());
			}

			@Override
			public void onSuccess(ResponseEntity<String> value) {
				LOGGER.error("onSuccess::" + value.getBody());
				LOGGER.error("status code::" + value.getStatusCodeValue());
			}
		});

		return null;
	}

	@Override
	public void associatePlan(int studyId, int planId, LocalDate subscriptionDate, Integer userId) {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_plan_id", planId);
		inputParams.put("p_subscription_date", subscriptionDate);
		inputParams.put("p_created_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_ASSOCIATE_PLAN, inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Plan has been associated to Study successfully");
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"associatePlan service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.PLAN_ALREADY_MAPPED_TO_STUDY)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing addStudy ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void disassociatePlan(int studyId, int planId, Integer userId) {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_plan_id", planId);
		inputParams.put("p_modified_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_DISASSOCIATE_PLAN, inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Plan has been disassociated with Study successfully");
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("updateStudy service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.PLAN_ALREADY_MAPPED_TO_STUDY, errorMsg)));

				} else if (statusFlag == -3) {

					throw new ServiceExecutionException("updateStudy service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.STUDY_NAME_ALREADY_EXISTS, studyId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing addStudy ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<PetListDTO> getAssociatedPets(int studyId) {
		List<PetListDTO> petList = new ArrayList<>();
		LOGGER.debug("getAssociatedPets called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ASSOCIATED_PETS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetListDTO petListDTO = new PetListDTO();
					petListDTO.setPetId(rs.getInt("PET_ID"));
					petListDTO.setPetName(rs.getString("PET_NAME"));
					petListDTO.setPetPhoto(rs.getString("PHOTO_NAME"));
					petListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					petListDTO.setPetStatus(rs.getString("STATUS_NAME"));
					petListDTO.setPetStudyId(rs.getInt("PET_STUDY_ID"));
					petList.add(petListDTO);
				}
			}, studyId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getAssociatedPets", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getStudyListCount(StudyFilter filter) throws ServiceExecutionException {
		int totalCount = NumberUtils.INTEGER_ZERO;
		LOGGER.debug("getStudyListCount called");
		String counts;
		HashMap<String, Integer> map = new HashMap<>();
		try {
			counts = selectForObject(SQLConstants.STUDY_GET_LIST_COUNT, String.class, filter.getSearchText(),
					filter.getPlanId(), filter.getStatusId(), filter.getUserId(), filter.getRoleTypeId());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getStudyListCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<StudyListDTO> getStudies(StudyFilter filter) throws ServiceExecutionException {
		List<StudyListDTO> studyList = new ArrayList<>();
		LOGGER.debug("getStudies called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					StudyListDTO studyListDTO = new StudyListDTO();
					// studyListDTO.setSlNumber(rs.getInt("slNo"));
					studyListDTO.setStudyId(rs.getInt("STUDY_ID"));
					studyListDTO.setActionId(rs.getInt("ACTION_ID"));
					studyListDTO.setStudyName(rs.getString("STUDY_NAME"));
					studyListDTO.setPlanName(rs.getString("PLAN_NAME"));
					studyListDTO.setStartDate(rs.getString("START_DATE") == null ? "" : rs.getString("START_DATE"));
					studyListDTO.setEndDate(rs.getString("END_DATE") == null ? "" : rs.getString("END_DATE"));
					studyListDTO.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
					studyListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					studyList.add(studyListDTO);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getPlanId(), filter.getStatusId(), filter.getUserId(), filter.getRoleTypeId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getStudies", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyList;
	}

	@Override
	public List<StudyListDTO> getStudyList(int userId) throws ServiceExecutionException {
		List<StudyListDTO> studyList = new ArrayList<>();
		LOGGER.debug("getStudyList called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ALL_STUDY_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					StudyListDTO studyListDTO = new StudyListDTO();
					studyListDTO.setStudyId(rs.getInt("STUDY_ID"));
					studyListDTO.setStudyName(rs.getString("STUDY_NAME"));
					studyListDTO.setPrincipleInvestigator(rs.getString("PRINCIPLE_INVESTIGATOR"));
					studyListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					studyListDTO.setStartDate(rs.getString("START_DATE") == null ? "" : rs.getString("START_DATE"));
					studyListDTO.setEndDate(rs.getString("END_DATE") == null ? "" : rs.getString("END_DATE"));
					studyListDTO.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
					studyListDTO.setIsExternal(rs.getInt("IS_EXTERNAL"));
					studyListDTO.setStudyDescription(rs.getString("DESCRIPTION"));
					studyList.add(studyListDTO);
				}
			}, userId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getStudyList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyList;
	}

	@Override
	public List<StudyListDTO> getStudiesByPetParentAndPet(int petParentId, int petId) throws ServiceExecutionException {
		List<StudyListDTO> studyList = new ArrayList<>();
		LOGGER.debug("getStudyList called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_STUDY_LIST_BY_PET_PARENT_AND_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					StudyListDTO studyListDTO = new StudyListDTO();
					studyListDTO.setStudyId(rs.getInt("STUDY_ID"));
					studyListDTO.setStudyName(rs.getString("STUDY_NAME"));
					studyList.add(studyListDTO);
				}
			}, petParentId, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getStudyList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StudyDTO getStudyById(int studyId) throws ServiceExecutionException {
		final StudyDTO study = new StudyDTO();
		List<StudyNotesResponse> notes = new ArrayList<>();
		List<PreludeMandatory> preludeMandatoryList = new ArrayList<>();
		LOGGER.debug("getStudyById called");
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_study_id", studyId);

			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_GET_BY_ID, inputParams);
			LOGGER.info("outParams are {}", outParams);

			Iterator<Entry<String, Object>> itr = outParams.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(SQLConstants.RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(studyData -> {
						study.setStudyId((Integer) studyData.get("STUDY_ID"));
						study.setStudyName((String) studyData.get("STUDY_NAME"));
						study.setDescription((String) studyData.get("DESCRIPTION") == null ? ""
								: (String) studyData.get("DESCRIPTION"));
						study.setPrincipleInvestigator((String) studyData.get("PRINCIPLE_INVESTIGATOR") == null ? ""
								: (String) studyData.get("PRINCIPLE_INVESTIGATOR"));
						Date startDate = (Date) studyData.get("START_DATE");
						Date endDate = (Date) studyData.get("END_DATE");
						study.setStartDate(startDate != null ? startDate : null);
						study.setEndDate(endDate != null ? endDate : null);
						Boolean isActive = (Boolean) studyData.get("IS_ACTIVE");
						if (isActive != null) {
							study.setStatus(isActive ? 1 : 0);
						} else {
							study.setStatus(0);
						}

						String appConfigs = (String) studyData.get("MOBILE_APP_CONFIGS");
						String studyPlans = (String) studyData.get("STUDY_PLANS");
						String studyQuestionnaires = (String) studyData.get("STUDY_QUESTIONNAIRES");
						String studyPushNotifications = (String) studyData.get("STUDY_PUSH_NOTIFICATIONS");
						String studyImageSclaes = (String) studyData.get("STUDY_IMAGE_SCALES");
						String studyPrelude = (String) studyData.get("STUDY_PRELUDE");
//						String studyPreludeMandatoru = (String) studyData.get("STUDY_PRELUDE_MANDATORY");
						String preludeUrl = (String) studyData.get("URL") != null ? (String) studyData.get("URL") : "";
						String weightUnit = (String) studyData.get("WEIGHT_UNIT") != null
								? (String) studyData.get("WEIGHT_UNIT")
								: "";

						study.setMobileAppConfigs(appConfigs == null ? new ArrayList<Integer>()
								: Arrays.asList(appConfigs.split(",", -1)).stream().map(Integer::parseInt)
										.collect(Collectors.toList()));
						study.setWeightUnit(weightUnit);

						String entsmScleStartDate = (String) studyData.get("ENTSM_SCALE_START_DATE");
						String entsmScleEndDate = (String) studyData.get("ENTSM_SCALE_END_DATE");
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						study.setEntsmScaleStartDate(
								entsmScleStartDate != null ? LocalDate.parse(entsmScleStartDate, formatter) : null);
						study.setEntsmScaleEndDate(
								entsmScleEndDate != null ? LocalDate.parse(entsmScleEndDate, formatter) : null);

						study.setPlansSubscribed(getPlanSubscribedList(studyPlans));
						study.setQuestionnairesAssociated(getAssociatedQuestionnaireList(studyQuestionnaires));
						study.setPushNotificationsAssociated(getAssociatedPushNotificationList(studyPushNotifications));
						study.setImageScoringSaclesAssociated(getAssociatedImageScaleList(studyImageSclaes));
						study.setPreludeAssociated(getAssociatedPreludeList(studyPrelude));
						study.setPreludeUrl(preludeUrl);
						study.setIsDataLoadSuccess((Integer) studyData.get("IS_DATA_LOAD_SUCCESS"));
						Long totalPets = (Long) studyData.get("TOTAL_PETS");
						Long totalActivePets = (Long) studyData.get("TOTAL_ACTIVE_PETS");
						Long totalInactivePets = (Long) studyData.get("TOTAL_INACTIVE_PETS");
						Boolean notificationEnabled = (Boolean) studyData.get("NOTIFICATIONS_ENABLED");
						if (notificationEnabled != null) {
							study.setIsNotificationEnable(notificationEnabled ? 1 : 0);
						} else {
							study.setIsNotificationEnable(0);
						}
						study.setIsExternal((Integer) studyData.get("IS_EXTERNAL"));
						study.setTotalPets(Integer.parseInt(String.valueOf(totalPets)));
						study.setTotalActivePets(Integer.parseInt(String.valueOf(totalActivePets)));
						study.setTotalInactivePets(Integer.parseInt(String.valueOf(totalInactivePets)));
					});
				}
				if (key.equals(SQLConstants.RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(noteData -> {
						StudyNotesResponse note = new StudyNotesResponse();
						note.setStudyNoteId((Integer) noteData.get("STUDY_NOTE_ID"));
						note.setContent((String) noteData.get("CONTENT"));
						// Timestamp createdDate = (Timestamp) noteData.get("CREATED_DATE");
						// note.setCreatedDate(createdDate != null ?
						// createdDate.toLocalDateTime().toLocalDate() : null);
						note.setCreatedDate((LocalDateTime) noteData.get("CREATED_DATE"));
						note.setUserName((String) noteData.get("USER_NAME"));
						notes.add(note);
					});
				}

				if (key.equals(SQLConstants.RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(mandatoryData -> {
						PreludeMandatory preludeMandatory = new PreludeMandatory();

						preludeMandatory.setCategory((String) mandatoryData.get("CATEGORY"));
						preludeMandatory.setExternalStudyId((String) mandatoryData.get("EXT_STUDY_ID"));
						preludeMandatory.setExtractDefId((Integer) mandatoryData.get("EXTRACT_DEF_ID"));
						preludeMandatory.setFieldName((String) mandatoryData.get("FIELD"));
						preludeMandatory.setFormName((String) mandatoryData.get("FORM"));
						preludeMandatory.setLabel((String) mandatoryData.get("WEARABLE_FIELD_NAME"));
						preludeMandatory.setPreludeGroup((String) mandatoryData.get("EXTRACT_GROUP"));
						preludeMandatoryList.add(preludeMandatory);
					});
				}

				if (key.equals(SQLConstants.RESULT_SET_4)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					ActivityFactorConfig activityFactorConfig = new ActivityFactorConfig();
					list.forEach(activityFactorData -> {
						activityFactorConfig.setGoogleSheetUrl((String) activityFactorData.get("URL"));
						activityFactorConfig.setStartDate(activityFactorData.get("START_DATE") == null ? null
								: activityFactorData.get("START_DATE").toString());
						activityFactorConfig.setEndDate(activityFactorData.get("END_DATE") == null ? null
								: activityFactorData.get("END_DATE").toString());
						activityFactorConfig.setHasPrelude((Integer) activityFactorData.get("HAS_PRELUDE"));
					});
					study.setActivityFactorConfig(activityFactorConfig);
				}
			}
			study.setPreludeMandatory(preludeMandatoryList);
			study.setNotes(notes);
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error("error while fetching getStudyById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return study;
	}

	@Override
	public void deleteStudy(int studyId, int modifiedBy) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_DELETE, inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("deleteStudy service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.STUDY_CANNOT_DELETE_ACTIVE)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deletePlan ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void associateQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_questionnaire_id", questionnaireAssociated.getQuestionnaireId());
		inputParams.put("p_start_date", questionnaireAssociated.getStartDate());
		inputParams.put("p_end_date", questionnaireAssociated.getEndDate());
		inputParams.put("p_created_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_ASSOCIATE_QUESTIONNAIRE,
					inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Questionnaire has been associated successfully, Study id is ", studyId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"associateQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_ALREADY_MAPPED_TO_STUDY,
									questionnaireAssociated.getQuestionnaireName())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing associateQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateStudyQuestionnaire(int studyId, QuestionnaireAssociated questionnaireAssociated, Integer userId)
			throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_questionnaire_id", questionnaireAssociated.getQuestionnaireId());
		inputParams.put("p_start_date", questionnaireAssociated.getStartDate());
		inputParams.put("p_end_date", questionnaireAssociated.getEndDate());
		inputParams.put("p_modofied_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_UPDATE_QUESTIONNAIRE, inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Questinnaire mapping details has been updated successfully, Study id is ", studyId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateSudyQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.INVALID_STUDY_QUESTIONNAIRE_ASSOCAITION)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing updateSudyQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public void disassociateQuestionnaire(int studyId, int questionnaireId, Integer userId)
			throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_questionnaire_id", questionnaireId);
		inputParams.put("p_modified_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_DISASSOCIATE_QUESTIONNAIRE,
					inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Questionnaire has been disassociated with Study successfully");
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"disassociateQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.INVALID_STUDY_QUESTIONNAIRE_ASSOCAITION)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing disassociateQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<QuestionnaireListDTO> getAssociatedQuestionnaires(int studyId) throws ServiceExecutionException {
		List<QuestionnaireListDTO> questionnaireList = new ArrayList<>();
		LOGGER.debug("getAssociatedQuestionnaires called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ASSOCIATED_QUESTIONNAIRES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireListDTO questionnaireListDTO = new QuestionnaireListDTO();
					questionnaireListDTO.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					questionnaireListDTO.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					questionnaireListDTO.setStartDate(rs.getDate("START_DATE").toLocalDate());
					questionnaireListDTO.setEndDate(rs.getDate("END_DATE").toLocalDate());
					questionnaireListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					questionnaireList.add(questionnaireListDTO);
				}
			}, studyId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getAssociatedQuestionnaires", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireList;
	}

	/*---------------------- STUDY NOTIFICATIONS SERVICES -----------------------------------*/

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getStudyNotificationCount(BaseFilter filter) throws ServiceExecutionException {
		int totalCount = NumberUtils.INTEGER_ZERO;
		String counts;
		HashMap<String, Integer> map = new HashMap<>();
		LOGGER.debug("getgetStudyNotificationCount called");
		try {
			counts = selectForObject(SQLConstants.STUDY_NOTIFICATION_GET_LIST_COUNT, String.class,
					filter.getSearchText(), filter.getStatusId(), filter.getUserId(), filter.getRoleTypeId());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getgetStudyNotificationCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<StudyNotification> getStudyNotifications(BaseFilter filter) {
		List<StudyNotification> studyNotifications = new ArrayList<>();
		LOGGER.debug("getStudyNotifications called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_NOTIFICATION_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					StudyNotification studyNotification = new StudyNotification();
					studyNotification.setSlNumber(rs.getInt("slNo"));
					studyNotification.setStudyId(rs.getInt("STUDY_ID"));
					studyNotification.setStudyName(rs.getString("STUDY_NAME"));
					studyNotification.setIsNotificationEnable(rs.getBoolean("NOTIFICATIONS_ENABLED"));
					studyNotification.setIsActive(rs.getBoolean("IS_ACTIVE"));
					studyNotifications.add(studyNotification);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getStatusId(), filter.getUserId(), filter.getRoleTypeId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getStudyNotifications", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyNotifications;
	}

	@Override
	public void updateStudyNotificationStatus(StudyNotificationRequest studyNotificationRequest, int modifiedBy)
			throws ServiceExecutionException {

		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_study_ids",
					new ObjectMapper().writeValueAsString(studyNotificationRequest.getStudyNotificationStatusList()));
			inputParams.put("p_modified_by", modifiedBy);

			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_NOTIFICATION_UPDATE_STATUS,
					inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing updateStudyNotificationStatus ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	/* ----------- PRIVATE METHODS --------------- */
	private List<PlanSubscribed> getPlanSubscribedList(String plansSubscribed) {
		List<String> plansSubscribedStr = plansSubscribed == null ? new ArrayList<String>()
				: Arrays.asList(plansSubscribed.split(StringLiterals.SEPERATOR.getCode(), -1));
		List<PlanSubscribed> plansSubscribedList = new ArrayList<>();

		for (String planSubsStr : plansSubscribedStr) {

			List<String> plansSubsList = Arrays
					.asList(planSubsStr.toString().split(StringLiterals.SEPERATOR_KEYS.getCode(), -1));

			PlanSubscribed planSubscribed = new PlanSubscribed();
			planSubscribed.setPlanId(Integer.valueOf(plansSubsList.get(NumberUtils.INTEGER_ZERO)));
			planSubscribed.setSubscribedDate(LocalDate.parse(plansSubsList.get(NumberUtils.INTEGER_ONE),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			planSubscribed.setPlanName(plansSubsList.get(NumberUtils.INTEGER_TWO));
			plansSubscribedList.add(planSubscribed);

		}
		return plansSubscribedList;
	}

	private List<QuestionnaireAssociated> getAssociatedQuestionnaireList(String studyQuestionnaires) {
		List<String> associatedQuestionnaireStr = studyQuestionnaires == null ? new ArrayList<String>()
				: Arrays.asList(studyQuestionnaires.split(StringLiterals.SEPERATOR.getCode(), -1));

		List<QuestionnaireAssociated> associatedQuestionnaireList = new ArrayList<>();

		for (String questionnaireDtlsStr : associatedQuestionnaireStr) {

			List<String> questionnaireDtlsList = Arrays
					.asList(questionnaireDtlsStr.split(StringLiterals.SEPERATOR_KEYS.getCode(), -1));

			QuestionnaireAssociated questionnaireAssociated = new QuestionnaireAssociated();
			
			questionnaireAssociated
					.setQuestionnaireId(Integer.valueOf(questionnaireDtlsList.get(NumberUtils.INTEGER_ZERO)));
			questionnaireAssociated.setQuestionnaireName(questionnaireDtlsList.get(NumberUtils.INTEGER_ONE));
			questionnaireAssociated.setStartDate(questionnaireDtlsList.get(NumberUtils.INTEGER_TWO));
			questionnaireAssociated.setEndDate(questionnaireDtlsList.get(Constants.APP_INDEX_THREE));
			questionnaireAssociated
					.setOccuranceId(Integer.parseInt(questionnaireDtlsList.get(Constants.APP_INDEX_FOUR)));
			questionnaireAssociated
					.setFrequencyId(Integer.parseInt(questionnaireDtlsList.get(Constants.APP_INDEX_FIVE)));		

			questionnaireAssociated
					.setOccurance(questionnaireDtlsList.get(Constants.APP_INDEX_SIX));
			questionnaireAssociated
					.setFrequency(questionnaireDtlsList.get(Constants.APP_INDEX_SEVEN));
						
			associatedQuestionnaireList.add(questionnaireAssociated);

		}
		return associatedQuestionnaireList;
	}

	@Override
	public List<StudyListDTO> getStudyListByUser(int userId) throws ServiceExecutionException {
		List<StudyListDTO> studyList = new ArrayList<>();
		LOGGER.debug("getStudyList called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ALL_STUDY_LIST_BY_USER, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					StudyListDTO studyListDTO = new StudyListDTO();
					studyListDTO.setStudyId(rs.getInt("STUDY_ID"));
					studyListDTO.setStudyName(rs.getString("STUDY_NAME"));
					studyListDTO.setPrincipleInvestigator(rs.getString("PRINCIPLE_INVESTIGATOR"));
					studyListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					studyListDTO.setStartDate(rs.getString("START_DATE") == null ? "" : rs.getString("START_DATE"));
					studyListDTO.setEndDate(rs.getString("END_DATE") == null ? "" : rs.getString("END_DATE"));
					studyListDTO.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
					studyListDTO.setIsExternal(rs.getInt("IS_EXTERNAL"));
					studyList.add(studyListDTO);
				}
			}, userId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getStudyList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyList;
	}

	@Override
	public List<StudyListDTO> getAllStudyList() throws ServiceExecutionException {
		List<StudyListDTO> studyList = new ArrayList<>();
		LOGGER.debug("getAllStudyList called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ALL_STUDY_LIST_ACTIVE_INACTIVE, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					StudyListDTO studyListDTO = new StudyListDTO();
					studyListDTO.setStudyId(rs.getInt("STUDY_ID"));
					studyListDTO.setStudyName(rs.getString("STUDY_NAME"));
					studyListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));

					studyList.add(studyListDTO);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getAllStudyList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyList;
	}

	@Override
	public List<PreludeListDTO> getPreludeDataList(int studyId) throws ServiceExecutionException {
		List<PreludeListDTO> preludeList = new ArrayList<>();
		LOGGER.debug("getPreludeDataList called");
		try {
			jdbcTemplate.query(SQLConstants.GET_ALL_PRELUDE_DATA, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PreludeListDTO preludeListDTO = new PreludeListDTO();
					preludeListDTO.setCategory(rs.getString("CATEGORY"));
					preludeListDTO.setExternalStudyId(rs.getString("EXT_STUDY_ID"));
					preludeListDTO.setExtractDefId(rs.getInt("EXTRACT_DEF_ID"));
					preludeListDTO.setField(rs.getString("FIELD"));
					preludeListDTO.setForm(rs.getString("FORM"));
					preludeListDTO.setExtractGroup(rs.getString("EXTRACT_GROUP"));
					preludeListDTO.setWearablesFieldName(
							rs.getString("WEARABLE_FIELD_NAME") != null ? rs.getString("WEARABLE_FIELD_NAME") : "");
					preludeList.add(preludeListDTO);
				}
			}, studyId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPreludeDataList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return preludeList;
	}

	private List<PreludeAssociated> getAssociatedPreludeList(String studyPrelude) {
		List<String> associatedPreluideStr = studyPrelude == null ? new ArrayList<String>()
				: Arrays.asList(studyPrelude.split(StringLiterals.SEPERATOR.getCode(), -1));

		List<PreludeAssociated> associatedPreluideList = new ArrayList<>();

		for (String associatedPreluideDtlsStr : associatedPreluideStr) {

			List<String> associatedPreluideDtlsList = Arrays
					.asList(associatedPreluideDtlsStr.split(StringLiterals.SEPERATOR_KEYS.getCode(), -1));

			PreludeAssociated preludeAssociated = new PreludeAssociated();
			preludeAssociated.setFormName(associatedPreluideDtlsList.get(NumberUtils.INTEGER_ZERO));
			preludeAssociated.setCategory(associatedPreluideDtlsList.get(NumberUtils.INTEGER_ONE));
			preludeAssociated.setPreludeGroup(associatedPreluideDtlsList.get(NumberUtils.INTEGER_TWO));
			preludeAssociated.setFieldName(associatedPreluideDtlsList.get((Integer.valueOf(3))));
			preludeAssociated.setExternalStudyId(associatedPreluideDtlsList.get((Integer.valueOf(4))));
			preludeAssociated.setExtractDefId(Integer.valueOf(associatedPreluideDtlsList.get((Integer.valueOf(5)))));
			associatedPreluideList.add(preludeAssociated);

		}
		return associatedPreluideList;
	}

	@Override
	public List<PreludeDataByStudyDTO> getPreludeDataByStudy(String studyName) throws ServiceExecutionException {
		List<PreludeDataByStudyDTO> preludeList = new ArrayList<>();
		LOGGER.debug("getPreludeDataList called");
		try {
			jdbcTemplate.query(SQLConstants.GET_PRELUDE_DATA_BY_STUDY, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PreludeDataByStudyDTO preludeDataByStudyDTO = new PreludeDataByStudyDTO();
					String fields = rs.getString("FIELDS");
					if (!fields.isEmpty() && fields != null) {
						String[] fieldList = fields.split(",");
						if (fieldList.length > 0 && fieldList[0] != null) {
							preludeDataByStudyDTO.setPatientId(fieldList[0]);
						}
						if (fieldList.length > 1 && fieldList[1] != null) {
							preludeDataByStudyDTO.setOwnerLastName(fieldList[1]);
						}
						if (fieldList.length > 2 && fieldList[2] != null) {
							preludeDataByStudyDTO.setOwnerEmail(fieldList[2]);
						}
						if (fieldList.length > 3 && fieldList[3] != null) {
							preludeDataByStudyDTO.setPetName(fieldList[3]);
						}
					}
					preludeList.add(preludeDataByStudyDTO);
				}
			}, studyName);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPreludeDataList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return preludeList;
	}

	/*********** STUDY PUSH NOTIFICATIONS ******************/

	@Override
	public void associatePushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_push_notification_id", pushNotificationsAssociated.getNotificationId());
		inputParams.put("p_start_date", pushNotificationsAssociated.getStartDate());
		inputParams.put("p_end_date", pushNotificationsAssociated.getEndDate());
		inputParams.put("p_display_time", pushNotificationsAssociated.getDisplayTime()); // TIME CHECK
		inputParams.put("p_frequency", pushNotificationsAssociated.getFrequency());
		inputParams.put("p_created_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_ASSOCIATE_PUSH_NOTIFICATION,
					inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Study Push Notification has been associated successfully, Study id is ", studyId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"associatePushNotifications service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(
									new WearablesError(WearablesErrorCode.PUSH_NOTIFICATION_ALREADY_MAPPED_TO_STUDY,
											pushNotificationsAssociated.getNotificationName())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing associatePushNotifications ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateStudyPushNotifications(int studyId, PushNotificationsAssociated pushNotificationsAssociated,
			Integer userId) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_push_notification_id", pushNotificationsAssociated.getNotificationId());
		inputParams.put("p_start_date", pushNotificationsAssociated.getStartDate());
		inputParams.put("p_end_date", pushNotificationsAssociated.getEndDate());
		inputParams.put("p_display_time", pushNotificationsAssociated.getDisplayTime()); // TIME CHECK
		inputParams.put("p_frequency", pushNotificationsAssociated.getFrequency());
		inputParams.put("p_modofied_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_UPDATE_PUSH_NOTIFICATION,
					inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Questinnaire mapping details has been updated successfully, Study id is ", studyId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateStudyPushNotifications service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(new WearablesError(
									WearablesErrorCode.INVALID_STUDY_PUSH_NOTIFICATION_ASSOCAITION)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing updateStudyPushNotifications ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public List<PushNotification> getAssociatedPushNotifications(int studyId) throws ServiceExecutionException {
		List<PushNotification> pushNotificationList = new ArrayList<>();
		LOGGER.debug("getAssociatedPushNotifications called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ASSOCIATED_PUSH_NOTIFICATIONS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PushNotification studyPushNotification = new PushNotification();
					studyPushNotification.setNotificationId(rs.getInt("PUSH_NOTIFICATION_ID"));
					studyPushNotification.setNotificationName(rs.getString("NOTIFICATION_NAME"));
					/*
					 * studyPushNotification.setStartDate(rs.getDate("START_DATE").toLocalDate());
					 * studyPushNotification.setEndDate(rs.getDate("END_DATE").toLocalDate()); TODO
					 * CHECK
					 */
					studyPushNotification
							.setStartDate(rs.getString("START_DATE") == null ? "" : rs.getString("START_DATE"));
					studyPushNotification.setEndDate(rs.getString("END_DATE") == null ? "" : rs.getString("END_DATE"));
					studyPushNotification.setDisplayTime(rs.getString("DISPLAY_TIME"));
					studyPushNotification.setFrequency(rs.getString("FREQUENCY"));
					studyPushNotification.setIsActive(rs.getBoolean("IS_ACTIVE"));
					pushNotificationList.add(studyPushNotification);
				}
			}, studyId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getAssociatedPushNotifications", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return pushNotificationList;
	}

	@Override
	public void disassociateStudyPushNotifications(int studyId, int notificationId, Integer userId)
			throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_push_notification_id", notificationId);
		inputParams.put("p_modified_by", userId);
		try {
			LOGGER.info("inputParams are {}", inputParams);
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.STUDY_DISASSOCIATE_PUSH_NOTIFICATION,
					inputParams);
			LOGGER.info("outParams are {}", outParams);

			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Questionnaire has been disassociated with Study successfully");
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"disassociateStudyPushNotifications service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(new WearablesError(
									WearablesErrorCode.INVALID_STUDY_PUSH_NOTIFICATION_ASSOCAITION)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing disassociateStudyPushNotifications ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<StudyImageScale> associatedImageScales(int studyId) throws ServiceExecutionException {
		List<StudyImageScale> studyImageScaleList = new ArrayList<>();
		LOGGER.debug("associatedImageScales called");
		try {
			jdbcTemplate.query(SQLConstants.STUDY_GET_ASSOCIATED_PUSH_NOTIFICATIONS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
//					StudyImageScale studyImageScale = new StudyImageScale();
//					studyImageScale.setNotificationId(rs.getInt("PUSH_NOTIFICATION_ID"));
//					studyImageScale.setNotificationName(rs.getString("NOTIFICATION_NAME"));
//					studyImageScale
//							.setStartDate(rs.getString("START_DATE") == null ? "" : rs.getString("START_DATE"));
//					studyImageScale.setEndDate(rs.getString("END_DATE") == null ? "" : rs.getString("END_DATE"));
//					studyImageScale.setFrequency(rs.getString("FREQUENCY_NAME"));
//					studyImageScale.setFrequencyId(Integer.praseInt(rs.getString("FREQUENCY")));
//					studyImageScale.setIsActive(rs.getBoolean("IS_ACTIVE"));
//					pushNotificationList.add(studyPushNotification);
				}
			}, studyId);

		} catch (Exception e) {
			LOGGER.error("error while fetching associatedImageScales", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return studyImageScaleList;
	}

	private List<PushNotificationsAssociated> getAssociatedPushNotificationList(String studyPushNotifications) {
		List<String> associatedPushNotificationsStr = studyPushNotifications == null ? new ArrayList<String>()
				: Arrays.asList(studyPushNotifications.split(StringLiterals.SEPERATOR.getCode(), -1));

		List<PushNotificationsAssociated> pushNotificationsAssociatedList = new ArrayList<>();

		for (String pushNotificationsDtlsStr : associatedPushNotificationsStr) {

			List<String> pushNotificationDtlsList = Arrays
					.asList(pushNotificationsDtlsStr.split(StringLiterals.SEPERATOR_KEYS.getCode(), -1));

			PushNotificationsAssociated pushNotificationsAssociated = new PushNotificationsAssociated();
			pushNotificationsAssociated
					.setNotificationId(Integer.valueOf(pushNotificationDtlsList.get(Constants.APP_INDEX_ZERO)));
			pushNotificationsAssociated.setNotificationName(pushNotificationDtlsList.get(Constants.APP_INDEX_ONE));
			pushNotificationsAssociated
					.setStartDate(LocalDate.parse(pushNotificationDtlsList.get(Constants.APP_INDEX_TWO)));
			pushNotificationsAssociated
					.setEndDate(LocalDate.parse(pushNotificationDtlsList.get((Constants.APP_INDEX_THREE))));

			pushNotificationsAssociated.setDisplayTime(pushNotificationDtlsList.get(Constants.APP_INDEX_FOUR));
			pushNotificationsAssociated.setFrequency(pushNotificationDtlsList.get(Constants.APP_INDEX_FIVE));

			pushNotificationsAssociatedList.add(pushNotificationsAssociated);
		}
		return pushNotificationsAssociatedList;
	}

	private List<ImageScoringScalesAssociated> getAssociatedImageScaleList(String studyImageScales) {
		List<String> associatedImageScalesStr = studyImageScales == null ? new ArrayList<String>()
				: Arrays.asList(studyImageScales.split(StringLiterals.SEPERATOR.getCode(), -1));

		List<ImageScoringScalesAssociated> imageScalesAssociatedList = new ArrayList<>();

		for (String imageSclesDtlsStr : associatedImageScalesStr) {

			List<String> ImageScleDtlsList = Arrays
					.asList(imageSclesDtlsStr.split(StringLiterals.SEPERATOR_KEYS.getCode(), -1));

			ImageScoringScalesAssociated imageSclesAssociated = new ImageScoringScalesAssociated();
			imageSclesAssociated.setImageScoringId(Integer.valueOf(ImageScleDtlsList.get(Constants.APP_INDEX_ZERO)));
			imageSclesAssociated.setImageScaleName(ImageScleDtlsList.get(Constants.APP_INDEX_ONE));
			imageSclesAssociated.setStartDate(ImageScleDtlsList.get(Constants.APP_INDEX_TWO));
			imageSclesAssociated.setEndDate(ImageScleDtlsList.get(Constants.APP_INDEX_THREE));
			imageSclesAssociated.setFrequencyId(Integer.parseInt(ImageScleDtlsList.get(Constants.APP_INDEX_FOUR)));
			imageSclesAssociated.setFrequencyName(ImageScleDtlsList.get(Constants.APP_INDEX_FIVE));
			imageScalesAssociatedList.add(imageSclesAssociated);
		}
		return imageScalesAssociatedList;
	}

	@Override
	public List<PreludeListDTO> getAFPreludeDataList(int studyId) throws ServiceExecutionException {
		List<PreludeListDTO> preludeList = new ArrayList<>();
		LOGGER.debug("getAFPreludeDataList called");
		try {
			jdbcTemplate.query(SQLConstants.GET_ALL_PRELUDE_DATA, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PreludeListDTO preludeListDTO = new PreludeListDTO();
					preludeListDTO.setCategory(rs.getString("CATEGORY"));
					preludeListDTO.setExternalStudyId(rs.getString("EXT_STUDY_ID"));
					preludeListDTO.setExtractDefId(rs.getInt("EXTRACT_DEF_ID"));
					preludeListDTO.setField(rs.getString("FIELD"));
					preludeListDTO.setForm(rs.getString("FORM"));
					preludeListDTO.setExtractGroup(rs.getString("EXTRACT_GROUP"));
					preludeListDTO.setWearablesFieldName(
							rs.getString("WEARABLE_FIELD_NAME") != null ? rs.getString("WEARABLE_FIELD_NAME") : "");
					preludeList.add(preludeListDTO);
				}
			}, studyId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getAFPreludeDataList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return preludeList;
	}

	@Override
	public List<PreludeDataByStudyDTO> getAFPreludeDataByStudy(String studyName) throws ServiceExecutionException {
		List<PreludeDataByStudyDTO> preludeList = new ArrayList<>();
		LOGGER.debug("getAFPreludeDataByStudy called");
		try {
			jdbcTemplate.query(SQLConstants.GET_AF_PRELUDE_DATA_BY_STUDY, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PreludeDataByStudyDTO preludeDataByStudyDTO = new PreludeDataByStudyDTO();
					String fields = rs.getString("FIELDS");
					if (!fields.isEmpty() && fields != null) {
						String[] fieldList = fields.split(",");
						if (fieldList.length > 0 && fieldList[0] != null) {
							preludeDataByStudyDTO.setPatientId(fieldList[0]);
						}
						if (fieldList.length > 1 && fieldList[1] != null) {
							preludeDataByStudyDTO.setOwnerLastName(fieldList[1]);
						}
						if (fieldList.length > 2 && fieldList[2] != null) {
							preludeDataByStudyDTO.setOwnerEmail(fieldList[2]);
						}
						if (fieldList.length > 3 && fieldList[3] != null) {
							preludeDataByStudyDTO.setPetName(fieldList[3]);
						}
					}
					preludeList.add(preludeDataByStudyDTO);
				}
			}, studyName);
		} catch (Exception e) {
			LOGGER.error("error while fetching getAFPreludeDataByStudy", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return preludeList;
	}

	/*
	 * public static void main(String[] args) throws ExecutionException,
	 * InterruptedException, TimeoutException { StudyDaoImpl dao = new
	 * StudyDaoImpl(); dao.postDataToEndpointAysnc(
	 * "https://us-central1-ct-wearables-portal-pf.cloudfunctions.net/WP-CF-GetPreludeFields",
	 * "6072", "UAT"); }
	 */

	/*
	 * private List<PreludeMandatory> getMandatoryPreludeList(String studyPrelude) {
	 * List<String> associatedPreluideStr = studyPrelude == null ? new
	 * ArrayList<String>() : Arrays.asList(studyPrelude.split(",", -1));
	 * 
	 * List<PreludeMandatory> associatedPreluideList = new ArrayList<>();
	 * 
	 * for (String associatedPreluideDtlsStr : associatedPreluideStr) {
	 * 
	 * List<String> associatedPreluideDtlsList =
	 * Arrays.asList(associatedPreluideDtlsStr.split("#", -1));
	 * 
	 * PreludeMandatory preludeAssociated = new PreludeMandatory();
	 * preludeAssociated
	 * .setFormName(associatedPreluideDtlsList.get(NumberUtils.INTEGER_ZERO));
	 * preludeAssociated.setCategory(associatedPreluideDtlsList.get(NumberUtils.
	 * INTEGER_ONE));
	 * preludeAssociated.setPreludeGroup(associatedPreluideDtlsList.get(NumberUtils.
	 * INTEGER_TWO));
	 * preludeAssociated.setFieldName(associatedPreluideDtlsList.get((Integer.
	 * valueOf(3))));
	 * preludeAssociated.setExternalStudyId(associatedPreluideDtlsList.get((Integer.
	 * valueOf(4))));
	 * preludeAssociated.setExtractDefId(Integer.valueOf(associatedPreluideDtlsList.
	 * get((Integer.valueOf(5))))); associatedPreluideList.add(preludeAssociated);
	 * 
	 * } return associatedPreluideList; }
	 */

}