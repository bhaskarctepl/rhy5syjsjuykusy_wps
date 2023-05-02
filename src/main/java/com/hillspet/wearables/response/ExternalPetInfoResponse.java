package com.hillspet.wearables.response;

import com.hillspet.wearables.dto.ExternalPetInfoListDTO;

import java.util.List;

public class ExternalPetInfoResponse {
    private List<ExternalPetInfoListDTO> externalPetInfoListDTOList;

    public List<ExternalPetInfoListDTO> getExternalPetInfoListDTOList() {
        return externalPetInfoListDTOList;
    }

    public void setExternalPetInfoListDTOList(List<ExternalPetInfoListDTO> externalPetInfoListDTOList) {
        this.externalPetInfoListDTOList = externalPetInfoListDTOList;
    }
}
