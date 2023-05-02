package com.hillspet.wearables.dto;

public class PetParentName {
    private int petParentId;
    private String petParentName;
    private String petParentAddress;

    public int getPetParentId() {
        return petParentId;
    }

    public void setPetParentId(int petParentId) {
        this.petParentId = petParentId;
    }

    public String getPetParentName() {
        return petParentName;
    }

    public void setPetParentName(String petParentName) {
        this.petParentName = petParentName;
    }

    public String getPetParentAddress() {
        return petParentAddress;
    }

    public void setPetParentAddress(String petParentAddress) {
        this.petParentAddress = petParentAddress;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PetParentName{");
        sb.append("petParentId=").append(petParentId);
        sb.append(", petParentName='").append(petParentName).append('\'');
        sb.append(", petParentAddress='").append(petParentAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
