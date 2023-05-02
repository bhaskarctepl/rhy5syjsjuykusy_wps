
/**
 * Created Date: Jan 10, 2021
 * Class Name  : DeviceDetailsReportService.java
 * Description : Description of the package.
 *
 * © Copyright 2020 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>        <Author>        <Date>        <Comments on Change>
 * ID                rmaram          Jan 17, 2021        Mentions the comments on change, for the new file it's not required.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.hillspet.wearables.service.reports;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.AssetByStudyWidgetFilter;
import com.hillspet.wearables.dto.CustomerSupportByCategory;
import com.hillspet.wearables.dto.CustomerSupportIssuesByStudy;
import com.hillspet.wearables.dto.TotalAssetsByStausWidgetFilter;
import com.hillspet.wearables.dto.filter.*;
import com.hillspet.wearables.response.*;

import java.util.List;

/**
 * This is the service class for implementing Device Details Report Service
 * details.
 * 
 * @author rmaram
 * @since w1.0
 * @version w1.0
 * 
 */
public interface ReportService {

	DeviceDetailsReportResponse getDeviceDetailsReport(BaseFilter filter) throws ServiceExecutionException;

	DeviceHistoryReportResponse getDeviceHistoryReport(BaseFilter filter) throws ServiceExecutionException;

	DeviceInventoryReportResponse getDeviceInventoryReport(BaseFilter filter) throws ServiceExecutionException;

	DeviceMalfunctionReportResponse getDeviceMalfunctionReport(BaseFilter filter) throws ServiceExecutionException;
	
	DeviceTrackingReportResponse getDeviceTrackingReport(BaseFilter filter) throws ServiceExecutionException;
	
	StudyBasedReportResponse getStudyBasedReport(BaseFilter filter,int userId) throws ServiceExecutionException;
	
	DevicesbyStudyResponse getAssetsDevicesByStudyReport(AssetByStudyWidgetFilter filter) throws ServiceExecutionException;
	
	DevicesMalfunctionsResponse getAssetsDevicesMalfunctionsReport(PointTrackerFilter filter) throws ServiceExecutionException;

	PointTrackerReportResponse getPointTrackerReport(PointTrackerFilter filter,int userId) throws ServiceExecutionException;

	public CustomerSupportListResponse getCustomerSupportTickets(SupportFilter filter) throws ServiceExecutionException;

    public List<CustomerSupportByCategory> getCustomerSupportIssueWidget(IssueWidgetFilter filter) throws ServiceExecutionException;

    public List<CustomerSupportIssuesByStudy> getCustomerSupportIssueByStudyWidget(IssueByStudyWidgetFilter filter) throws ServiceExecutionException;

    TotalAssetsByStatusReportResponse getTotalAssetsByStatus(TotalAssetsByStausWidgetFilter filter) throws ServiceExecutionException;

	TotalAssetsReportResponse getTotalAssets() throws ServiceExecutionException;

}
