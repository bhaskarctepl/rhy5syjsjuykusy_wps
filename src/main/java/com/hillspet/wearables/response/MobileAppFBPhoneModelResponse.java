package com.hillspet.wearables.response;

import com.hillspet.wearables.dto.MobileAppFBPhoneModel;
import com.hillspet.wearables.dto.MobileAppFeedbackPage;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;



        import java.util.List;

        import com.hillspet.wearables.dto.MobileAppFeedbackPage;

        import io.swagger.annotations.ApiModelProperty;


public class MobileAppFBPhoneModelResponse {
    private List<MobileAppFBPhoneModel> mobileAppFBPhoneModel;


    @ApiModelProperty(value = "List of details for Mobile App Phone Models")
    public List<MobileAppFBPhoneModel> getMobileAppFBPhoneModel() {
        return mobileAppFBPhoneModel;
    }

    public void setMobileAppFBPhoneModel(List<MobileAppFBPhoneModel> mobileAppFBPhoneModel) {
        this.mobileAppFBPhoneModel = mobileAppFBPhoneModel;
    }
}
