package com.hillspet.wearables.dao.pet.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.constants.SQLConstants;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.common.utils.GCStorageUtil;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.pet.PetDao;
import com.hillspet.wearables.dto.ExternalPetInfoListDTO;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
import com.hillspet.wearables.dto.PetCampaignPointsListDTO;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetDevice;
import com.hillspet.wearables.dto.PetExternalInfoListDTO;
import com.hillspet.wearables.dto.PetFeedingEnthusiasmScale;
import com.hillspet.wearables.dto.PetImageScale;
import com.hillspet.wearables.dto.PetListDTO;
import com.hillspet.wearables.dto.PetNote;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.dto.PetObservationMediaListDTO;
import com.hillspet.wearables.dto.PetParentDTO;
import com.hillspet.wearables.dto.PetParentListDTO;
import com.hillspet.wearables.dto.PetRedemptionHistoryDTO;
import com.hillspet.wearables.dto.PetStudyDTO;
import com.hillspet.wearables.dto.PetWeightHistoryDTO;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.ExternalPetInfoFilter;
import com.hillspet.wearables.dto.filter.PetActivityFactorFilter;
import com.hillspet.wearables.dto.filter.PetEnthusiasmFilter;
import com.hillspet.wearables.dto.filter.PetFilter;
import com.hillspet.wearables.dto.filter.PetImageScaleFilter;
import com.hillspet.wearables.dto.filter.PetObservationMediaFilter;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.AssociateNewStudyRequest;
import com.hillspet.wearables.request.PetRequest;
import com.hillspet.wearables.response.ActivityFactorResultResponse;
import com.hillspet.wearables.security.Authentication;

@Repository
public class PetDaoImpl extends BaseDaoImpl implements PetDao {

	@Value("${gcp.env}")
	private String environment;

	@Value("${gcp.bucketName}")
	private String bucketName;

	@Autowired
	private GCStorageUtil gcStorageUtil;

	@Autowired
	private GCPClientUtil gcpClientUtil;

	@Autowired
	private Authentication authentication;

	private static final Logger LOGGER = LogManager.getLogger(PetDaoImpl.class);

