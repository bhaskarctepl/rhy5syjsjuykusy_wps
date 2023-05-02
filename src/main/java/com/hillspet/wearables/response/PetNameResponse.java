package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetName;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetNameResponse {
    private List<PetName> petNameList;

    public List<PetName> getPetNameList() {
        return petNameList;
    }

    public void setPetNameList(List<PetName> petNameList) {
        this.petNameList = petNameList;
    }
}
