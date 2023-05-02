/**
 * Created Date: Mar 17, 2021 12:50:00 PM
 * Class Name  : PreludeJob.java
 * � Copyright 2008 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>	<Author>	<Date>	<Comments on Change>
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package com.hillspet.wearables.dao.study.impl;

import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.BaseDaoImpl;

/**
 * 
 * @author sgorle
 * @date: 14-09-2022
 *
 */
@Repository
public class PreludeDaoImpl extends BaseDaoImpl {

	private static final Logger LOGGER = LogManager.getLogger(PreludeDaoImpl.class);

	private static final String PRELUDE_GET_NEW_PET_PARENT_LIST = "PRELUDE_GET_NEW_PET_PARENT_LIST";
	private static final String PRELUDE_GET_NEW_PET_LIST = "PRELUDE_GET_NEW_PET_LIST";
	private static final String PRELUDE_UPDATE_PET_STUDY = "PRELUDE_UPDATE_PET_STUDY";

	private static final String STUDY_LIST_QUERY = "SELECT * FROM B_EXTERNAL_STUDY_INFO WHERE EXT_STUDY_STATUS = 1 AND IS_DELETED = 0 AND (URL IS NOT NULL AND TRIM(URL) != '' AND TRIM(URL) LIKE '%http%') AND EXT_STUDY_ID IN ('WAM2')";

	private static final String SITE_LIST_QUERY = "SELECT B_EXTERNAL_SITE_INFO.*, B_EXTERNAL_STUDY_INFO.URL FROM B_EXTERNAL_SITE_INFO "
			+ " LEFT JOIN B_EXTERNAL_STUDY_INFO ON B_EXTERNAL_STUDY_INFO.EXT_STUDY_ID = B_EXTERNAL_SITE_INFO.EXTERNAL_STUDY_ID "
			+ " WHERE B_EXTERNAL_SITE_INFO.STATUS=1 and B_EXTERNAL_STUDY_INFO.IS_DELETED = 0 AND B_EXTERNAL_STUDY_INFO.EXT_STUDY_ID IN (?)";

	private static final String PATIENT_LIST_QUERY = "SELECT * FROM B_EXTERNAL_PET_INFO WHERE EXT_SITE_ID = ?";

	private static final String EXT_STUDY_EXTRACT_DEFINITION_QUERY = "SELECT * FROM B_EXTERNAL_STUDY_EXTRACT_DEF "
			+ " WHERE CATEGORY = 'Demographics' AND EXT_STUDY_ID = ? ORDER BY EXTRACT_DEF_ID ASC  ";

	private static final String NEED_UPDATE_EXT_STUDY_EXTRACT_DEFINITION_QUERY = "SELECT * FROM B_EXTERNAL_STUDY_EXTRACT_DEF WHERE EXT_STUDY_ID =  ? "
			+ "AND IS_PORTAL_CONFIGURED = 1 ";

	private static final String EXT_STUDY_EXTRACT_INFO_BY_EXT_PATIENTID_QUERY = "SELECT * FROM B_EXTERNAL_STUDY_EXTRACT_INFO WHERE EXT_PET_ID = ? "
			+ "AND CATEGORY = 'Demographics' ORDER BY EXTRACT_DEF_ID ASC";

	private static final String CHK_QUERY_B_EXTERNAL_SITE_INFO = "SELECT distinct EXTERNAL_SITE_ID FROM B_EXTERNAL_SITE_INFO WHERE EXTERNAL_SITE_ID = ? ";

	private static final String INSERT_QUERY_B_EXTERNAL_SITE_INFO = "INSERT INTO B_EXTERNAL_SITE_INFO "
			+ "(EXTERNAL_SITE_ID, EXTERNAL_SITE_NAME, EXTERNAL_STUDY_ID, STATUS, IS_DELETED, "
			+ " CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (?, ?, ?, 1, 0, 1, now(), 1, now())";

	private static final String CHK_QUERY_B_EXTERNAL_PET_INFO = "SELECT distinct EXT_PET_ID FROM B_EXTERNAL_PET_INFO WHERE EXT_PET_ID = ? ";

	private static final String INSERT_QUERY_B_EXTERNAL_PET_INFO = "INSERT INTO B_EXTERNAL_PET_INFO "
			+ "(EXT_PET_ID, EXT_PET_VALUE, PET_ID, EXT_SITE_ID, EXT_TREATMENT_GROUP, IS_NOTIFICATION_SENT, IS_DELETED, "
			+ " CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (?, ?, NULL, ?, NULL, 0, 0, 1, now(), 1, now())";

	private static final String CHK_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO = "select EXTRACT_INFO_ID from B_EXTERNAL_STUDY_EXTRACT_INFO WHERE EXT_PET_ID = ? AND EXTRACT_DEF_ID = ?";

