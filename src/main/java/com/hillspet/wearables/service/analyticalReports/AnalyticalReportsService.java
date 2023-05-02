package com.hillspet.wearables.service.analyticalReports;

import java.util.List;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.AnalyticalReportGroup;
import com.hillspet.wearables.dto.ManageReports;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.PreludeReportFilter;
import com.hillspet.wearables.response.AnalyticalReportResponse;
import com.hillspet.wearables.response.PreludeReportResponse;
import com.hillspet.wearables.response.GCSignedUrlResponse;

public interface AnalyticalReportsService {

	List<AnalyticalReportGroup> getReportGroups() throws ServiceExecutionException;

	public ManageReports addReport(ManageReports manageReports) throws ServiceExecutionException;

	public ManageReports updateReport(ManageReports manageReports) throws ServiceExecutionException;

	public ManageReports getReportById(int reportId) throws ServiceExecutionException;

	void deleteReport(int reportId, int modifiedBy) throws ServiceExecutionException;

	AnalyticalReportResponse getReportList(BaseFilter filter) throws ServiceExecutionException;

	AnalyticalReportResponse getReportsByReportGroupId(BaseFilter filter) throws ServiceExecutionException;

	PreludeReportResponse getPreludeReport(PreludeReportFilter filter) throws ServiceExecutionException;

	GCSignedUrlResponse getPreludeMediaSignedUrl(String mediaType, String filePath) throws ServiceExecutionException;
}
