package com.hillspet.wearables.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hillspet.wearables.dto.CustomerSupportHistory;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerSupportTicketHistoryResponse {
    private List<CustomerSupportHistory> customerSupport;

    @JsonProperty("rows")
    @ApiModelProperty(value = "Get the Support tickets based on search criteria")

    public List<CustomerSupportHistory> getCustomerSupport() {
        return customerSupport;
    }

    public void setCustomerSupport(List<CustomerSupportHistory> customerSupport) {
        this.customerSupport = customerSupport;
    }
}