	private static final String INSERT_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO = "INSERT INTO B_EXTERNAL_STUDY_EXTRACT_INFO "
			+ "(EXTRACT_DEF_ID, EXT_PET_ID, CATEGORY, FIELD_NAME, FIELD_VALUE, JSON_VALUE, IS_DELETED, "
			+ " CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (?, ?, ?, ?, ?, ?, 0, 1, now(), 1, now())";

	private static final String UPDATE_NOTIFICATION_B_EXTERNAL_PLAN_EXTRACT_INFO = "UPDATE B_EXTERNAL_PET_INFO SET IS_NOTIFICATION_SENT = 1, MODIFIED_DATE = now() WHERE EXT_PET_ID = ?";

	private static final String CHK_UNIQUE_REC_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO = "select EXTRACT_INFO_ID from B_EXTERNAL_STUDY_EXTRACT_INFO WHERE EXT_PET_ID = ? AND EXTRACT_DEF_ID = ?"
			+ " AND FIELD_NAME = ? AND FIELD_VALUE = ? AND CATEGORY = ? ";

	private static final String UPDATE_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO = "UPDATE B_EXTERNAL_STUDY_EXTRACT_INFO "
			+ "SET FIELD_NAME = ?, FIELD_VALUE = ?, CATEGORY = ? , JSON_VALUE = ? , MODIFIED_DATE = NOW() "
			+ "WHERE EXT_PET_ID = ? AND EXTRACT_DEF_ID = ?";

