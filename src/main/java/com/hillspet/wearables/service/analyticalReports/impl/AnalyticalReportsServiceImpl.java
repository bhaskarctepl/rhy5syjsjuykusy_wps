package com.hillspet.wearables.service.analyticalReports.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.constants.Constants;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.common.utils.GCPClientUtil;
import com.hillspet.wearables.dao.analyticalReports.AnalyticalReportsDao;
import com.hillspet.wearables.dto.AnalyticalReport;
import com.hillspet.wearables.dto.AnalyticalReportGroup;
import com.hillspet.wearables.dto.ManageReports;
import com.hillspet.wearables.dto.PreludeReport;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.PreludeReportFilter;
import com.hillspet.wearables.response.AnalyticalReportResponse;
import com.hillspet.wearables.response.GCSignedUrlResponse;
import com.hillspet.wearables.response.PreludeReportResponse;
import com.hillspet.wearables.service.analyticalReports.AnalyticalReportsService;

@Service
public class AnalyticalReportsServiceImpl implements AnalyticalReportsService {

	private static final Logger LOGGER = LogManager.getLogger(AnalyticalReportsServiceImpl.class);

	@Autowired
	private AnalyticalReportsDao analyticalReportsDao;

	@Autowired
	private GCPClientUtil gcpClientUtil;

	@Value("${gcp.uploadFolderPath}")
	private String uploadFolderPath;

	@Value("${gcp.bucketName}")
	private String bucketName;

	@Value("${gcp.env}")
	private String environment;

	@Override
	public List<AnalyticalReportGroup> getReportGroups() throws ServiceExecutionException {
		LOGGER.debug("getReportGroups called");
		List<AnalyticalReportGroup> analyticalReportGroupList = analyticalReportsDao.getReportGroups();
		LOGGER.debug("getReportGroups list", analyticalReportGroupList);
		return analyticalReportGroupList;
	}

	@Override
	public ManageReports addReport(ManageReports manageReports) throws ServiceExecutionException {

		LOGGER.debug("addPointTracker called");

		manageReports = analyticalReportsDao.addReport(manageReports);
		LOGGER.debug("addPointTracker called");
		return manageReports;

	}

	@Override
	public ManageReports updateReport(ManageReports manageReports) throws ServiceExecutionException {

		LOGGER.debug("updatePointTracker called");

		manageReports = analyticalReportsDao.updateReport(manageReports);
		LOGGER.debug("updatePointTracker called");
		return manageReports;

	}

	@Override
	public ManageReports getReportById(int reportId) throws ServiceExecutionException {
		LOGGER.info("getReportById called");
		return analyticalReportsDao.getReportById(reportId);
	}

	@Override
	public void deleteReport(int reportId, int modifiedBy) throws ServiceExecutionException {
		LOGGER.info("deleteReport called");
		analyticalReportsDao.deleteReport(reportId, modifiedBy);
	}

	@Override
	public AnalyticalReportResponse getReportList(BaseFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPlans called");
		Map<String, Integer> mapper = analyticalReportsDao.getReportListCount(filter);
		int total = mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<AnalyticalReport> manageReportList = total > 0 ? analyticalReportsDao.getReportList(filter)
				: new ArrayList<>();
		AnalyticalReportResponse response = new AnalyticalReportResponse();
		response.setAnalyticalReportList(manageReportList);
		response.setNoOfElements(manageReportList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		LOGGER.debug("getReportList  count is {}", manageReportList);
		LOGGER.debug("getReportList completed successfully");
		return response;
	}

	@Override
	public AnalyticalReportResponse getReportsByReportGroupId(BaseFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPlans called");
		Map<String, Integer> mapper = analyticalReportsDao.getReportsByReportGroupIdCount(filter);
		int total = mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<AnalyticalReport> manageReportList = total > 0 ? analyticalReportsDao.getReportsByReportGroupId(filter)
				: new ArrayList<>();
		AnalyticalReportResponse response = new AnalyticalReportResponse();
		response.setAnalyticalReportList(manageReportList);
		response.setNoOfElements(manageReportList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		LOGGER.debug("getReportList  count is {}", manageReportList);
		LOGGER.debug("getReportList completed successfully");
		return response;
	}

	@Override
	public PreludeReportResponse getPreludeReport(PreludeReportFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getPreludeReport called");
		Map<String, Integer> mapper = analyticalReportsDao.getPreludeReportCount(filter);
		int total = mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<PreludeReport> preludeReportList = total > 0 ? analyticalReportsDao.getPreludeReport(filter)
				: new ArrayList<>();
		PreludeReportResponse response = new PreludeReportResponse();
		response.setPreludeReportList(preludeReportList);
		response.setNoOfElements(preludeReportList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		LOGGER.debug("getPreludeReport  count is {}", preludeReportList);
		LOGGER.debug("getPreludeReport completed successfully");
		return response;
	}

	@Override
	public GCSignedUrlResponse getPreludeMediaSignedUrl(String mediaType, String filePath)
			throws ServiceExecutionException {
		LOGGER.info("ServiceImpl getPreludeMediaSignedUrl called =========== {}", filePath);
		String signedUrl = "";

		if (StringUtils.isNotBlank(mediaType) && StringUtils.isNotBlank(filePath)) {
			if (mediaType.equalsIgnoreCase("Video")) {
				if (!StringUtils.contains(filePath, "firebasestorage")) {

					filePath = filePath.replaceAll("https://storage.googleapis.com/" + bucketName + "/" + environment
							+ "/GCloud/WPortal/ObservationVideo/", "");

					signedUrl = gcpClientUtil.getDownloaFiledUrl(filePath, Constants.GCP_OBSERVATION_VIDEO_PATH);

				} else {
					signedUrl = filePath;
				}
			}

			if (mediaType.equalsIgnoreCase("Image")) {
				if (!StringUtils.contains(filePath, "firebasestorage")) {
					signedUrl = gcpClientUtil.getDownloaFiledUrl(filePath, Constants.GCP_OBSERVATION_PHOTO_PATH);
				} else {
					signedUrl = filePath;
				}
			}
		}

		LOGGER.info("signedUrl::" + signedUrl);
		GCSignedUrlResponse response = new GCSignedUrlResponse();
		response.setSignedUrl(signedUrl);

		return response;
	}

}
