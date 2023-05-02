package com.hillspet.wearables.service.pet.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
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
import com.hillspet.wearables.response.ActivityFactorResultResponseList;
import com.hillspet.wearables.response.ExternalPetInfoResponse;
import com.hillspet.wearables.response.PetCampaignResponse;
import com.hillspet.wearables.response.PetDevicesResponse;
import com.hillspet.wearables.response.PetFeedingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.PetImageScaleResponse;
import com.hillspet.wearables.response.PetNotesResponse;
import com.hillspet.wearables.response.PetObservationMediaListResponse;
import com.hillspet.wearables.response.PetObservationsResponse;
import com.hillspet.wearables.response.PetParentListResponse;
import com.hillspet.wearables.response.PetRedemptionHistoryResponse;
import com.hillspet.wearables.response.PetWeightHistoryResponse;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.security.Authentication;
import com.hillspet.wearables.service.pet.PetService;

@Service
public class PetServiceImpl implements PetService {

	private static final Logger LOGGER = LogManager.getLogger(PetServiceImpl.class);

	@Autowired
	private PetDao petDao;

	@Autowired
	private Authentication authentication;

	@Override
	public PetsResponse getPetList(PetFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPetList called");
		int searchedElements = 0;
		String counts = petDao.getPetsCount(filter);
		int total, totalActivePets, totalActiveStudies = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonCountObj = mapper.readTree(counts);
			total = jsonCountObj.get("totalCount").asInt();
			totalActivePets = jsonCountObj.get("totalActivePets").asInt();
			totalActiveStudies = jsonCountObj.get("totalActiveStudies").asInt();
			searchedElements = jsonCountObj.get("searchedElementsCount").asInt();

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetListDTO> petList = total > 0 ? petDao.getPetList(filter) : new ArrayList<>();

		PetsResponse response = new PetsResponse();
		response.setPetsList(petList);
		response.setNoOfElements(petList.size());
		response.setTotalRecords(total);
		response.setTotalActivePets(totalActivePets);
		response.setTotalActiveStudies(totalActiveStudies);
		response.setSearchElments(searchedElements);

		LOGGER.debug("getPetList pet count is {}", petList);
		LOGGER.debug("getPetList completed successfully");
		return response;
	}

	@Override
	public List<PetListDTO> getPets() throws ServiceExecutionException {
		LOGGER.debug("getPets called");
		List<PetListDTO> pets = petDao.getPets();
		LOGGER.debug("getPets pet count is {}", pets.size());
		LOGGER.debug("getPets completed successfully");
		return pets;
	}

	@Override
	public PetDTO addPet(PetRequest petRequest) throws ServiceExecutionException {
		LOGGER.debug("updatePet called");
		PetDTO petDTO = petDao.addPet(petRequest);
		LOGGER.debug("updatePet completed successfully");
		return petDTO;
	}

	@Override
	public PetDTO updatePet(PetRequest petRequest) throws ServiceExecutionException {
		LOGGER.debug("updatePet called");
		PetDTO petDTO = petDao.updatePet(petRequest);
		LOGGER.debug("updatePet completed successfully");
		return petDTO;
	}

	@Override
	public PetDTO getPetById(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetById called");
		PetDTO petDTO = petDao.getPetById(petId);
		LOGGER.debug("getPetById completed successfully");
		return petDTO;
	}

	@Override
	public PetDTO getPetByIdAndStudyId(int petId, int studyId) throws ServiceExecutionException {
		LOGGER.debug("getPetByIdAndStudyId called");
		PetDTO petDTO = petDao.getPetByIdAndStudyId(petId, studyId);
		LOGGER.debug("getPetByIdAndStudyId completed successfully");
		return petDTO;
	}

	@Override
	public void deletePet(int petId, int modifiedBy) throws ServiceExecutionException {
		LOGGER.debug("deletePet called");
		petDao.deletePet(petId, modifiedBy);
		LOGGER.debug("deletePet completed successfully");
	}

	@Override
	public PetsResponse getPetsByPetParent(int petParentId) throws ServiceExecutionException {
		LOGGER.debug("getPetsByPetParent called");
		List<PetListDTO> petList = petDao.getPetsByPetParent(petParentId);

		PetsResponse response = new PetsResponse();
		response.setPets(petList);

		LOGGER.debug("getPetsByPetParent pet count is {}", petList.size());
		LOGGER.debug("getPetsByPetParent completed successfully");
		return response;
	}