	/**
	 * 
	 * @return
	 * @throws SQLNonTransientConnectionException
	 * 
	 */
	public List<Map<String, Object>> getExtStudyList() throws SQLNonTransientConnectionException {

		LOGGER.info("Inside the PreludeDaoImpl.getExtStudyList() ::   " + STUDY_LIST_QUERY);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = select(STUDY_LIST_QUERY);
		return list;
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws java.sql.SQLNonTransientConnectionException
	 * 
	 */
	public List<Map<String, Object>> getExtSiteInfoList(String studyId) throws SQLNonTransientConnectionException {

		LOGGER.info("Inside the PreludeDaoImpl.getExtSiteInfo()   :::::::::::::::::   ");
		List<Map<String, Object>> siteList = select(SITE_LIST_QUERY, studyId);
		LOGGER.info("siteList  :::::   " + siteList);
		return siteList;
	}

	/**
	 * 
	 * 
	 * @param siteId
	 * @return
	 * @throws SQLNonTransientConnectionException
	 * 
	 */
	public List<Map<String, Object>> getExtPatientInfoList(String siteId) throws SQLNonTransientConnectionException {

		LOGGER.info("Inside the PreludeDaoImpl.getExtPatientInfoList()   ::   siteInfoId  : " + siteId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = select(PATIENT_LIST_QUERY, siteId);
		LOGGER.info("list  :::::   " + list);
		return list;
	}

	/**
	 * 
	 * 
	 * @param studyId
	 * @return
	 * @throws SQLNonTransientConnectionException
	 * 
	 */
	public List<Map<String, Object>> getExtStudyExtractDefinitionList(String studyId)
			throws SQLNonTransientConnectionException {

		LOGGER.info("Inside the PreludeDaoImpl.getExtStudyExtractDefinitionList()  ::::   siteInfoId : " + studyId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = select(EXT_STUDY_EXTRACT_DEFINITION_QUERY, studyId);

		LOGGER.info("list  :::::   " + list);
		return list;
	}

	public List<Map<String, Object>> GetNeedUpdateExtStudyExtractDefinitionList(String studyId)
			throws java.sql.SQLNonTransientConnectionException {

		LOGGER.info(
				"Inside the PreludeDaoImpl.GetNeedUpdateExtStudyExtractDefinitionList()   :::::::::::::::::   siteInfoId  : "
						+ studyId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = select(NEED_UPDATE_EXT_STUDY_EXTRACT_DEFINITION_QUERY, studyId);
		LOGGER.info("list  :::::   " + list);

		return list;
	}

	public List<Map<String, Object>> getExtStudyExtractInfoList(String extPatientID)
			throws java.sql.SQLNonTransientConnectionException {

		LOGGER.info("Inside the PreludeDaoImpl.getExtStudyExtractInfoList()   :::::::::::::::::   extPatientID  : "
				+ extPatientID);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = select(EXT_STUDY_EXTRACT_INFO_BY_EXT_PATIENTID_QUERY, extPatientID);
		LOGGER.info("list  :::::   " + list);

		return list;
	}

	@SuppressWarnings("unchecked")
	public void insertExtSiteInfo(String study, List<Object> siteList) {

		LOGGER.info("Inside the PreludeDaoImpl.insertExtSiteInfo()   ::::   siteList  : " + siteList.toString());
		try {
			for (Object siteMap : siteList) {
				LOGGER.info(" siteMap  :::  " + siteMap);

				Map<String, Object> map = new ObjectMapper().convertValue(siteMap, Map.class);
				LOGGER.info("map  ::  " + map);

				List<Map<String, Object>> list = select(CHK_QUERY_B_EXTERNAL_SITE_INFO, map.get("db"));

				if (list.size() == 0) {
					try {
						insert(INSERT_QUERY_B_EXTERNAL_SITE_INFO,
								new Object[] { map.get("db").toString(), map.get("ui").toString(), study });
						LOGGER.info("Site record inserted successfully ::::    study : " + study + " map : " + map);
					} catch (Exception e) {
						LOGGER.info("Error oocured inside insertExtSiteInfo " + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error oocured inside insertExtSiteInfo " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void insertExtPetInfo(String extSiteID, List<Object> petList) {

		LOGGER.info("Inside the PreludeDaoImpl.insertExtPetInfo()   :::::::::::::::::  " + " extSiteID   " + extSiteID
				+ "  petList  : " + petList.toString());

		try {
			for (Object petInfoMap : petList) {

				LOGGER.info(" petInfoMap  :::  " + petInfoMap);

				ObjectMapper oMapper = new ObjectMapper();
				Map<String, Object> map = oMapper.convertValue(petInfoMap, Map.class);

				List<Map<String, Object>> list = select(CHK_QUERY_B_EXTERNAL_PET_INFO, map.get("db"));
				if (list.size() == 0) {
					LOGGER.info("extSiteID  ::::  " + extSiteID + " map  :::  " + map);
					try {
						insert(INSERT_QUERY_B_EXTERNAL_PET_INFO,
								new Object[] { map.get("db").toString(), map.get("ui").toString(), extSiteID });
						LOGGER.info(
								"Pet record inserted successfully ::::    extSiteID : " + extSiteID + " map : " + map);
					} catch (Exception e) {
						LOGGER.info("Error oocured inside insertExtPetInfo() while inserting site record :::   "
								+ e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Error oocured inside insertExtPetInfo " + e.getMessage());
		}
	}

	public void insertExtStudyExtractInfo(JSONObject patientDemographicJson) {

		LOGGER.info("Inside the PreludeDaoImpl.insertExtStudyExtractInfo()   :::::::::::::::::  ");
		LOGGER.info("patientDemographicJson  :::  " + patientDemographicJson);
		try {

			List<Map<String, Object>> list = select(CHK_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO, new Object[] {
					patientDemographicJson.get("extPatientID"), patientDemographicJson.get("extractDefID") });

			LOGGER.info("list size :::  " + list.size());
			if (list.size() == 0) {
				try {
					long id = insertNew(INSERT_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO,
							new Object[] { patientDemographicJson.get("extractDefID"),
									patientDemographicJson.get("extPatientID"), patientDemographicJson.get("category"),
									patientDemographicJson.get("fieldName"), patientDemographicJson.get("fieldValue"),
									patientDemographicJson.toString() });
					LOGGER.info("Pet record inserted successfully ::::    id : " + id);
				} catch (Exception e) {
					LOGGER.info("Error oocured inside insertExtStudyExtractInfo() while inserting site record :::   "
							+ e.getMessage());
				}
			}

		} catch (Exception e) {
			LOGGER.info("Error oocured inside insertExtPetInfo " + e.getMessage());
		}
	}

	public void updateExtPetNotificationStatus(String extPetId) {

		LOGGER.info("Inside the PreludeDaoImpl.updateExtPetInfo()   ::::::   extPetId  : " + extPetId);

		try {
			update(UPDATE_NOTIFICATION_B_EXTERNAL_PLAN_EXTRACT_INFO, new Object[] { extPetId });
			LOGGER.info("Pey record updated successfully ::::    extPetId : " + extPetId);
		} catch (Exception e) {
			LOGGER.info("Error oocured inside updateExtPetInfo() while updating prt record " + e.getMessage());
		}
	}

	public int insertOrUpdateExtStudyExtractInfo(JSONObject patientDemographicJson) {

		LOGGER.info("Inside the PreludeDaoImpl.insertOrUpdateExtStudyExtractInfo()   :::::::::::::::::  ");
		LOGGER.info("patientDemographicJson  :::  " + patientDemographicJson);
		int i = 0;
		try {

			List<Map<String, Object>> list = select(CHK_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO, new Object[] {
					patientDemographicJson.get("extPatientID"), patientDemographicJson.get("extractDefID") });

			LOGGER.info("list size :::  " + list.size());
			if (list.size() == 0) {
				try {
					long id = insertNew(INSERT_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO,
							new Object[] { patientDemographicJson.get("extractDefID"),
									patientDemographicJson.get("extPatientID"), patientDemographicJson.get("category"),
									patientDemographicJson.get("fieldName"), patientDemographicJson.get("fieldValue"),
									patientDemographicJson.toString() });
					LOGGER.info("Pet record inserted successfully ::::    id : " + id);
					i = 1;
				} catch (Exception e) {
					LOGGER.info(
							"Error oocured inside insertOrUpdateExtStudyExtractInfo() while inserting site record :::   "
									+ e.getMessage());
				}
			} else {

				List<Map<String, Object>> uniqueList = select(CHK_UNIQUE_REC_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO,
						new Object[] { patientDemographicJson.get("extPatientID"),
								patientDemographicJson.get("extractDefID"), patientDemographicJson.get("fieldName"),
								patientDemographicJson.get("fieldValue"), patientDemographicJson.get("category") });

				LOGGER.info("uniqueList size :::  " + uniqueList.size());

				if (uniqueList.size() == 0) {
					try {
						update(UPDATE_QUERY_B_EXTERNAL_PLAN_EXTRACT_INFO,
								new Object[] { patientDemographicJson.get("fieldName"),
										patientDemographicJson.get("fieldValue"),
										patientDemographicJson.get("category"), patientDemographicJson.toString(),
										patientDemographicJson.get("extPatientID"),
										patientDemographicJson.get("extractDefID") });
						LOGGER.info("Pey record updated successfully ::::    extPetId : "
								+ patientDemographicJson.get("extPatientID") + " extractDefID : "
								+ patientDemographicJson.get("extractDefID"));
						i = 1;
					} catch (Exception e) {
						LOGGER.info(
								"Error oocured inside updateExtPetInfo() while updating prt record " + e.getMessage());
					}
				}

			}

		} catch (Exception e) {
			LOGGER.info("Error oocured inside insertExtPetInfo " + e.getMessage());
		}
		return i;
	}

	/**
	 * Calling the Prelude-Portal sync procedures
	 */
	public void callCreateExtPetParentProc() {
		LOGGER.info(" ******** Start Of callExtPetParentSyncProc() *********** ");

		try {
			Map<String, Object> outParams = callStoredProcedure(PRELUDE_GET_NEW_PET_PARENT_LIST);

			String errorMsg = (String) outParams.get("out_error_msg");

			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isNotEmpty(errorMsg) || (int) statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("deleteRole validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList());
					// Arrays.asList(new WearablesError("Error while creating Prelude Pet
					// Parents")));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException("Inserta Pet Parent validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList());
					// Arrays.asList(new WearablesError("Insert Pet Parent validation failed")));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing callCreateExtPetParentProc ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	/**
	 * Calling the Prelude-Portal sync procedures
	 */
	public void callCreateExtPetProc() {
		LOGGER.info(" ******** Start Of callCreateExtPetProc() *********** ");

		try {
			Map<String, Object> outParams = callStoredProcedure(PRELUDE_GET_NEW_PET_LIST);
			String errorMsg = (String) outParams.get("out_error_msg");

			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isNotEmpty(errorMsg) || (int) statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("deleteRole validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList());
					// Arrays.asList(new WearablesError("Error while creating Prelude Pet
					// Parents")));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException("Inserta Pet Parent validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList());
					// Arrays.asList(new WearablesError("Insert Pet Parent validation failed")));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing callCreateExtPetProc ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	/**
	 * Calling the Prelude-Portal sync procedures
	 */
	public void callUpdateExtPetProc() {
		LOGGER.info(" ******** Start Of callUpdateExtPetProc() *********** ");

		try {
			Map<String, Object> outParams = callStoredProcedure(PRELUDE_UPDATE_PET_STUDY);
			String errorMsg = (String) outParams.get("out_error_msg");

			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isNotEmpty(errorMsg) || (int) statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("deleteRole validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList());
					// Arrays.asList(new WearablesError("Error while creating Prelude Pet
					// Parents")));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException("Inserta Pet Parent validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList());
					// Arrays.asList(new WearablesError("Insert Pet Parent validation failed")));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing callUpdateExtPetProc ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	public void savePreludeInfo(String extPatientID, String string) {
		try {
			insert("insert into temp_prelude_data (patient_id, json_value) values (?,?)", extPatientID, string);
			LOGGER.info("Insert into temp_prelude_data " + extPatientID + " json : " + string);
		} catch (Exception e) {
			LOGGER.info("Error oocured while insert into  temp_prelude_data" + e.getMessage());
		}

	}

}
