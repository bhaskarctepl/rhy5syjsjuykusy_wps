package com.hillspet.wearables.dao.analyticalReports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.AnalyticalReport;
import com.hillspet.wearables.dto.AnalyticalReportGroup;
import com.hillspet.wearables.dto.ManageReports;
import com.hillspet.wearables.dto.PointTracker;
import com.hillspet.wearables.dto.PreludeReport;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.PreludeReportFilter;

public interface AnalyticalReportsDao {

	List<AnalyticalReportGroup> getReportGroups() throws ServiceExecutionException;

	ManageReports addReport(ManageReports manageReports) throws ServiceExecutionException;
	
	ManageReports updateReport(ManageReports manageReports) throws ServiceExecutionException;
	
	ManageReports getReportById(int reportId) throws ServiceExecutionException;
	
	void deleteReport(int reportId, int modifiedBy) throws ServiceExecutionException;

	Map<String, Integer> getReportListCount(BaseFilter filter) throws ServiceExecutionException;

	List<AnalyticalReport> getReportList(BaseFilter filter) throws ServiceExecutionException;
	
	List<AnalyticalReport> getReportsByReportGroupId(BaseFilter filter) throws ServiceExecutionException;

	Map<String, Integer> getReportsByReportGroupIdCount(BaseFilter filter) throws ServiceExecutionException;
	
	List<PreludeReport> getPreludeReport(PreludeReportFilter filter) throws ServiceExecutionException;

	Map<String, Integer> getPreludeReportCount(PreludeReportFilter filter) throws ServiceExecutionException;
}
