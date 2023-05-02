/**
 * Created Date: Jan 15, 2021
 * Class Name  : DeviceDetailsReportDao.java
 * Description : Description of the package.
 *
 * © Copyright 2020 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>        <Author>        <Date>        <Comments on Change>
 * ID                rmaram          Jan 11, 2021        Mentions the comments on change, for the new file it's not required.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.hillspet.wearables.dao.reports;

import java.util.List;
import java.util.Map;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.*;
import com.hillspet.wearables.dto.filter.*;
import com.hillspet.wearables.response.TotalAssetsByStatusReportResponse;
import com.hillspet.wearables.response.TotalAssetsReportResponse;

/**
 * Gets DeviceDetailsReportDao the db.
 * 
 * @author rmaram
 * @since - w1.0
 * @version - w1.0
 */
public interface ReportDao {

	public Map<String, Integer> getDeviceDetailsReportCount(BaseFilter filter) throws ServiceExecutionException;

	public List<DeviceDetailsReport> getDeviceDetailsReport(BaseFilter filter) throws ServiceExecutionException;

	public Map<String, Integer> getDeviceHistoryReportCount(BaseFilter filter) throws ServiceExecutionException;

	public List<DeviceHistoryReport> getDeviceHistoryReport(BaseFilter filter) throws ServiceExecutionException;

	public Map<String, Integer> getDeviceInventoryReportCount(BaseFilter filter) throws ServiceExecutionException;

	public List<DeviceInventoryReport> getDeviceInventoryReport(BaseFilter filter) throws ServiceExecutionException;

	public Map<String, Integer> getDeviceMalfunctionReportCount(BaseFilter filter) throws ServiceExecutionException;

	public List<DeviceMalfunctionReport> getDeviceMalfunctionReport(BaseFilter filter) throws ServiceExecutionException;

	public Map<String, Integer> getDeviceTrackingReportCount(BaseFilter filter) throws ServiceExecutionException;

	public List<DeviceTrackingReport> getDeviceTrackingReport(BaseFilter filter) throws ServiceExecutionException;

	Map<String, Integer> getStudyBasedReportCount(BaseFilter filter,int userId) throws ServiceExecutionException;

	public List<StudyBasedReport> getStudyBasedReport(BaseFilter filter, int userId) throws ServiceExecutionException;
	
	public List<DevicesbyStudyReport> getAssetsDevicesByStudyReport(AssetByStudyWidgetFilter filter) throws ServiceExecutionException;

	public List<DevicesMalfunctionsReport> getAssetsDevicesMalfunctionsReport(PointTrackerFilter filter) throws ServiceExecutionException;

	int getAssetsDevicesByStudyReportCount(BaseFilter filter) throws ServiceExecutionException;

	int getAssetsDevicesMalfunctionsReportCount(BaseFilter filter) throws ServiceExecutionException;

	public Map<String, Integer> getPointTrackerReportCount(PointTrackerFilter filter,int userId) throws ServiceExecutionException;

	public List<PointTrackerReport> getPointTrackerReport(PointTrackerFilter filter,int userId) throws ServiceExecutionException;

	public Map<String, Integer> getCustomerSupportTicketsCount(SupportFilter filter) throws ServiceExecutionException;

	public List<SupportListDTO> getCustomerSupportTickets(SupportFilter filter) throws ServiceExecutionException;

	public List<CustomerSupportByCategory> getCustomerSupportIssueWidget(IssueWidgetFilter filter) throws ServiceExecutionException;

	public List<CustomerSupportIssuesByStudy> getCustomerSupportIssueByStudyWidget(IssueByStudyWidgetFilter filter) throws ServiceExecutionException;

	public List<TotalAssetsbyStatusListDTO> getTotalAssetsByStatus(TotalAssetsByStausWidgetFilter filter) throws ServiceExecutionException;
	public List<TotalAssetsListDTO> getTotalAssets() throws ServiceExecutionException;
	
}
