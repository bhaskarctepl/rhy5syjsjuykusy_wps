package com.hillspet.wearables.service.pet;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetDevice;
import com.hillspet.wearables.dto.PetExternalInfoListDTO;
import com.hillspet.wearables.dto.PetListDTO;
import com.hillspet.wearables.dto.PetNote;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.dto.PetObservationMediaListDTO;
import com.hillspet.wearables.dto.PetStudyDTO;
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

public interface PetService {

	PetsResponse getPetList(PetFilter filter) throws ServiceExecutionException;

	List<PetListDTO> getPets() throws ServiceExecutionException;

	PetDTO addPet(PetRequest petRequest) throws ServiceExecutionException;

	PetDTO updatePet(PetRequest petRequest) throws ServiceExecutionException;

	PetDTO getPetById(int petId) throws ServiceExecutionException;

	PetDTO getPetByIdAndStudyId(int petId, int studyId) throws ServiceExecutionException;

	void deletePet(int petId, int modifiedBy) throws ServiceExecutionException;

	PetsResponse getPetsByPetParent(int petParentId) throws ServiceExecutionException;

	PetObservationsResponse getPetObservations(PetObservationMediaFilter filter) throws ServiceExecutionException;

	PetParentListResponse getPetParents(PetFilter filter) throws ServiceExecutionException;

	PetNotesResponse getPetNotes(PetFilter filter) throws ServiceExecutionException;

	List<PetDevice> getPetDevices(int petId) throws ServiceExecutionException;

	void addPetNote(PetNote petNote) throws ServiceExecutionException;

	PetObservationMediaListResponse getPetObservationMediaList(PetObservationMediaFilter filter)
			throws ServiceExecutionException;

	List<PetObservationMediaListDTO> getPetObservationMediaById(int petId) throws ServiceExecutionException;

	PetsResponse getPetViewPane(PetFilter filter) throws ServiceExecutionException;

	void disassociateStudy(int petId, int studyId) throws ServiceExecutionException;

	PetWeightHistoryResponse getPetWeightHistory(BaseFilter filter, int petId, String fromDate, String toDate)
			throws ServiceExecutionException;

	List<PetStudyDTO> getCurrentStudies(int petId) throws ServiceExecutionException;

	List<PetStudyDTO> getArchiveStudies(int petId) throws ServiceExecutionException;

	PetDevicesResponse getPetDevicesByStudy(PetFilter filter) throws ServiceExecutionException;

	PetDTO getPetDetailsById(int petId) throws ServiceExecutionException;

	PetCampaignPointsDTO getPetCampaignPoints(int petId) throws ServiceExecutionException;

	PetCampaignResponse getPetCampaignPointsList(int petId, int activityId, BaseFilter filter)
			throws ServiceExecutionException;

	PetRedemptionHistoryResponse getPetRedemptionHistory(int petId) throws ServiceExecutionException;

	void redeemRewardPoints(int petId, int pointsRedeemed, int userId) throws ServiceExecutionException;

	List<PetExternalInfoListDTO> getExternalPetInfoList(int studyId) throws ServiceExecutionException;

	public ExternalPetInfoResponse getExternalPetInfo(ExternalPetInfoFilter filter) throws ServiceExecutionException;

	public PetDTO associateNewStudy(AssociateNewStudyRequest request) throws ServiceExecutionException;

	public PetDTO addPetWeight(AddPetWeight addPetWeight) throws ServiceExecutionException;

	void updateWeight(int weightId, String weight, String unit) throws ServiceExecutionException;

	public PetFeedingEnthusiasmScaleResponse getPetFeedingEnthusiasmScaleDtls(PetEnthusiasmFilter filter)
			throws ServiceExecutionException;

	public PetImageScaleResponse getPetImageScaleDtls(PetImageScaleFilter filter) throws ServiceExecutionException;

	public ActivityFactorResultResponseList getPetActivityFactorResult(PetActivityFactorFilter filter) throws ServiceExecutionException;

	
}