	@Override
	public PetObservationsResponse getPetObservations(PetObservationMediaFilter filter)
			throws ServiceExecutionException {
		LOGGER.debug("getPetObservations called");

		Map<String, Integer> mapper = petDao.getPetObservationsCount(filter);

		int searchCount = mapper.get("searchedElementsCount");
		int totalCount = mapper.get("totalCount");

		List<PetObservation> petObservations = totalCount > 0 ? petDao.getPetObservations(filter) : new ArrayList<>();

		PetObservationsResponse response = new PetObservationsResponse();
		response.setPetObservations(petObservations);
		response.setNoOfElements(petObservations.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(searchCount);

		response.setPetObservations(petObservations);
		LOGGER.debug("getPetObservations completed successfully");
		return response;
	}

	@Override
	public PetParentListResponse getPetParents(PetFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPetParents called");
		String counts = petDao.getPetParentsCount(filter);

		HashMap<String, Integer> countMap = new HashMap<>();
		int searchCount, totalCount = 0;
		try {
			countMap = new ObjectMapper().readValue(counts, HashMap.class);
			totalCount = countMap.get("totalCount");
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetParentListDTO> parentDTOs = totalCount > 0 ? petDao.getPetParents(filter) : new ArrayList<>();
		PetParentListResponse response = new PetParentListResponse();
		response.setPetParents(parentDTOs);
		response.setNoOfElements(parentDTOs.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(totalCount);
		LOGGER.debug("getPetParents completed successfully");
		return response;
	}

	@Override
	public PetNotesResponse getPetNotes(PetFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPetNotes called");
		String counts = petDao.getPetNotesCount(filter);
		HashMap<String, Integer> countMap = new HashMap<>();
		int searchCount, totalCount = 0;
		try {
			countMap = new ObjectMapper().readValue(counts, HashMap.class);
			totalCount = countMap.get("totalCount");
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetParentsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetNote> petNotes = totalCount > 0 ? petDao.getPetNotes(filter) : new ArrayList<>();
		PetNotesResponse response = new PetNotesResponse();
		response.setPetNotes(petNotes);
		response.setTotalRecords(totalCount);
		response.setSearchElments(totalCount);
		response.setNoOfElements(petNotes.size());
		LOGGER.debug("getPetNotes completed successfully");
		return response;
	}

	@Override
	public List<PetDevice> getPetDevices(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetDevices called");
		List<PetDevice> petDevices = petDao.getPetDevices(petId);
		LOGGER.debug("getPetDevices completed successfully");
		return petDevices;
	}

	@Override
	public void addPetNote(PetNote petNote) throws ServiceExecutionException {
		LOGGER.debug("addPetNote called");
		petDao.addPetNote(petNote);
		LOGGER.debug("addPetNote completed successfully");
	}

	@Override
	public PetObservationMediaListResponse getPetObservationMediaList(PetObservationMediaFilter filter)
			throws ServiceExecutionException {
		LOGGER.debug("getPetObservationMediaList called");
		Map<String, Integer> mapper = petDao.getPetsObservatonMediaCount(filter);

		int searchCount = mapper.get("searchedElementsCount");
		int totalCount = mapper.get("totalCount");
		// HashMap<String, Integer> map = new HashMap<>();

		List<PetObservationMediaListDTO> petObservationMediaList = totalCount > 0
				? petDao.getPetObservationMediaList(filter)
				: new ArrayList<>();

		PetObservationMediaListResponse response = new PetObservationMediaListResponse();
		response.setPetObservationMediaList(petObservationMediaList);
		response.setNoOfElements(petObservationMediaList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(searchCount);

//		LOGGER.debug("getPetObservationMediaList pet count is {}", petObservationMediaList);
		LOGGER.debug("getPetObservationMediaList completed successfully");
		return response;
	}

	@Override
	public List<PetObservationMediaListDTO> getPetObservationMediaById(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetById called");
		List<PetObservationMediaListDTO> petObservationMediaListDTO = petDao.getPetObservationMediaById(petId);
		LOGGER.debug("getPetById completed successfully");
		return petObservationMediaListDTO;
	}

	@Override
	public PetsResponse getPetViewPane(PetFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPetViewPane called");
		int searchedElements = 0;
		int userId = authentication.getAuthUserDetails().getUserId();
		int roleTypeId = authentication.getAuthUserDetails().getRoleTypeId();
		filter.setUserId(userId);
		filter.setRoleTypeId(roleTypeId);
		String counts = petDao.getPetViewPaneCount(filter);
		int total = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonCountObj = mapper.readTree(counts);
			total = jsonCountObj.get("totalCount").asInt();
			searchedElements = jsonCountObj.get("searchedElementsCount").asInt();

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetListDTO> pets = petDao.getPetViewPane(filter);

		PetsResponse response = new PetsResponse();
		response.setPetsList(pets);
		response.setNoOfElements(pets.size());
		response.setTotalRecords(total);
		response.setSearchElments(searchedElements);

		LOGGER.debug("getPetViewPane completed successfully");
		return response;
	}

	@Override
	public void disassociateStudy(int petId, int studyId) throws ServiceExecutionException {
		LOGGER.debug("disassociateStudy called");
		petDao.disassociateStudy(petId, studyId);
		LOGGER.debug("disassociateStudy completed successfully");
	}

	@Override
	public PetWeightHistoryResponse getPetWeightHistory(BaseFilter filter, int petId, String fromDate, String toDate)
			throws ServiceExecutionException {
		LOGGER.debug("getPetWeightHistory called");
		String counts = petDao.getPetWeightHistoryCount(filter, petId, fromDate, toDate);
		int total = 0;
		try {
			total = Integer.parseInt(counts);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetsCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetWeightHistoryDTO> petsWeightHistory = petDao.getPetWeightHistory(filter, petId, fromDate, toDate);
		PetWeightHistoryResponse response = new PetWeightHistoryResponse();
		response.setWeightList(petsWeightHistory);
		response.setNoOfElements(petsWeightHistory.size());
		response.setTotalRecords(total);

		LOGGER.debug("getPetWeightHistory end");

		return response;
	}

	@Override
	public List<PetStudyDTO> getCurrentStudies(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetByIdAndStudyId called");
		List<PetStudyDTO> petDTO = petDao.getCurrentStudies(petId);
		LOGGER.debug("getPetByIdAndStudyId completed successfully");
		return petDTO;
	}

	@Override
	public List<PetStudyDTO> getArchiveStudies(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetByIdAndStudyId called");
		List<PetStudyDTO> petDTO = petDao.getArchiveStudies(petId);
		LOGGER.debug("getPetByIdAndStudyId completed successfully");
		return petDTO;
	}

	@Override
	public PetDevicesResponse getPetDevicesByStudy(PetFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPetDevicesByStudy called");

		String counts = petDao.getPetDevicesByStudyCount(filter);
		int total = 0;
		try {
			total = Integer.parseInt(counts);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignListCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetDevice> petDevices = total > 0 ? petDao.getPetDevicesByStudy(filter) : new ArrayList<>();
		PetDevicesResponse response = new PetDevicesResponse();
		response.setNoOfElements(petDevices.size());
		response.setTotalRecords(total);
		response.setSearchElments(total);
		response.setPetDevices(petDevices);

		LOGGER.debug("getPetDevicesByStudy completed successfully");
		return response;
	}

	@Override
	public PetDTO getPetDetailsById(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetDetailsById called");
		PetDTO petDTO = petDao.getPetDetailsById(petId);
		LOGGER.debug("getPetDetailsById completed successfully");
		return petDTO;
	}

	@Override
	public PetCampaignPointsDTO getPetCampaignPoints(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetCampaignPoints called");
		PetCampaignPointsDTO petDTO = petDao.getPetCampaignPoints(petId);
		LOGGER.debug("getPetCampaignPoints completed successfully");
		return petDTO;
	}

	@Override
	public PetCampaignResponse getPetCampaignPointsList(int petId, int activityId, BaseFilter filter)
			throws ServiceExecutionException {
		LOGGER.debug("getPetCampaignPointsList called");
		String counts = petDao.getPetCampaignListCount(petId, activityId);
		int total = 0;
		try {
			total = Integer.parseInt(counts);
		} catch (Exception e) {
			LOGGER.error("error while fetching getPetCampaignListCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetCampaignPointsListDTO> campaignList = petDao.getPetCampaignPointsList(petId, activityId, filter);
		PetCampaignResponse response = new PetCampaignResponse();
		response.setNoOfElements(campaignList.size());
		response.setTotalRecords(total);
		response.setPetCampaignList(campaignList);

		LOGGER.debug("getPetCampaignPointsList end");

		return response;
	}

	@Override
	public PetRedemptionHistoryResponse getPetRedemptionHistory(int petId) throws ServiceExecutionException {
		LOGGER.debug("getPetRedemptionHistory called");

		List<PetRedemptionHistoryDTO> redemptionHistoryDTOs = petDao.getPetRedemptionHistory(petId);
		PetRedemptionHistoryResponse response = new PetRedemptionHistoryResponse();
		response.setNoOfElements(redemptionHistoryDTOs.size());
		response.setTotalRecords(redemptionHistoryDTOs.size());
		response.setRedemptionHistoryList(redemptionHistoryDTOs);

		LOGGER.debug("getPetRedemptionHistory end");

		return response;
	}

	@Override
	public void redeemRewardPoints(int petId, int pointsRedeemed, int userId) throws ServiceExecutionException {
		LOGGER.debug("redeemRewardPoints called");
		petDao.redeemRewardPoints(petId, pointsRedeemed, userId);
		LOGGER.debug("redeemRewardPoints completed successfully");
	}

	@Override
	public List<PetExternalInfoListDTO> getExternalPetInfoList(int studyId) throws ServiceExecutionException {
		LOGGER.debug("getExternalPetInfo start");
		List<PetExternalInfoListDTO> externalPetList = petDao.getExternalPetInfoList(studyId);
		LOGGER.debug("getExternalPetInfo end externalPetList size is " + externalPetList.size());
		return externalPetList;
	}

	@Override
	public ExternalPetInfoResponse getExternalPetInfo(ExternalPetInfoFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getExternalPetInfo called");
		List<ExternalPetInfoListDTO> externalPetInfoListDTOList = petDao.getExternalPetInfo(filter);
		ExternalPetInfoResponse response = new ExternalPetInfoResponse();
		response.setExternalPetInfoListDTOList(externalPetInfoListDTOList);
		LOGGER.debug("getExternalPetInfo completed successfully");
		return response;
	}

	@Override
	public PetDTO associateNewStudy(AssociateNewStudyRequest request) throws ServiceExecutionException {
		LOGGER.debug("associateNewStudy called");
		PetDTO petDTO = petDao.associateNewStudy(request);
		LOGGER.debug("associateNewStudy completed successfully");
		return petDTO;
	}

	@Override
	public PetDTO addPetWeight(AddPetWeight addPetWeight) throws ServiceExecutionException {
		LOGGER.debug("addPetWeight called");
		PetDTO petDTO = petDao.addPetWeight(addPetWeight);
		LOGGER.debug("addPetWeight completed successfully");
		return petDTO;
	}

	@Override
	public void updateWeight(int weightId, String weight, String unit) throws ServiceExecutionException {
		LOGGER.debug("disassociateStudy called");
		petDao.updateWeight(weightId, weight, unit);
		LOGGER.debug("disassociateStudy completed successfully");
	}

	@Override
	public PetFeedingEnthusiasmScaleResponse getPetFeedingEnthusiasmScaleDtls(PetEnthusiasmFilter filter)
			throws ServiceExecutionException {
		LOGGER.debug("getPetFeedingEnthusiasmScaleDtls called");
		int searchedElements = 0;
		String counts = petDao.getPetFeedingEnthusiasmScaleDtlsCount(filter);
		int total = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonCountObj = mapper.readTree(counts);
			total = jsonCountObj.get("totalCount").asInt();
			searchedElements = jsonCountObj.get("searchedElementsCount").asInt();

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetFeedingEnthusiasmScaleDtls", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetFeedingEnthusiasmScale> petEnthusiasmScaleList = total > 0
				? petDao.getPetFeedingEnthusiasmScaleDtls(filter)
				: new ArrayList<>();

		PetFeedingEnthusiasmScaleResponse response = new PetFeedingEnthusiasmScaleResponse();
		response.setPetFeedingEnthusiasmScaleDtls(petEnthusiasmScaleList);
		response.setNoOfElements(petEnthusiasmScaleList.size());
		response.setTotalRecords(total);
		response.setSearchElments(searchedElements);

		LOGGER.debug("getPetFeedingEnthusiasmScaleDtls pet count is {}", petEnthusiasmScaleList);
		LOGGER.debug("getPetFeedingEnthusiasmScaleDtls completed successfully");
		return response;
	}

	@Override
	public PetImageScaleResponse getPetImageScaleDtls(PetImageScaleFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPetImageScaleDtls called");
		int searchedElements = 0;
		String counts = petDao.getPetImageScaleDtlsCount(filter);
		int total = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonCountObj = mapper.readTree(counts);
			total = jsonCountObj.get("totalCount").asInt();
			searchedElements = jsonCountObj.get("searchedElementsCount").asInt();

		} catch (Exception e) {
			LOGGER.error("error while fetching getPetImageScaleDtls", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<PetImageScale> petImageScaleList = total > 0 ? petDao.getPetImageScaleDtls(filter) : new ArrayList<>();

		PetImageScaleResponse response = new PetImageScaleResponse();
		response.setPetImageScaleDtls(petImageScaleList);
		response.setNoOfElements(petImageScaleList.size());
		response.setTotalRecords(total);
		response.setSearchElments(searchedElements);

		LOGGER.debug("getPetImageScaleDtls pet count is {}", petImageScaleList);
		LOGGER.debug("getPetImageScaleDtls completed successfully");
		return response;
	}

	@Override
	public ActivityFactorResultResponseList getPetActivityFactorResult(PetActivityFactorFilter filter)
			throws ServiceExecutionException {
		LOGGER.debug("ActivityFactorResultResponseList called");
		int searchedElements = 0;
		String counts = petDao.ActivityFactorResultResponseListCount(filter);
		int total = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonCountObj = mapper.readTree(counts);
			total = jsonCountObj.get("totalCount").asInt();
			searchedElements = jsonCountObj.get("searchedElementsCount").asInt();

		} catch (Exception e) {
			LOGGER.error("error while fetching ActivityFactorResultResponseList", e);
			throw new ServiceExecutionException(e.getMessage());
		}

		List<ActivityFactorResultResponse> petAFResultList = total > 0 ? petDao.getPetActivityFactorResult(filter)
				: new ArrayList<>();
		/*
		 * List<ActivityFactorResultResponse> petAFResultList =
		 * getActivityFactorResultListTemp();
		 */

		ActivityFactorResultResponseList response = new ActivityFactorResultResponseList();
		response.setResultList(petAFResultList);
		response.setNoOfElements(petAFResultList.size());
		response.setTotalRecords(total);
		response.setSearchElments(searchedElements);

		LOGGER.debug("ActivityFactorResultResponseList pet count is {}", petAFResultList);
		LOGGER.debug("ActivityFactorResultResponseList completed successfully");
		return response;
	}

	/*
	 * private List<ActivityFactorResultResponse> getActivityFactorResultListTemp()
	 * { try { return new ObjectMapper().
	 * readValue("[{\"studyName\":\"CTE_Observation_Study\",\"extPetValue\":72,\"afCalculatedDate\":\"2022-05-06\",\"algorithmName\":\"Activity Factor\",\"algorithmVerion\":\"1.0\",\"traditionalEe\":\"\",\"estimatedEnergyExpenditure\":\"\",\"estimatedStepCount\":\"\",\"estimatedAf\":\"\",\"recommendedFeedAmt\":\"300\",\"feedUnits\":\"grams\"},{\"studyName\":\"WAM\",\"extPetValue\":72,\"afCalculatedDate\":\"2022-05-06\",\"algorithmName\":\"Activity Factor\",\"algorithmVerion\":\"1.0\",\"traditionalEe\":\"\",\"estimatedEnergyExpenditure\":\"\",\"estimatedStepCount\":\"\",\"estimatedAf\":\"\",\"recommendedFeedAmt\":\"200\",\"feedUnits\":\"grams\"},{\"studyName\":\"Hills Marvin\",\"extPetValue\":72,\"afCalculatedDate\":\"2022-05-06\",\"algorithmName\":\"Activity Factor\",\"algorithmVerion\":\"1.0\",\"traditionalEe\":\"\",\"estimatedEnergyExpenditure\":\"\",\"estimatedStepCount\":\"\",\"estimatedAf\":\"\",\"recommendedFeedAmt\":\"150\",\"feedUnits\":\"grams\"},{\"studyName\":\"Animal Derm-Vegas\",\"extPetValue\":72,\"afCalculatedDate\":\"2022-05-06\",\"algorithmName\":\"Activity Factor\",\"algorithmVerion\":\"1.0\",\"traditionalEe\":\"\",\"estimatedEnergyExpenditure\":\"\",\"estimatedStepCount\":\"\",\"estimatedAf\":\"\",\"recommendedFeedAmt\":\"250\",\"feedUnits\":\"grams\"}]"
	 * , ArrayList.class); } catch (JsonProcessingException e) {
	 * e.printStackTrace(); }; return new ArrayList<ActivityFactorResultResponse>();
	 * }
	 */
}
