package com.hillspet.wearables.dao.petparent.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.constants.SQLConstants;
import com.hillspet.wearables.common.constants.StringLiterals;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.petparent.PetParentDao;
import com.hillspet.wearables.dto.PetNote;
import com.hillspet.wearables.dto.PetParentDTO;
import com.hillspet.wearables.dto.PetParentListDTO;
import com.hillspet.wearables.dto.PetsAssociatedDTO;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.request.PetParentRequest;
import com.hillspet.wearables.security.Authentication;

@Repository
public class PetParentDaoImpl extends BaseDaoImpl implements PetParentDao {

	private static final Logger LOGGER = LogManager.getLogger(PetParentDaoImpl.class);
	@Autowired GCPClientUtil gcpClientUtil;
	
	@Autowired
	private Authentication authentication;

	@Override
	public PetParentDTO addPetParent(PetParentRequest petParentRequest) throws ServiceExecutionException {
		PetParentDTO petParentDTO = new PetParentDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_first_name", petParentRequest.getPetParentFirstName());
			inputParams.put("p_pet_parent_last_name", petParentRequest.getPetParentLastName());
			inputParams.put("p_email", petParentRequest.getEmail());
			inputParams.put("p_phone_number", petParentRequest.getPhoneNumber());
			inputParams.put("p_shipping_address", petParentRequest.getShippingAddress());
			inputParams.put("p_pets_associated",
					StringUtils.join(petParentRequest.getPetsAssociated(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_created_by", petParentRequest.getUserId());
			inputParams.put("p_status", petParentRequest.getStatus());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_PARENT_INSERT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer petParentId = (int) outParams.get("last_insert_id");
				LOGGER.info("Pet Parent has been created successfully, Pet Parent id is ", petParentId);
				petParentRequest.setPetParentId(petParentId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("AddPetParent service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.PET_PARENT_EMAIL_ALREADY_EXISTS,
									petParentRequest.getEmail())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
			BeanUtils.copyProperties(petParentRequest, petParentDTO);
		} catch (SQLException e) {
			LOGGER.error("error while executing AddPetParent ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petParentDTO;
	}

	@Override
	public PetParentDTO updatePetParent(PetParentRequest petParentRequest) throws ServiceExecutionException {
		PetParentDTO petParentDTO = new PetParentDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_pet_parent_id", petParentRequest.getPetParentId());
			inputParams.put("p_pet_parent_first_name", petParentRequest.getPetParentFirstName());
			inputParams.put("p_pet_parent_last_name", petParentRequest.getPetParentLastName());
			inputParams.put("p_email", petParentRequest.getEmail());
			inputParams.put("p_phone_number", petParentRequest.getPhoneNumber());
			inputParams.put("p_shipping_address", petParentRequest.getShippingAddress());
			inputParams.put("p_pets_associated",
					StringUtils.join(petParentRequest.getPetsAssociated(), StringLiterals.COMMA.getCode()));
			inputParams.put("p_status", petParentRequest.getStatus());
			inputParams.put("p_modified_by", petParentRequest.getUserId());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_PARENT_UPDATE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updatePetParent service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.PET_PARENT_EMAIL_ALREADY_EXISTS,
									petParentRequest.getEmail())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
			BeanUtils.copyProperties(petParentRequest, petParentDTO);
		} catch (SQLException e) {
			LOGGER.error("error while executing updatePetParent ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petParentDTO;
	}

	@Override
	public PetParentDTO getPetParentById(int petParentId) throws ServiceExecutionException {
		final PetParentDTO petParentDto = new PetParentDTO();
		List<PetsAssociatedDTO> petsAssociated = new ArrayList<>();
		LOGGER.debug("getPetParentById called");
		int userId = authentication.getAuthUserDetails().getUserId();
		try {
			
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_pet_parent_id", petParentId);
			inputParams.put("p_user_id", userId);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(SQLConstants.PET_PARENT_GET_BY_ID, inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();
			
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(SQLConstants.RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(parent -> {
						petParentDto.setPetParentId((Integer) parent.get("PET_PARENT_ID"));
						petParentDto.setPetParentName((String) parent.get("FULL_NAME"));
						petParentDto.setFirstName((String) parent.get("FIRST_NAME"));
						petParentDto.setLastName((String) parent.get("LAST_NAME"));
						petParentDto.setEmail((String) parent.get("EMAIL"));
						petParentDto.setPhoneNumber((String) parent.get("PHONE_NUMBER"));
						petParentDto.setShippingAddress((String) parent.get("SHIPPING_ADDRESS"));
						Boolean status = (Boolean) parent.get("PET_PARENT_STATUS");
						petParentDto.setStatus(status ? 1: 0);
					});
				}
				if (key.equals(SQLConstants.RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(pets -> {
						PetsAssociatedDTO associatedPet = new PetsAssociatedDTO();
						associatedPet.setPetId((Integer) pets.get("PET_ID"));
						associatedPet.setPetName((String) pets.get("PET_NAME"));
						String fileName = (String) pets.get("PHOTO_NAME");
						if(fileName!=null && !fileName.trim().equals("")) {
							associatedPet.setPetPhotoUrl(gcpClientUtil.getDownloaFiledUrl(fileName, Constants.GCP_PETPHOTO_PATH));
						}
						
						associatedPet.setPetPhoto((String) pets.get("PHOTO_NAME"));
						associatedPet.setBreedName((String) pets.get("BREED_NAME"));
						BigDecimal  weight = (BigDecimal ) pets.get("PET_WEIGHT"); 
						if(weight != null) {
							associatedPet.setWeight(weight.toString());
						}
						
						associatedPet.setGender((String) pets.get("GENDER"));
						LocalDateTime birthday = (LocalDateTime) pets.get("BIRTHDAY");
						associatedPet.setDob(birthday != null ? birthday.toLocalDate() : null);
						associatedPet.setPetStatus((Integer)pets.get("PET_STAUTS"));
						
						associatedPet.setIsNeutered((Boolean) pets.get("IS_NEUTERED"));
						associatedPet.setIsDeceased((Boolean)pets.get("IS_DECEASED"));
						/*associatedPet.setDeviceNumber((String)pets.get("DEVICE_NUMBER"));
						associatedPet.setDeviceType((String)pets.get("DEVICE_TYPE"));
						associatedPet.setDeviceModel((String) pets.get("DEVICE_MODEL"));
						*/
						if(pets.get("PET_STUDY_ID") != null) {
							associatedPet.setPetStudyId((Integer) pets.get("PET_STUDY_ID"));	
						}
						
						petsAssociated.add(associatedPet);
					});
				}
			}
			petParentDto.setPetsAssociated(petsAssociated);  
		}catch (Exception e) {
			LOGGER.error("error while fetching getPetParents", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petParentDto;
	}

	@Override
	public String getPetParentListCount(BaseFilter filter) throws ServiceExecutionException {
		String totalCount;
		LOGGER.debug("getPetParentListCount called");
		try {
			totalCount = selectForObject(SQLConstants.PET_PARENT_GET_LIST_COUNT, String.class, filter.getSearchText(),
					filter.getStatusId(), filter.getUserId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentListCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<PetParentListDTO> getPetParentList(BaseFilter filter) throws ServiceExecutionException {
		List<PetParentListDTO> petParentList = new ArrayList<>();
		LOGGER.debug("getPetParentList called");
		try {
			jdbcTemplate.query(SQLConstants.PET_PARENT_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetParentListDTO petParentListDTO = new PetParentListDTO();
					petParentListDTO.setSlNumber(rs.getInt("slNo"));
					petParentListDTO.setPetParentId(rs.getInt("PET_PARENT_ID"));
					petParentListDTO.setPetParentName(rs.getString("FULL_NAME"));
					petParentListDTO.setPetParentFirstName(rs.getString("FIRST_NAME"));
					petParentListDTO.setPetParentLastName(rs.getString("LAST_NAME") != null ? rs.getString("LAST_NAME") : "" );
					petParentListDTO.setEmail(rs.getString("EMAIL"));
					petParentListDTO.setStudyNames(rs.getString("STUDY_NAMES"));
					petParentListDTO.setPetNames(rs.getString("PET_NAMES"));
					petParentListDTO.setCreatedDate(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
					petParentListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					petParentListDTO.setOnStudyPetExist(rs.getBoolean("PET_PARENT_ON_STUDY_PET_EXIST"));
					petParentList.add(petParentListDTO);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getStatusId(), filter.getUserId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petParentList;
	}

	@Override
	public List<PetParentListDTO> getPetParents() throws ServiceExecutionException {
		List<PetParentListDTO> petParentList = new ArrayList<>();
		LOGGER.debug("getPetParents called");
		int userId = authentication.getAuthUserDetails().getUserId();
		try {
			jdbcTemplate.query(SQLConstants.PET_PARENT_GET_ALL, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetParentListDTO petParentListDTO = new PetParentListDTO();
					petParentListDTO.setPetParentId(rs.getInt("PET_PARENT_ID"));
					petParentListDTO.setPetParentName(rs.getString("FULL_NAME"));
					petParentListDTO.setEmail(rs.getString("EMAIL"));
					petParentListDTO.setPhoneNumber(rs.getString("PHONE_NUMBER"));
					petParentListDTO.setShippingAddress(rs.getString("SHIPPING_ADDRESS"));
					petParentListDTO.setPetParentFirstName(rs.getString("FIRST_NAME"));
					petParentListDTO.setPetParentLastName(rs.getString("LAST_NAME"));

					petParentList.add(petParentListDTO);
				}
			}, userId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParents", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petParentList;
	}

	@Override
	public void deletePetParent(int petParentId, int modifiedBy) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_pet_parent_id", petParentId);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.PET_PARENT_DELETE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				throw new ServiceExecutionException(errorMsg,
						Status.BAD_REQUEST.getStatusCode(),Arrays.asList(new WearablesError(WearablesErrorCode.ALREADY_BEING_REFERENCED,
								errorMsg)));
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deletePetParent ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

}
