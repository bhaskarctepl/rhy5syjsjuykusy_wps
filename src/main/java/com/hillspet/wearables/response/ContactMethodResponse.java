package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.ContactMethod;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactMethodResponse {
    private List<ContactMethod> contactMethodList;

    public List<ContactMethod> getContactMethodList() {
        return contactMethodList;
    }

    public void setContactMethodList(List<ContactMethod> contactMethodList) {
        this.contactMethodList = contactMethodList;
    }
}
