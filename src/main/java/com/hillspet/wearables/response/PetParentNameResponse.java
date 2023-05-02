package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.PetParentName;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetParentNameResponse {
    List<PetParentName> petNameList;

    public List<PetParentName> getPetNameList() {
        return petNameList;
    }

    public void setPetNameList(List<PetParentName> petNameList) {
        this.petNameList = petNameList;
    }
}