	@Override
	public String getPetsCount(PetFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetsCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_LIST_COUNT, String.class, filter.getSearchText(),
					filter.getStatusId(), filter.getStudyId(), filter.getUserId(), filter.getRoleTypeId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetListDTO> getPetList(PetFilter filter) throws ServiceExecutionException {
		List<PetListDTO> petList = new ArrayList<>();
		LOGGER.debug("getPetList called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetListDTO petListDTO = new PetListDTO();
					petListDTO.setSlNumber(rs.getInt("slNo"));
					petListDTO.setPetId(rs.getInt("PET_ID"));
					petListDTO.setPetName(rs.getString("PET_NAME"));

					petListDTO.setPetPhoto(rs.getString("PHOTO_NAME"));
					String fileName = rs.getString("PHOTO_NAME");

					if (fileName != null && !fileName.trim().equals("")) {
						petListDTO.setPetPhotoUrl(
								gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
					}

					petListDTO.setBreedName(rs.getString("BREED_NAME"));
					petListDTO.setStudyId(rs.getInt("STUDY_ID"));
					petListDTO.setStudyName(rs.getString("STUDY_NAME"));
					petListDTO.setSensorDetails(rs.getString("sensorDetails"));
					petListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					petListDTO.setPetStatusId(rs.getInt("PET_STATUS_ID"));
					petListDTO.setPetStatus(rs.getString("STATUS_NAME"));
					petList.add(petListDTO);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(),
					filter.getSearchText().trim(), filter.getStatusId(), filter.getStudyId(), filter.getUserId(),
					filter.getRoleTypeId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@Override
	public List<PetListDTO> getPets() throws ServiceExecutionException {
		List<PetListDTO> petList = new ArrayList<>();
		int userId = authentication.getAuthUserDetails().getUserId();
		LOGGER.debug("getPets called " + userId);
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_ALL_PETS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetListDTO petListDTO = new PetListDTO();
					petListDTO.setPetId(rs.getInt("PET_ID"));
					petListDTO.setPetName(rs.getString("PET_NAME"));
					petListDTO.setPetPhoto(rs.getString("PHOTO_NAME"));
					String fileName = rs.getString("PHOTO_NAME");

					if (fileName != null && !fileName.trim().equals("")) {
						petListDTO.setPetPhotoUrl(
								gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
					}

					petListDTO.setBreedName(rs.getString("BREED_NAME"));
					petListDTO.setGender(rs.getString("GENDER"));
					petListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					petList.add(petListDTO);
				}
			}, userId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPets", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@Override
	public PetDTO addPet(PetRequest petRequest) throws ServiceExecutionException {
		PetDTO petDTO = new PetDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_name", new ObjectMapper().writeValueAsString(petRequest.getPetName()));
			inputParams.put("p_breed_id", petRequest.getBreedId());
			inputParams.put("p_weight", petRequest.getWeight());
			inputParams.put("p_weight_unit", petRequest.getWeightUnit());
			inputParams.put("p_gender", petRequest.getGender());
			inputParams.put("p_dob", petRequest.getDateOfBirth());
			inputParams.put("p_image", petRequest.getPetImage());
			inputParams.put("p_neutered", petRequest.getIsNeutered());
			inputParams.put("p_status_id", petRequest.getPetStatusId());
			inputParams.put("p_pet_devices", new ObjectMapper().writeValueAsString(petRequest.getPetDevices()));
			inputParams.put("p_pet_parents", new ObjectMapper().writeValueAsString(petRequest.getPetParents()));

			inputParams.put("p_created_by", petRequest.getUserId());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_INSERT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer petId = (int) outParams.get("last_insert_id");
				petRequest.setPetId(petId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"addPet device assignment validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_ALREADY_MAPPED, errorMsg)));
				} else {
					if (statusFlag == -3) {
						throw new ServiceExecutionException(
								"AddPetParent service validation failed cannot proceed further",
								Status.BAD_REQUEST.getStatusCode(),
								Arrays.asList(new WearablesError(WearablesErrorCode.PET_PARENT_EMAIL_ALREADY_EXISTS,
										errorMsg)));
					} else {
						throw new ServiceExecutionException(errorMsg);
					}
				}
			}
			BeanUtils.copyProperties(petRequest, petDTO);
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing addPet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public PetDTO updatePet(PetRequest petRequest) throws ServiceExecutionException {
		PetDTO petDTO = new PetDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", petRequest.getPetId());
			inputParams.put("p_pet_name", new ObjectMapper().writeValueAsString(petRequest.getPetName()));
			inputParams.put("p_breed_id", petRequest.getBreedId());
			/* inputParams.put("p_weight", petRequest.getWeight()); */
			/* inputParams.put("p_weight_unit", petRequest.getWeightUnit()); */
			inputParams.put("p_gender", petRequest.getGender());
			inputParams.put("p_dob", petRequest.getDateOfBirth());
			inputParams.put("p_image", petRequest.getPetImage());
			inputParams.put("p_neutered", petRequest.getIsNeutered());
			inputParams.put("p_status_id", petRequest.getPetStatusId());
			inputParams.put("p_pet_devices", new ObjectMapper().writeValueAsString(petRequest.getPetDevices()));
			inputParams.put("p_pet_parents", new ObjectMapper().writeValueAsString(petRequest.getPetParents()));

			inputParams.put("p_modified_by", petRequest.getUserId());
			inputParams.put("p_removed_study_ids", petRequest.getRemovedStudyIds());
			inputParams.put("p_removed_pet_parent_ids", petRequest.getRemovedPetParents());

			inputParams.put("p_removed_assets",
					new ObjectMapper().writeValueAsString(petRequest.getPetUnAssignAssets()));

			inputParams.put("p_confirm_off_study", petRequest.getConfirmOffStudy());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_UPDATE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updatePet device assignment validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_ALREADY_MAPPED, errorMsg)));
				} else if (statusFlag == -5) {
					throw new ServiceExecutionException("Error occurred.", Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.PET_PARENT_ALREADY_EXISTS, errorMsg)));
				} else {
					if (statusFlag == -3) {
						throw new ServiceExecutionException(
								"AddPetParent service validation failed cannot proceed further",
								Status.BAD_REQUEST.getStatusCode(),
								Arrays.asList(new WearablesError(WearablesErrorCode.PET_PARENT_EMAIL_ALREADY_EXISTS,
										errorMsg)));
					} else {
						throw new ServiceExecutionException(errorMsg);
					}
				}
			}
			BeanUtils.copyProperties(petRequest, petDTO);
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing updatePet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public PetDTO getPetById(int petId) {
		final PetDTO petDTO = new PetDTO();
		List<PetDevice> petDevices = new ArrayList<>();
		List<PetParentDTO> petParents = new ArrayList<>();
		int userId = authentication.getAuthUserDetails().getUserId();
		LOGGER.debug("getPetById called " + userId);
		try {
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);
			inputParams.put("p_user_id", userId);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(SQLConstants.PET_GET_BY_ID, inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(SQLConstants.RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(pet -> {
						petDTO.setPetId((Integer) pet.get("PET_ID"));
						petDTO.setPetName((String) pet.get("PET_NAME"));
						petDTO.setPhotoName((String) pet.get("PHOTO_NAME"));
						String fileName = (String) pet.get("PHOTO_NAME");
						if (fileName != null && !fileName.trim().equals("")) {
							petDTO.setPetPhotoUrl(
									gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
						}
						if (pet.get("BREED_ID") != null) {
							petDTO.setBreedId((Integer) pet.get("BREED_ID"));
						}
						petDTO.setBreedName((String) pet.get("BREED_NAME"));
						petDTO.setSpeciesId((Integer) pet.get("SPECIES_ID"));
						petDTO.setSpeciesName((String) pet.get("SPECIES_NAME"));
						petDTO.setFeedingPreferences((String) pet.get("FEEDING_PREFERENCES"));
						petDTO.setGender((String) pet.get("GENDER"));
						if (pet.get("BIRTHDAY") != null) {
							// Timestamp birthday = (Timestamp) pet.get("BIRTHDAY");
							LocalDateTime birthday = (LocalDateTime) pet.get("BIRTHDAY");
							petDTO.setDateOfBirth(birthday != null ? birthday.toLocalDate() : null);
						}
						petDTO.setIsDobUnknown((Boolean) pet.get("IS_UNKNOWN"));

						if (pet.get("WEIGHT") != null) {
							BigDecimal weight = (BigDecimal) pet.get("WEIGHT");
							petDTO.setWeight(weight != null ? weight.doubleValue() : 0);
						}
						petDTO.setWeightUnit((String) pet.get("WEIGHT_UNIT"));
						petDTO.setIsNeutered((Boolean) pet.get("IS_NEUTERED"));
						petDTO.setIsDeceased((Boolean) pet.get("IS_DECEASED"));
						petDTO.setIsMixed((Boolean) pet.get("IS_MIXED"));
						petDTO.setPetStatusId((Integer) pet.get("PET_STATUS_ID"));
						petDTO.setIsActive((Boolean) pet.get("IS_ACTIVE"));

						// Timestamp timestamp = (Timestamp) pet.get("MODIFIED_DATE");
						petDTO.setModifiedDate((LocalDateTime) pet.get("MODIFIED_DATE"));
					});
				}

				if (key.equals(SQLConstants.RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(parent -> {
						PetParentDTO petParentDTO = new PetParentDTO();
						petParentDTO.setPetParentId((Integer) parent.get("PET_PARENT_ID"));
						petParentDTO.setPetParentName((String) parent.get("FULL_NAME"));
						petParentDTO.setFirstName((String) parent.get("FIRST_NAME"));
						petParentDTO.setLastName((String) parent.get("LAST_NAME"));
						petParentDTO.setEmail((String) parent.get("EMAIL"));
						petParentDTO.setPhoneNumber((String) parent.get("PHONE_NUMBER"));
						petParentDTO.setShippingAddress((String) parent.get("SHIPPING_ADDRESS"));
						petParents.add(petParentDTO);
					});
				}

				if (key.equals(SQLConstants.RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(device -> {
						PetDevice petDevice = new PetDevice();
						petDevice.setStudyId((Integer) device.get("STUDY_ID"));
						petDevice.setStudyName((String) device.get("STUDY_NAME"));
						petDevice.setStudyDescription((String) device.get("STUDY_DESCRIPTION"));
						petDevice.setDeviceId((Integer) device.get("DEVICE_ID"));
						petDevice.setDeviceNumber((String) device.get("DEVICE_NUMBER"));
						petDevice.setDeviceModel((String) device.get("DEVICE_MODEL"));

						if (device.get("ASSIGN_DATE") != null) {
							LocalDateTime assignedOn = (LocalDateTime) device.get("ASSIGN_DATE");
							petDevice.setAssignedOn(assignedOn != null ? assignedOn.toLocalDate() : null);
						}

						if (device.get("START_DATE") != null) {
							LocalDateTime startDate = (LocalDateTime) device.get("START_DATE");
							petDevice.setStudyAssignedOn(startDate != null ? startDate.toLocalDate() : null);
						}

						if (device.get("END_DATE") != null) {
							LocalDateTime endDate = (LocalDateTime) device.get("END_DATE");
							petDevice.setStudyEndDate(endDate != null ? endDate.toLocalDate() : null);
						}

						Integer petStudyDeviceId = (Integer) device.get("PET_STUDY_DEVICE_ID");
						petDevice.setPetStudyDeviceId(petStudyDeviceId != null ? petStudyDeviceId : 0);
						if (device.get("DEVICE_TYPE") != null) {
							petDevice.setDeviceType((String) device.get("DEVICE_TYPE"));
						}
						if (device.get("PET_STUDY_ID") != null) {
							petDevice.setPetStudyId((Integer) device.get("PET_STUDY_ID"));
						}
						if (device.get("UN_ASSIGN_DATE") != null) {
							Date dateUnAssign = (Date) device.get("UN_ASSIGN_DATE");
							petDevice.setUnAssignedOn(dateUnAssign != null ? dateUnAssign : null);
							petDevice.setUnAssignReasonId((Integer) device.get("REASON_ID"));
						}

						Integer isStudyActive = (Integer) device.get("IS_ACTIVE");
						if (isStudyActive != null && isStudyActive != 0) {
							petDevice.setStudyActive(true);
						} else {
							petDevice.setStudyActive(false);
						}
						if (device.get("EXT_PET_ID") != null) {
							petDevice.setExternalPetInfoId(device.get("EXT_PET_ID") + "");
							petDevice.setExternalPetValue(device.get("EXT_PET_VALUE") + "");
							petDevice.setExternal(true);
						} else {
							petDevice.setExternal(false);
						}

						petDevices.add(petDevice);
					});
				}

			}
			petDTO.setPetParents(petParents);
			petDTO.setPetDevices(petDevices);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public PetDTO getPetByIdAndStudyId(int petId, int studyId) {
		final PetDTO petDTO = new PetDTO();
		LOGGER.debug("getPetByIdAndStudyId called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_BY_ID_AND_STUDY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					petDTO.setPetId(rs.getInt("PET_ID"));
					petDTO.setPetName(rs.getString("PET_NAME"));
					petDTO.setPhotoName(rs.getString("PHOTO_NAME"));
					String fileName = rs.getString("PHOTO_NAME");
					if (fileName != null && !fileName.trim().equals("")) {
						petDTO.setPetPhotoUrl(gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
					}
					petDTO.setBreedId(rs.getInt("BREED_ID"));
					petDTO.setBreedName(rs.getString("BREED_NAME"));
					petDTO.setGender(rs.getString("GENDER"));
					petDTO.setDateOfBirth(rs.getDate("BIRTHDAY") == null ? null : rs.getDate("BIRTHDAY").toLocalDate());
					petDTO.setPetStatusId(rs.getInt("PET_STATUS_ID"));
					petDTO.setPetStatus(rs.getString("STATUS_NAME"));

					petDTO.setWeight(rs.getBigDecimal("WEIGHT").doubleValue());
					petDTO.setWeightUnit(rs.getString("WEIGHT_UNIT"));
					petDTO.setIsNeutered(rs.getBoolean("IS_NEUTERED"));
					petDTO.setIsDeceased(rs.getBoolean("IS_DECEASED"));
					petDTO.setIsMixed(rs.getBoolean("IS_MIXED"));
					petDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));

					petDTO.setStudyId(rs.getInt("STUDY_ID"));
					petDTO.setStudyName(rs.getString("STUDY_NAME"));
					petDTO.setStartDate(
							rs.getDate("START_DATE") == null ? null : rs.getDate("START_DATE").toLocalDate());
					petDTO.setEndDate(rs.getDate("END_DATE") == null ? null : rs.getDate("END_DATE").toLocalDate());
					petDTO.setModifiedDate(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime());
				}
			}, petId, studyId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetByIdAndStudyId", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public void deletePet(int petId, int modifiedBy) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_pet_id", petId);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_DELETE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				throw new ServiceExecutionException(errorMsg, Status.BAD_REQUEST.getStatusCode(),
						Arrays.asList(new WearablesError(WearablesErrorCode.PET_ON_STUDY, errorMsg)));
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deletePet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<PetListDTO> getPetsByPetParent(int petParentId) throws ServiceExecutionException {
		List<PetListDTO> petList = new ArrayList<>();
		LOGGER.debug("getPetsByPetParent called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_LIST_BY_PET_PARENT, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetListDTO petListDTO = new PetListDTO();
					petListDTO.setPetId(rs.getInt("PET_ID"));
					petListDTO.setPetName(rs.getString("PET_NAME"));
					petList.add(petListDTO);
				}
			}, petParentId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsByPetParent", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@Override
	public Map<String, Integer> getPetObservationsCount(PetObservationMediaFilter filter)
			throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetObservationsCount called");
		HashMap<String, Integer> map = new HashMap<>();
		try {
			totalCount = selectForObject(SQLConstants.FN_GET_PET_OBSERVATIONS_COUNT, String.class, filter.getPetId(),
					filter.getStudyId());
			map = new ObjectMapper().readValue(totalCount, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetObservationsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<PetObservation> getPetObservations(PetObservationMediaFilter filter) throws ServiceExecutionException {
		List<PetObservation> petObservations = new ArrayList<>();
		LOGGER.debug("getPetObservations called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_OBSERVATIONS_BY_PET_AND_STUDY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetObservation petObservation = new PetObservation();
					petObservation.setPetId(rs.getInt("PET_ID"));
					petObservation.setActivityTypeId(rs.getInt("ACTIVITY_TYPE_ID"));
					petObservation.setActivityType(rs.getString("ACTIVITY_TYPE"));
					petObservation.setObservationText(rs.getString("OBS_TEXT"));
					petObservation.setObservationTime(rs.getTimestamp("OBSERVATION_DATE_TIME").toLocalDateTime());

					String imagePath = rs.getString("IMAGE_PATH");
					petObservation.setImagePath(imagePath);

					if (StringUtils.isNotBlank(imagePath)) {
						petObservation.setImageList(
								gcStorageUtil.getMediaSignedUrlList(imagePath, Constants.GCP_OBSERVATION_PHOTO_PATH));
					}

					String videoURL = rs.getString("VIDEO_URL");
					petObservation.setVideoURL(videoURL);

					if (StringUtils.isNotBlank(videoURL)) {
						videoURL = videoURL.replaceAll("https://storage.googleapis.com/" + bucketName + "/"
								+ environment + "/GCloud/WPortal/ObservationVideo/", "");

						petObservation.setVideoList(
								gcStorageUtil.getMediaSignedUrlList(videoURL, Constants.GCP_OBSERVATION_VIDEO_PATH));
					}

					String videoThumbnailURL = rs.getString("VIDEO_THUMBNAIL_URL");
					petObservation.setVideoThumbnailURL(videoThumbnailURL);

					if (StringUtils.isNotBlank(videoThumbnailURL)) {
						videoThumbnailURL = videoThumbnailURL.replaceAll("https://storage.googleapis.com/" + bucketName
								+ "/" + environment + "/GCloud/WPortal/ObservationVideoThumbnail/", "");

						petObservation.setVideoThumbnailList(gcStorageUtil.getMediaSignedUrlList(videoThumbnailURL,
								Constants.GCP_OBSERVATION_VIDEO_THUMBNAIL_PATH));
					}

					petObservations.add(petObservation);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getSortBy(), filter.getOrder(), filter.getPetId(),
					filter.getStudyId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetObservations", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petObservations;
	}

	@Override
	public List<PetParentListDTO> getPetParents(PetFilter filter) throws ServiceExecutionException {
		List<PetParentListDTO> parentDTOs = new ArrayList<>();
		LOGGER.debug("getPetParents called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_PARENTS_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetParentListDTO parentDTO = new PetParentListDTO();
					parentDTO.setPetParentId(rs.getInt("PET_PARENT_ID"));
					parentDTO.setPetParentName(rs.getString("FULL_NAME"));
					parentDTO.setPetParentFirstName(rs.getString("FIRST_NAME"));
					parentDTO.setPetParentLastName(rs.getString("LAST_NAME"));
					parentDTO.setEmail(rs.getString("EMAIL"));
					parentDTO.setPhoneNumber(rs.getString("PHONE_NUMBER"));
					parentDTO.setShippingAddress(rs.getString("SHIPPING_ADDRESS"));
					parentDTOs.add(parentDTO);
				}
			}, filter.getPetId(), filter.getStartIndex(), filter.getLimit(), filter.getSortBy(), filter.getOrder());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParents", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return parentDTOs;
	}

	@Override
	public List<PetNote> getPetNotes(PetFilter filter) throws ServiceExecutionException {
		List<PetNote> petNotes = new ArrayList<>();
		LOGGER.debug("getPetNotes called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_NOTES_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetNote petNote = new PetNote();
					petNote.setPetId(rs.getInt("PET_ID"));
					petNote.setNoteType(rs.getString("NOTE_TYPE"));
					petNote.setContent(rs.getString("CONTENT"));
					petNote.setModifiedDate(rs.getTimestamp("MODIFIED_DATE").toLocalDateTime());
					petNotes.add(petNote);
				}
			}, filter.getPetId(), filter.getStartIndex(), filter.getLimit(), filter.getSortBy(), filter.getOrder());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetNotes", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petNotes;
	}

	@Override
	public List<PetDevice> getPetDevices(int petId) throws ServiceExecutionException {
		List<PetDevice> petDevices = new ArrayList<>();
		LOGGER.debug("getPetDevices called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_DEVICES_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDevice petDevice = new PetDevice();
					petDevice.setPetId(rs.getInt("PET_ID"));
					petDevice.setStudyId(rs.getInt("STUDY_ID"));
					petDevice.setStudyName(rs.getString("STUDY_NAME"));
					petDevice.setStudyDescription(rs.getString("STUDY_DESCRIPTION"));
					petDevice.setDeviceId(rs.getInt("DEVICE_ID"));
					petDevice.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					petDevice.setFirmwareVersion(rs.getString("FIRMWARE_VERSION_NUMBER"));
					petDevice.setLastSync(rs.getString("lastSync"));
					petDevice.setAssignedOn(rs.getTimestamp("ASSIGN_DATE").toLocalDateTime().toLocalDate());
					petDevice.setPetStudyId(rs.getInt("PET_STUDY_ID"));
					petDevices.add(petDevice);
				}
			}, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetDevices", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDevices;
	}

	@Override
	public void addPetNote(PetNote petNote) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_pet_id", petNote.getPetId());
		inputParams.put("p_note_type", petNote.getNoteType());
		inputParams.put("p_notes", petNote.getContent());
		inputParams.put("p_created_by", petNote.getCreatedBy());
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_NOTES_INSERT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Notes has been added successfully to Pet, Id is ", petNote.getPetId());
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("addPetNote service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.PET_NOTE_ALREADY_EXISTS)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing addPetNote ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public Map<String, Integer> getPetsObservatonMediaCount(PetObservationMediaFilter filter)
			throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetsObservatonMediaCount called");
		HashMap<String, Integer> map = new HashMap<>();
		try {
			totalCount = selectForObject(SQLConstants.FN_GET_OBSERVATION_MEDIA_COUNT, String.class,
					filter.getSearchText(), filter.getStatus(), filter.getStudy(), filter.getStartDate(),
					filter.getEndDate());
			map = new ObjectMapper().readValue(totalCount, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsObservatonMediaCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<PetObservationMediaListDTO> getPetObservationMediaList(PetObservationMediaFilter filter)
			throws ServiceExecutionException {
		List<PetObservationMediaListDTO> petObservationMediaList = new ArrayList<>();
		LOGGER.debug("getPetObservationMediaList called");
		try {
			jdbcTemplate.query(SQLConstants.OBSERVATION_MEDIA_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetObservationMediaListDTO petObservationMediaListDTO = new PetObservationMediaListDTO();
					petObservationMediaListDTO.setSlNumber(rs.getInt("slNo"));
					petObservationMediaListDTO.setPetId(rs.getInt("PET_ID"));
					petObservationMediaListDTO.setPetName(rs.getString("PET_NAME"));
					petObservationMediaListDTO.setPhotoName(rs.getString("PHOTO_NAME"));

					String fileName = rs.getString("PHOTO_NAME");

					if (fileName != null && !fileName.trim().equals("")) {
						petObservationMediaListDTO.setPetPhotoUrl(
								gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
					}

					petObservationMediaListDTO.setBreedId(rs.getInt("BREED_ID"));
					petObservationMediaListDTO.setBreedName(rs.getString("BREED_NAME"));
					petObservationMediaListDTO.setPetStatusId(rs.getInt("PET_STATUS_ID"));
					petObservationMediaListDTO.setPetStatus(rs.getString("STATUS_NAME"));
					petObservationMediaListDTO.setStudyNames(rs.getString("STUDY_NAME"));
					String imagePathCSV = rs.getString("IMAGE_PATH");

					petObservationMediaListDTO.setImageList(
							gcStorageUtil.getMediaSignedUrlList(imagePathCSV, Constants.GCP_OBSERVATION_PHOTO_PATH));

					petObservationMediaListDTO.setVideoURL(rs.getString("VIDEO_URL"));
					String videoPathCSV = rs.getString("VIDEO_URL");

					if (StringUtils.isNotBlank(videoPathCSV)) {
						videoPathCSV = videoPathCSV.replaceAll("https://storage.googleapis.com/" + bucketName + "/"
								+ environment + "/GCloud/WPortal/ObservationVideo/", "");
					}

					petObservationMediaListDTO.setVideoList(
							gcStorageUtil.getMediaSignedUrlList(videoPathCSV, Constants.GCP_OBSERVATION_VIDEO_PATH));

					petObservationMediaListDTO.setVideoThumbnailURL(rs.getString("VIDEO_THUMBNAIL_URL"));

					String videoThumbnailURL = rs.getString("VIDEO_THUMBNAIL_URL");

					if (StringUtils.isNotBlank(videoThumbnailURL)) {
						videoThumbnailURL = videoThumbnailURL.replaceAll("https://storage.googleapis.com/" + bucketName
								+ "/" + environment + "/GCloud/WPortal/ObservationVideoThumbnail/", "");
					}

					petObservationMediaListDTO.setVideoThumbnailList(gcStorageUtil
							.getMediaSignedUrlList(videoThumbnailURL, Constants.GCP_OBSERVATION_VIDEO_THUMBNAIL_PATH));

					petObservationMediaListDTO.setCreatedDate(rs.getTimestamp("CREATED_DATE") == null ? null
							: rs.getTimestamp("CREATED_DATE").toLocalDateTime());
					petObservationMediaListDTO.setModifiedDate(rs.getTimestamp("MODIFIED_DATE") == null ? null
							: rs.getTimestamp("MODIFIED_DATE").toLocalDateTime());
					petObservationMediaList.add(petObservationMediaListDTO);
				}

			}, filter.getStartIndex(), filter.getLimit(), filter.getSortBy(), filter.getOrder(), filter.getSearchText(),
					filter.getStatus(), filter.getStudy(), filter.getStartDate(), filter.getEndDate());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetObservationMediaList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petObservationMediaList;
	}

	@Override
	public List<PetObservationMediaListDTO> getPetObservationMediaById(int petId) {
		List<PetObservationMediaListDTO> petObservationMediaList = new ArrayList<>();

		LOGGER.debug("getPetObservationMediaById called");
		try {
			jdbcTemplate.query(SQLConstants.OBSERVATION_MEDIA_GET_BY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetObservationMediaListDTO petObservationMediaListDTO = new PetObservationMediaListDTO();

					petObservationMediaListDTO.setPetName(rs.getString("PET_NAME"));
					petObservationMediaListDTO.setBreedId(rs.getInt("BREED_ID"));
					petObservationMediaListDTO.setBreedName(rs.getString("BREED_NAME"));
					petObservationMediaListDTO.setStudyNames(rs.getString("STUDY_NAME"));
					petObservationMediaListDTO.setPhotoName(rs.getString("PHOTO_NAME"));
					petObservationMediaListDTO.setPetStatusId(rs.getInt("PET_STATUS_ID"));
					petObservationMediaListDTO.setPetStatus(rs.getString("STATUS_NAME"));

					String fileName = rs.getString("PHOTO_NAME");
					if (fileName != null && !fileName.trim().equals("")) {
						petObservationMediaListDTO.setPetPhotoUrl(
								gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
					}

					petObservationMediaListDTO.setImagePath(rs.getString("IMAGE_PATH"));
					petObservationMediaListDTO.setVideoURL(rs.getString("VIDEO_URL"));

					String imagePathCSV = rs.getString("IMAGE_PATH");

					petObservationMediaListDTO.setImageList(
							gcStorageUtil.getMediaSignedUrlList(imagePathCSV, Constants.GCP_OBSERVATION_PHOTO_PATH));

					petObservationMediaListDTO.setVideoURL(rs.getString("VIDEO_URL"));
					String videoPathCSV = rs.getString("VIDEO_URL");

					if (StringUtils.isNotBlank(videoPathCSV)) {
						videoPathCSV = videoPathCSV.replaceAll("https://storage.googleapis.com/" + bucketName + "/"
								+ environment + "/GCloud/WPortal/ObservationVideo/", "");
					}

					petObservationMediaListDTO.setVideoList(
							gcStorageUtil.getMediaSignedUrlList(videoPathCSV, Constants.GCP_OBSERVATION_VIDEO_PATH));
					petObservationMediaListDTO.setVideoThumbnailURL(rs.getString("VIDEO_THUMBNAIL_URL"));
					String videoThumbnailURL = rs.getString("VIDEO_THUMBNAIL_URL");

					if (StringUtils.isNotBlank(videoThumbnailURL)) {
						videoThumbnailURL = videoThumbnailURL.replaceAll("https://storage.googleapis.com/" + bucketName
								+ "/" + environment + "/GCloud/WPortal/ObservationVideoThumbnail/", "");
					}
					petObservationMediaListDTO.setVideoThumbnailList(gcStorageUtil
							.getMediaSignedUrlList(videoThumbnailURL, Constants.GCP_OBSERVATION_VIDEO_THUMBNAIL_PATH));

					petObservationMediaListDTO.setCreatedDate(rs.getTimestamp("CREATED_DATE") == null ? null
							: rs.getTimestamp("CREATED_DATE").toLocalDateTime());
					petObservationMediaListDTO.setModifiedDate(rs.getTimestamp("MODIFIED_DATE") == null ? null
							: rs.getTimestamp("MODIFIED_DATE").toLocalDateTime());

					petObservationMediaList.add(petObservationMediaListDTO);
				}

			}, petId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetObservationMediaById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petObservationMediaList;
	}

	@Override
	public String getPetViewPaneCount(PetFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetViewPaneCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_VIEW_PANE_COUNT, String.class, filter.getSearchText(),
					filter.getUserId(), filter.getRoleTypeId());
		} catch (Exception e) {
			LOGGER.error("error while getPetViewPaneCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetListDTO> getPetViewPane(PetFilter filter) throws ServiceExecutionException {
		List<PetListDTO> petList = new ArrayList<>();
		LOGGER.debug("getPetViewPane called " + filter.getUserId());
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_VIEW_PANE, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetListDTO petListDTO = new PetListDTO();
					petListDTO.setPetId(rs.getInt("PET_ID"));
					petListDTO.setPetName(rs.getString("PET_NAME"));
					petListDTO.setPetPhoto(rs.getString("PHOTO_NAME"));
					String fileName = rs.getString("PHOTO_NAME");

					if (fileName != null && !fileName.trim().equals("")) {
						petListDTO.setPetPhotoUrl(
								gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
					}

					petListDTO.setBreedName(rs.getString("BREED_NAME"));
					/*
					 * petListDTO.setStudyId(rs.getInt("STUDY_ID"));
					 * petListDTO.setStudyName(rs.getString("STUDY_NAME"));
					 */
					petListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					petListDTO.setPetStatusId(rs.getInt("PET_STATUS_ID"));
					petListDTO.setPetStatus(rs.getString("STATUS_NAME"));
					petListDTO.setSelectStudyId(rs.getInt("SELECT_STUDY_ID"));
					petList.add(petListDTO);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getSearchText(), filter.getUserId(),
					filter.getRoleTypeId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetViewPane", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@Override
	public void disassociateStudy(int petId, int studyId) {
		int userId = authentication.getAuthUserDetails().getUserId();
		LOGGER.debug("disassociateStudy called " + userId);
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_pet_id", petId);
		inputParams.put("p_study_id", studyId);
		inputParams.put("p_modified_by", userId);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_DISASSOCIATE_STUDY, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isEmpty(errorMsg)) {
				LOGGER.info("Study has been disassociated with Pet successfully");
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing disassociateStudy ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("disassociateStudy end " + userId);
	}

	@Override
	public List<PetWeightHistoryDTO> getPetWeightHistory(BaseFilter filter, int petId, String fromDate, String toDate)
			throws ServiceExecutionException {
		List<PetWeightHistoryDTO> weightList = new ArrayList<>();
		LOGGER.debug("getPetWeightHistory called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_WEIGHT_HISTORY, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetWeightHistoryDTO dto = new PetWeightHistoryDTO();
					dto.setPetId(rs.getInt("PET_ID"));
					dto.setWeight(rs.getDouble("WEIGHT"));
					dto.setWeightKgs(rs.getDouble("WEIGHT_KGS"));
					dto.setActive(rs.getBoolean("IS_ACTIVE"));
					dto.setAddDate(rs.getTimestamp("ADD_DATE").toLocalDateTime());
					dto.setCreatedBy(rs.getInt("CREATED_BY"));
					dto.setModifiedBy(rs.getInt("MODIFIED_BY"));
					dto.setWeightUnit(rs.getString("WEIGHT_UNIT") == null ? null : rs.getString("WEIGHT_UNIT"));
					dto.setPetWeightId(rs.getInt("PET_WEIGHT_ID"));
					Timestamp createDate = rs.getTimestamp("CREATED_DATE");
					if (createDate != null) {
						dto.setCreatedDate(createDate.toLocalDateTime());
					}
					Timestamp modifiedDate = rs.getTimestamp("MODIFIED_DATE");
					if (modifiedDate != null) {
						dto.setModifiedDate(modifiedDate.toLocalDateTime());
					}
					weightList.add(dto);
				}
			}, petId, fromDate, toDate, filter.getStartIndex(), filter.getLimit(), filter.getOrder(),
					filter.getSortBy(), filter.getFilterType(), filter.getFilterValue()

			);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetWeightHistory", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return weightList;
	}

	@Override
	public String getPetWeightHistoryCount(BaseFilter filter, int petId, String fromDate, String toDate)
			throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetWeightHistoryCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_WEIGHT_HISTORY_COUNT, String.class, petId, fromDate,
					toDate);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetWeightHistoryCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetStudyDTO> getCurrentStudies(int petId) throws ServiceExecutionException {
		final List<PetStudyDTO> petList = new ArrayList<>();
		int userId = authentication.getAuthUserDetails().getUserId();
		LOGGER.debug("getCurrentStudies called " + userId);
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_CURRENT_STUDY_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetStudyDTO petStudyDTO = new PetStudyDTO();
					petStudyDTO.setPetId(rs.getInt("PET_ID"));
					petStudyDTO.setStudyId(rs.getInt("STUDY_ID"));
					petStudyDTO.setStudyName(rs.getString("STUDY_NAME"));
					petStudyDTO.setStartDate(
							rs.getDate("START_DATE") != null ? rs.getDate("START_DATE").toLocalDate() : null);
					petStudyDTO
							.setEndDate(rs.getDate("END_DATE") != null ? rs.getDate("END_DATE").toLocalDate() : null);
					petStudyDTO.setStudyActive(rs.getBoolean("IS_ACTIVE"));
					petStudyDTO.setPricipleInvestigator(rs.getString("PRINCIPLE_INVESTIGATOR"));
					petStudyDTO.setAssigned(rs.getBoolean("IS_ASSIGN"));
					petStudyDTO.setPetStudyId(rs.getInt("PET_STUDY_ID"));
					Integer isExternalStudy = rs.getInt("IS_EXTERNAL");
					petStudyDTO.setExternalStudy(isExternalStudy == 1 ? true : false);

					// petStudyDTO.setis(rs.getBoolean("IS_ASSIGN"));
					/*
					 * petStudyDTO.setAssignedDate(rs.getDate("ASSIGN_DATE") != null ?
					 * rs.getDate("ASSIGN_DATE").toLocalDate() : null);
					 * petStudyDTO.setAssignedDate(rs.getDate("UN_ASSIGN_DATE") != null ?
					 * rs.getDate("UN_ASSIGN_DATE").toLocalDate() : null);
					 */

					petList.add(petStudyDTO);

				}
			}, petId, userId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getCurrentStudies", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@Override
	public List<PetStudyDTO> getArchiveStudies(int petId) throws ServiceExecutionException {
		final List<PetStudyDTO> petList = new ArrayList<>();
		int userId = authentication.getAuthUserDetails().getUserId();
		LOGGER.debug("getArchiveStudies called " + userId);
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_ARCHIVE_STUDY_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetStudyDTO petStudyDTO = new PetStudyDTO();
					petStudyDTO.setPetId(rs.getInt("PET_ID"));
					petStudyDTO.setStudyId(rs.getInt("STUDY_ID"));
					petStudyDTO.setStudyName(rs.getString("STUDY_NAME"));
					Date startDate = rs.getDate("START_DATE");
					if (startDate != null) {
						petStudyDTO.setStartDate(startDate != null ? rs.getDate("START_DATE").toLocalDate() : null);
					}
					Date endDate = rs.getDate("END_DATE");
					if (endDate != null) {
						petStudyDTO.setEndDate(endDate != null ? rs.getDate("END_DATE").toLocalDate() : null);
					}

					petStudyDTO.setStudyActive(rs.getBoolean("IS_ACTIVE"));
					petStudyDTO.setPricipleInvestigator(rs.getString("PRINCIPLE_INVESTIGATOR"));
					petStudyDTO.setAssigned(rs.getBoolean("IS_ASSIGN"));
					petStudyDTO.setPetStudyId(rs.getInt("PET_STUDY_ID"));

					// petStudyDTO.setAssetAssigned(rs.getBoolean("IS_ASSIGN"));

					/*
					 * petStudyDTO.setAssignedDate(rs.getDate("ASSIGN_DATE") != null ?
					 * rs.getDate("ASSIGN_DATE").toLocalDate() : null);
					 * petStudyDTO.setAssignedDate(rs.getDate("UN_ASSIGN_DATE") != null ?
					 * rs.getDate("UN_ASSIGN_DATE").toLocalDate() : null);
					 */

					petList.add(petStudyDTO);

				}
			}, petId, userId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getArchiveStudies", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petList;
	}

	@Override
	public List<PetDevice> getPetDevicesByStudy(PetFilter filter) throws ServiceExecutionException {
		List<PetDevice> petDevices = new ArrayList<>();
		LOGGER.debug("getPetDevicesByStudy called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_DEVICES_BY_PET_ID_AND_STUDY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetDevice petDevice = new PetDevice();
					petDevice.setPetId(rs.getInt("PET_ID"));
					petDevice.setStudyId(rs.getInt("STUDY_ID"));
					petDevice.setStudyName(rs.getString("STUDY_NAME"));
					petDevice.setStudyDescription(rs.getString("STUDY_DESCRIPTION"));
					petDevice.setDeviceId(rs.getInt("DEVICE_ID"));
					petDevice.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					petDevice.setDeviceModel(rs.getString("DEVICE_MODEL"));
					petDevice.setFirmwareVersion(rs.getString("FIRMWARE_VERSION_NUMBER"));
					petDevice.setLastSync(rs.getString("LAST_SYNC")); // in days and hours.

					petDevice.setAssignedOn(rs.getTimestamp("ASSIGN_DATE") != null
							? rs.getTimestamp("ASSIGN_DATE").toLocalDateTime().toLocalDate()
							: null);
					petDevice.setBatteryPercentage(rs.getString("BATTERY_PERCENTAGE"));
					petDevice.setUnassignedReason(rs.getString("UNASSIGN_REASON"));
					Date unAssignedOn = rs.getDate("UN_ASSIGN_DATE");
					if (unAssignedOn != null) {
						petDevice.setUnAssignedOn(unAssignedOn);
					}

					petDevices.add(petDevice);
				}
			}, filter.getPetId(), filter.getStudyId(), filter.getStartIndex(), filter.getLimit(), filter.getSortBy(),
					filter.getOrder());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetDevicesByStudy", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDevices;
	}

	/* This method using for View Pet */
	@Override
	public PetDTO getPetDetailsById(int petId) throws ServiceExecutionException {
		final PetDTO petDTO = new PetDTO();
		LOGGER.debug("getPetDetailsById called");
		List<PetParentDTO> petParents = new ArrayList<>();
		List<PetNote> petNotes = new ArrayList<>();
		try {

			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_id", petId);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(SQLConstants.PET_GET_DETAILS_BY_ID,
					inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(SQLConstants.RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(pet -> {
						petDTO.setPetId((Integer) pet.get("PET_ID"));
						petDTO.setPetName((String) pet.get("PET_NAME"));
						petDTO.setPhotoName((String) pet.get("PHOTO_NAME"));
						String fileName = (String) pet.get("PHOTO_NAME");
						if (fileName != null && !fileName.trim().equals("")) {
							petDTO.setPetPhotoUrl(
									gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
						}
						if (pet.get("BREED_ID") != null) {
							petDTO.setBreedId((Integer) pet.get("BREED_ID"));
						}
						petDTO.setBreedName((String) pet.get("BREED_NAME"));
						petDTO.setSpeciesId((Integer) pet.get("SPECIES_ID"));
						petDTO.setSpeciesName((String) pet.get("SPECIES_NAME"));
						petDTO.setFeedingPreferences((String) pet.get("FEEDING_PREFERENCES"));
						petDTO.setGender((String) pet.get("GENDER"));
						LocalDateTime birthday = (LocalDateTime) pet.get("BIRTHDAY");
						petDTO.setDateOfBirth(birthday != null ? birthday.toLocalDate() : null);
						petDTO.setIsDobUnknown((Boolean) pet.get("IS_UNKNOWN"));

						BigDecimal weight = (BigDecimal) pet.get("WEIGHT");
						petDTO.setWeight(weight != null ? weight.doubleValue() : 0);
						petDTO.setWeightUnit((String) pet.get("WEIGHT_UNIT"));
						petDTO.setIsNeutered((Boolean) pet.get("IS_NEUTERED"));
						petDTO.setIsDeceased((Boolean) pet.get("IS_DECEASED"));
						petDTO.setIsMixed((Boolean) pet.get("IS_MIXED"));
						petDTO.setPetStatusId((Integer) pet.get("PET_STATUS_ID"));
						petDTO.setPetStatus((String) pet.get("STATUS_NAME"));
						petDTO.setIsActive((Boolean) pet.get("IS_ACTIVE"));
						petDTO.setQuestionnaireAttempted((Integer) pet.get("QUESTIONNAIRE_COUNT"));

						Integer isExternalPet = (Integer) pet.get("IS_EXTERNAL_PET");
						petDTO.setIsExternalPet((isExternalPet != null && isExternalPet == 1) ? true : false);
						// Timestamp timestamp = (Timestamp) pet.get("MODIFIED_DATE");
						petDTO.setModifiedDate((LocalDateTime) pet.get("MODIFIED_DATE"));

						if (pet.get("SELECT_STUDY_ID") != null) {
							petDTO.setSelectStudyId(pet.get("SELECT_STUDY_ID") + "");
						}
					});
				}
				if (key.equals(SQLConstants.RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(parent -> {
						PetParentDTO petParentDTO = new PetParentDTO();
						petParentDTO.setPetParentId((Integer) parent.get("PET_PARENT_ID"));
						petParentDTO.setPetParentName((String) parent.get("FULL_NAME"));
						petParentDTO.setFirstName((String) parent.get("FIRST_NAME"));
						petParentDTO.setLastName((String) parent.get("FIRST_NAME"));
						petParentDTO.setEmail((String) parent.get("EMAIL"));
						petParentDTO.setPhoneNumber((String) parent.get("PHONE_NUMBER"));
						petParentDTO.setShippingAddress((String) parent.get("SHIPPING_ADDRESS"));
						petParents.add(petParentDTO);
					});
				}

				if (key.equals(SQLConstants.RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(note -> {
						PetNote petNote = new PetNote();
						petNote.setPetId((Integer) note.get("PET_ID"));
						petNote.setNoteType((String) note.get("NOTE_TYPE"));
						petNote.setContent((String) note.get("CONTENT"));
						petNote.setModifiedDate((LocalDateTime) note.get("MODIFIED_DATE"));
						petNotes.add(petNote);
					});
				}

			}
			petDTO.setPetParents(petParents);
			petDTO.setPetNotes(petNotes);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetDetailsById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public PetCampaignPointsDTO getPetCampaignPoints(int petId) throws ServiceExecutionException {
		final PetCampaignPointsDTO petCampaignPointsDTO = new PetCampaignPointsDTO();
		LOGGER.debug("getPetCampaignPoints called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_ALL_CAMPAIGN_POINTS_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {

					petCampaignPointsDTO.setTotalEarnedPoints(rs.getInt("TOTAL_EARNED_POINTS"));
					petCampaignPointsDTO.setImages(rs.getInt("TOTAL_IMAGES"));
					petCampaignPointsDTO.setObservations(rs.getInt("TOTAL_OBSERVATIONS"));
					petCampaignPointsDTO.setPointsUtilized(rs.getInt("POINTS_UTILIZED"));
					petCampaignPointsDTO.setQuestionnaire(rs.getInt("TOTAL_QUESIONARES"));
					petCampaignPointsDTO.setFeedback(rs.getInt("TOTAL_FEEDBACK"));
					petCampaignPointsDTO.setVideos(rs.getInt("TOTAL_VIDEOS"));
					petCampaignPointsDTO.setRedeemablePoints(rs.getInt("REMAINING_POINTS"));

				}
			}, petId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignPoints", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petCampaignPointsDTO;
	}

	@Override
	public String getPetCampaignListCount(int petId, int activityId) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetCampaignListCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_CAMPAIGN_POINTS_LIST_COUNT_BY_PET, String.class, petId,
					activityId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignListCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetCampaignPointsListDTO> getPetCampaignPointsList(int petId, int activityId, BaseFilter filter)
			throws ServiceExecutionException {
		LOGGER.debug("getPetCampaignPointsList called");
		List<PetCampaignPointsListDTO> campaignList = new ArrayList<>();

		try {
			jdbcTemplate.query(SQLConstants.PET_GET_CAMPAIGN_POINTS_LIST_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetCampaignPointsListDTO campagin = new PetCampaignPointsListDTO();
					campagin.setPetCampaignPointsId(rs.getInt("TRACKER_PET_POINTS_ID"));
					campagin.setPetId(rs.getInt("PET_ID"));
					campagin.setPetName(rs.getString("PET_NAME"));
					campagin.setCampaignName(rs.getString("CAMPAIGN_NAME"));

					String fileName = rs.getString("FILE_NAME");
					String imageURL = rs.getString("IMAGE_PATH");
					if (StringUtils.isNotBlank(imageURL)) {
						if (!StringUtils.contains(imageURL, "firebasestorage")) {
							String imageSignedUrl = gcpClientUtil.getDownloaFiledUrl(
									imageURL.concat("/").concat(fileName), Constants.GCP_OBSERVATION_PHOTO_PATH);
							campagin.setImageUrl(imageSignedUrl);
						} else {
							campagin.setImageUrl(imageURL);
						}
					}

					String videoURL = rs.getString("VIDEO_URL");
					if (StringUtils.isNotBlank(videoURL)) {
						if (!StringUtils.contains(videoURL, "firebasestorage")) {
							String mediaSignedUrl = gcpClientUtil.getDownloaFiledUrl(
									videoURL.replaceAll("https://storage.googleapis.com/" + bucketName + "/"
											+ environment + "/GCloud/WPortal/ObservationVideo/", ""),
									Constants.GCP_OBSERVATION_VIDEO_PATH);
							campagin.setVideoUrl(mediaSignedUrl);
						} else {
							campagin.setVideoUrl(videoURL);
						}
					}

					String videoThumbnailURL = rs.getString("VIDEO_THUMBNAIL");
					if (StringUtils.isNotBlank(videoThumbnailURL)) {
						if (!StringUtils.contains(videoThumbnailURL, "firebasestorage")) {
							String mediaSignedThumbUrl = gcpClientUtil.getDownloaFiledUrl(
									videoThumbnailURL.replaceAll("https://storage.googleapis.com/" + bucketName + "/"
											+ environment + "/GCloud/WPortal/ObservationVideoThumbnail/", ""),
									Constants.GCP_OBSERVATION_VIDEO_THUMBNAIL_PATH);
							campagin.setVideoThumbnailUrl(mediaSignedThumbUrl);
						} else {
							campagin.setVideoThumbnailUrl(videoThumbnailURL);
						}
					}

					campagin.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					campagin.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					campagin.setFeedback(rs.getString("FEED_BACK_TEXT"));
					campagin.setStatusId(rs.getInt("STATUS_ID"));
					campagin.setStatus(rs.getString("STATUS"));

					Timestamp createdDate = (Timestamp) rs.getTimestamp("CREATED_DATE");
					campagin.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);
					campagin.setObservation(rs.getString("OBSERVATION"));
					campagin.setActivityId(rs.getInt("ACTIVITY_ID"));
					campagin.setActivityName(rs.getString("ACTIVITY"));
					campagin.setBehaviourId(rs.getInt("BEHAVIOR_ID"));
					campagin.setBehaviourName(rs.getString("BEHAVIOR"));
					campagin.setPoints(rs.getInt("POINTS"));

					campaignList.add(campagin);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), petId, activityId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignPointsList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return campaignList;
	}

	@Override
	public String getPetRewardPointsHistoryCount(int petId) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetRewardPointsHistoryCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_REWARD_POINTS_LIST_COUNT_BY_PET, String.class, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetRewardPointsHistoryCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetRedemptionHistoryDTO> getPetRedemptionHistory(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetRedemptionHistory called");
		List<PetRedemptionHistoryDTO> redemptionHistoryDTOs = new ArrayList<>();

		try {
			jdbcTemplate.query(SQLConstants.PET_GET_REDEEMTION_HISTORY_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetRedemptionHistoryDTO redemptionHistoryDTO = new PetRedemptionHistoryDTO();
					redemptionHistoryDTO.setPetId(rs.getInt("PET_ID"));
					redemptionHistoryDTO.setTotalPoints(rs.getInt("TOTAL_POINTS"));
					redemptionHistoryDTO.setPointsRedeemed(rs.getInt("POINTS_REDEMED"));
					redemptionHistoryDTO.setBalancePoints(rs.getInt("AVAILABLE_POINTS"));

					redemptionHistoryDTO.setRedeemedByUserId(rs.getInt("REDEEMED_BY"));
					redemptionHistoryDTO.setRedeemedByUser(rs.getString("FULL_NAME"));

					redemptionHistoryDTO.setCreatedBy(rs.getInt("CREATED_BY"));
					redemptionHistoryDTO.setModifiedBy(rs.getInt("MODIFIED_BY"));

					Timestamp modifiedDate = (Timestamp) rs.getTimestamp("MODIFIED_DATE");
					Timestamp createdDate = (Timestamp) rs.getTimestamp("CREATED_DATE");
					redemptionHistoryDTO.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);
					redemptionHistoryDTO.setModifiedDate(modifiedDate != null ? modifiedDate.toLocalDateTime() : null);

					redemptionHistoryDTOs.add(redemptionHistoryDTO);
				}
			}, petId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetRedemptionHistory", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return redemptionHistoryDTOs;
	}

	@Override
	public void redeemRewardPoints(int petId, int redeemPoints, Integer userId) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_pet_id", petId);
		inputParams.put("p_redeem_points", redeemPoints);
		inputParams.put("p_modified_by", userId);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_REDEEM_POINTS, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isEmpty(errorMsg)) {
				LOGGER.info("Pet Redeem points for Pet successfully");
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing redeemRewardPoints ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<PetExternalInfoListDTO> getExternalPetInfoList(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getExternalPetInfoList start");
		List<PetExternalInfoListDTO> externalPetList = new ArrayList<>();
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_EXTERNAL_INFO_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetExternalInfoListDTO dto = new PetExternalInfoListDTO();
					dto.setExternalPetId(rs.getString("EXT_PET_ID"));
					dto.setExternalPetValue(rs.getString("EXT_PET_VALUE"));

					externalPetList.add(dto);
				}
			}, studyId);
		} catch (Exception e) {
			LOGGER.error("error while fetching getExternalPetInfoList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("getExternalPetInfo end ");
		return externalPetList;
	}

	@Override
	public List<ExternalPetInfoListDTO> getExternalPetInfo(ExternalPetInfoFilter filter)
			throws ServiceExecutionException {
		List<ExternalPetInfoListDTO> externalPetInfoListDTOList = new ArrayList<>();
		try {
			String sql = SQLConstants.PET_GET_EXTERNAL_PET_INFO;
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					ExternalPetInfoListDTO externalPetInfoListDTO = new ExternalPetInfoListDTO();
					externalPetInfoListDTO.setPlainId(rs.getString("EXT_PLAN_ID"));
					externalPetInfoListDTO.setPetId(rs.getString("EXT_PET_ID"));
					externalPetInfoListDTO.setCategory(rs.getString("CATEGORY"));
					externalPetInfoListDTO.setExtractGroup(rs.getString("EXTRACT_GROUP"));
					externalPetInfoListDTO.setFieldName(rs.getString("FIELD_NAME"));
					externalPetInfoListDTO.setFieldValue(rs.getString("FIELD_VALUE"));
					externalPetInfoListDTOList.add(externalPetInfoListDTO);
				}
			}, filter.getPetId(), filter.getStudyId());
		} catch (Exception e) {
			LOGGER.error("error while executing getExternalPetInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return externalPetInfoListDTOList;
	}

	@Override
	public PetDTO associateNewStudy(AssociateNewStudyRequest request) throws ServiceExecutionException {
		PetDTO petDTO = new PetDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", request.getPetId());
			inputParams.put("p_study_id", request.getStudyId());
			inputParams.put("p_study_associated_on", request.getAssignedOnDate());
			inputParams.put("p_user_id", request.getUserId());
			inputParams.put("p_external_pet_id", request.getExternalPetInfoId());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_ASSOCIATE_NEW_STUDY, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) && statusFlag < NumberUtils.INTEGER_ZERO) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("Pet Associate New Study, Study already assigend to the pet.",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.STUDY_ALREADY_EXIST_TO_PET, errorMsg)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}

		} catch (SQLException e) {
			LOGGER.error("error while executing associateNewStudy ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public PetDTO addPetWeight(AddPetWeight addPetWeight) throws ServiceExecutionException {
		PetDTO petDTO = new PetDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_id", addPetWeight.getPetId());
			inputParams.put("p_weight", addPetWeight.getWeight());
			inputParams.put("p_weight_unit", addPetWeight.getWeightUnit());
			inputParams.put("p_user_id", addPetWeight.getUserId());
			inputParams.put("p_add_date", addPetWeight.getAddDate());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_ADD_WEIGHT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) && statusFlag < NumberUtils.INTEGER_ZERO) {
				throw new ServiceExecutionException(errorMsg);
			}

		} catch (Exception e) {
			LOGGER.error("error while executing addPetWeight ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petDTO;
	}

	@Override
	public void updateWeight(int weightId, String weight, String unit) throws ServiceExecutionException {
		int userId = authentication.getAuthUserDetails().getUserId();
		LOGGER.debug("updateWeight called " + weightId);
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_weight_id", weightId);
		inputParams.put("p_weight", weight);
		inputParams.put("p_weight_unit", unit);
		inputParams.put("p_modified_by", authentication.getAuthUserDetails().getUserId());
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_UPDATE_WEIGHT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isEmpty(errorMsg)) {
				LOGGER.info("Pet weight updated successfully");
			} else {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (Exception e) {
			LOGGER.error("error while executing updateWeight ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		LOGGER.debug("updateWeight end " + userId);
	}

	@Override
	public String getPetFeedingEnthusiasmScaleDtlsCount(PetEnthusiasmFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetFeedingEnthusiasmScaleDtlsCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_FEEDING_ENTHUSIASM_SCALE_GET_COUNT, String.class,
					filter.getSearchText(), filter.getPetId(), filter.getEnthusiasmScaleId(), filter.getFeedingTimeId(),
					filter.getStartDate(), filter.getEndDate());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetFeedingEnthusiasmScaleDtlsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetFeedingEnthusiasmScale> getPetFeedingEnthusiasmScaleDtls(PetEnthusiasmFilter filter)
			throws ServiceExecutionException {
		List<PetFeedingEnthusiasmScale> feedingEnthusiasmScaleDtls = new ArrayList<>();
		LOGGER.debug("getPetFeedingEnthusiasmScaleDtls called");
		try {
			jdbcTemplate.query(SQLConstants.PET_FEEDING_ENTHUSIASM_SCALE_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetFeedingEnthusiasmScale feedingEnthusiasmScale = new PetFeedingEnthusiasmScale();
					feedingEnthusiasmScale.setSlNumber(rs.getInt("slNo"));
					feedingEnthusiasmScale.setFeedingEnthusiasmScaleId(rs.getInt("FEEDING_ENTSM_SCALE_ID"));
					feedingEnthusiasmScale.setPetId(rs.getInt("PET_ID"));
					feedingEnthusiasmScale.setEnthusiasmScaleId(rs.getInt("ENTHUSIASM_SCALE_ID"));
					feedingEnthusiasmScale.setFeedingTimeId(rs.getInt("PET_FEEDING_TIME_ID"));
					feedingEnthusiasmScale.setFeedingDate(rs.getDate("FEEDING_DATE").toLocalDate());
					feedingEnthusiasmScale.setPetParentId(rs.getInt("MODIFIED_BY"));
					feedingEnthusiasmScale.setEnthusiasmScale(rs.getString("ENTHUSIASM_SCALE"));
					feedingEnthusiasmScale.setEnthusiasmScaleDesc(rs.getString("DESCRIPTION"));
					feedingEnthusiasmScale.setFeedingTime(rs.getString("FEEDING_VALUE"));
					feedingEnthusiasmScale.setPetParentName(rs.getString("FULL_NAME"));
					feedingEnthusiasmScaleDtls.add(feedingEnthusiasmScale);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getPetId(), filter.getEnthusiasmScaleId(), filter.getFeedingTimeId(), filter.getStartDate(),
					filter.getEndDate());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetFeedingEnthusiasmScaleDtls", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return feedingEnthusiasmScaleDtls;
	}

	@Override
	public String getPetImageScaleDtlsCount(PetImageScaleFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetImageScaleDtlsCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_IMAGE_SCALE_GET_COUNT, String.class, filter.getSearchText(),
					filter.getPetId(), filter.getImageScaleId(), filter.getScoringTypeId(), filter.getStartDate(),
					filter.getEndDate());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetImageScaleDtlsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetImageScale> getPetImageScaleDtls(PetImageScaleFilter filter) throws ServiceExecutionException {
		List<PetImageScale> PetImageScaleList = new ArrayList<>();
		LOGGER.debug("getPetImageScaleDtls called");
		try {
			jdbcTemplate.query(SQLConstants.PET_IMAGE_SCALE_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetImageScale petImageScale = new PetImageScale();
					petImageScale.setSlNumber(rs.getInt("slNo"));
					petImageScale.setPetId(rs.getInt("PET_ID"));
					petImageScale.setScoreType(rs.getString("SCORING_TYPE"));
					petImageScale.setScaleName(rs.getString("IMAGE_SCALE_NAME"));
					petImageScale.setImageLabel(rs.getString("IMAGE_LABEL"));
					petImageScale.setScore(rs.getString("SCORE"));
					petImageScale.setPetParentName(rs.getString("FULL_NAME"));
					petImageScale.setSubmittedOn(DateTimeFormatter.ofPattern("MM/dd/yyyy")
							.format(rs.getTimestamp("SUBMITTED_ON").toLocalDateTime()));
					petImageScale.setPetImagePath(rs.getString("PET_IMAGE"));
					petImageScale.setPetImgThumbnailPath(rs.getString("PET_IMAGE_THUMBNAIL"));
					String imageName = rs.getString("SCALE_IMAGE");
					if (StringUtils.isNotEmpty(imageName)) {
						petImageScale.setScaleImagePath(
								gcpClientUtil.getDownloaFiledUrl(imageName, Constants.GCP_IMAGE_SCORING_PATH));
					}

					PetImageScaleList.add(petImageScale);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getPetId(), filter.getImageScaleId(), filter.getScoringTypeId(), filter.getStartDate(),
					filter.getEndDate());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetImageScaleDtls", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return PetImageScaleList;
	}

	@Override
	public String getPetParentsCount(PetFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetParentsCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_PARENTS_LIST_COUNT, String.class, filter.getPetId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public String getPetNotesCount(PetFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetNotesCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_NOTES_LIST_COUNT, String.class, filter.getPetId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetNotesCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public String getPetDevicesByStudyCount(PetFilter filter) throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("getPetDevicesByStudyCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_DEVICES_LIST_COUNT, String.class, filter.getPetId(),
					filter.getStudyId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetDevicesByStudyCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public String ActivityFactorResultResponseListCount(PetActivityFactorFilter filter)
			throws ServiceExecutionException {
		String totalCount = "";
		LOGGER.debug("ActivityFactorResultResponseListCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_GET_ACTIVITY_FACTOR_RESULTS_BY_PET_COUNT, String.class,
					filter.getPetId());
		} catch (Exception e) {
			LOGGER.error("error while ActivityFactorResultResponseListCount getPetDevicesByStudyCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<ActivityFactorResultResponse> getPetActivityFactorResult(PetActivityFactorFilter filter)
			throws ServiceExecutionException {
		List<ActivityFactorResultResponse> petAFResultList = new ArrayList<>();
		LOGGER.debug("getPetActivityFactorResult called");
		try {
			jdbcTemplate.query(SQLConstants.PET_GET_ACTIVITY_FACTOR_RESULTS_BY_PET, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					ActivityFactorResultResponse result = new ActivityFactorResultResponse();
					result.setStudyName(rs.getString("STUDY_NAME"));
					result.setExtPetValue(rs.getString("EXT_PET_VALUE"));
					result.setAfCalculatedDate(rs.getString("ACTIVITY_FACTOR_CALCULATED_DATE"));
					result.setAlgorithmName(rs.getString("ALGORITHM_NAME"));
					result.setAlgorithmVerion(rs.getString("ALGORITHM_VERSION"));
					result.setTraditionalEe(rs.getString("TRADITIONAL_EE"));
					result.setEstimatedEnergyExpenditure(rs.getString("ESTIMATED_ENERGY_EXPENDITURE"));
					result.setEstimatedStepCount(rs.getString("ESTIMATED_STEP_COUNT"));
					result.setEstimatedAf(rs.getString("ESTIMATED_ACTIVITY_FACTOR"));
					result.setRecommendedFeedAmt(rs.getString("RECOMMENDED_FEED_AMOUNT"));
					result.setFeedUnits(rs.getString("FEED_UNITS"));

					petAFResultList.add(result);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getPetId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetActivityFactorResult", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petAFResultList;
	}
}
