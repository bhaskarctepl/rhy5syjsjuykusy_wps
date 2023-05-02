package com.hillspet.wearables.dao.asset.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.constants.SQLConstants;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.asset.AssetManagementDao;
import com.hillspet.wearables.dto.Asset;
import com.hillspet.wearables.dto.AssetHistory;
import com.hillspet.wearables.dto.AssetType;
import com.hillspet.wearables.dto.BulkAssetUploadDeviceInfo;
import com.hillspet.wearables.dto.DeviceInfo;
import com.hillspet.wearables.dto.DeviceModel;
import com.hillspet.wearables.dto.DeviceUnAssignReason;
import com.hillspet.wearables.dto.FirmwareVersion;
import com.hillspet.wearables.dto.filter.AssetFirmwareVersionsFilter;
import com.hillspet.wearables.dto.filter.AssetUpdateFirmwareFilter;
import com.hillspet.wearables.dto.filter.AssetsFilter;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.request.BulkAssetUploadRequest;
import com.hillspet.wearables.response.DeviceResponse;

@Repository
public class AssetManagementDaoImpl extends BaseDaoImpl implements AssetManagementDao {

	private static final Logger LOGGER = LogManager.getLogger(AssetManagementDaoImpl.class);

	@Override
	public DeviceInfo addDeviceInfo(DeviceInfo deviceInfo) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_device_number", deviceInfo.getDeviceNumber().trim());
		inputParams.put("p_device_type", deviceInfo.getDeviceType().equals("Other") ? deviceInfo.getOtherAssetType()
				: deviceInfo.getDeviceType());
		inputParams.put("p_device_model", deviceInfo.getDeviceModel().equals("Other") ? deviceInfo.getOtherAssetModel()
				: deviceInfo.getDeviceModel());
		inputParams.put("p_device_location_id", deviceInfo.getDeviceLocationId());
		inputParams.put("p_device_location_others", deviceInfo.getDeviceLocationOthers());
		inputParams.put("p_status_id", deviceInfo.getStatusId());
		inputParams.put("p_mfg_serial_number", deviceInfo.getMfgSerialNumber());
		inputParams.put("p_mfg_firmware", deviceInfo.getMfgFirmware());
		inputParams.put("p_mfg_mac_addr", deviceInfo.getMfgMacAddr());
		inputParams.put("p_wifi_mac_addr", deviceInfo.getWifiMacAddr());
		inputParams.put("p_tracking_number", deviceInfo.getTrackingNumber());
		inputParams.put("p_created_by", deviceInfo.getCreatedBy());
//		inputParams.put("p_other_asset_type", deviceInfo.getOtherAssetType());
//		inputParams.put("p_other_asset_model", deviceInfo.getOtherAssetModel());

		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_INSERT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer deviceId = (int) outParams.get("last_insert_id");
				deviceInfo.setDeviceId(deviceId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"addDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_ALREADY_EXISTS,
									deviceInfo.getDeviceNumber())));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException(
							"addDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_MODEL_ALREADY_EXISTS,
									deviceInfo.getDeviceModel())));
				} else if (statusFlag == -4) {
					throw new ServiceExecutionException(
							"addDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_MFG_SERIAL_NUMBER_ALREADY_EXISTS,
									deviceInfo.getMfgSerialNumber())));
				} else if (statusFlag == -5) {
					throw new ServiceExecutionException(
							"addDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_WIFI_MAC_ADDR_ALREADY_EXISTS,
									deviceInfo.getMfgMacAddr())));
				} else if (statusFlag == -6) {
					throw new ServiceExecutionException(
							"addDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_WIFI_MAC_ADDR_ALREADY_EXISTS,
									deviceInfo.getMfgMacAddr())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing addDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfo;
	}

	@Override
	public DeviceInfo updateDeviceInfo(DeviceInfo deviceInfo) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_device_id", deviceInfo.getDeviceId());
		inputParams.put("p_device_number", deviceInfo.getDeviceNumber());
		inputParams.put("p_device_type", deviceInfo.getDeviceType());
		inputParams.put("p_device_model", deviceInfo.getDeviceModel());
		inputParams.put("p_device_location_id", deviceInfo.getDeviceLocationId());
		inputParams.put("p_device_location_others", deviceInfo.getDeviceLocationOthers());
		inputParams.put("p_status_id", deviceInfo.getStatusId());
		inputParams.put("p_mfg_serial_number", deviceInfo.getMfgSerialNumber());
		inputParams.put("p_mfg_firmware", deviceInfo.getMfgFirmware());
		inputParams.put("p_mfg_mac_addr", deviceInfo.getMfgMacAddr());
		inputParams.put("p_wifi_mac_addr", deviceInfo.getWifiMacAddr());
		inputParams.put("p_tracking_number", deviceInfo.getTrackingNumber());
		inputParams.put("p_modified_by", deviceInfo.getModifiedBy());
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_UPDATE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_ALREADY_EXISTS,
									deviceInfo.getDeviceNumber())));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException(
							"updateDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_MODEL_ALREADY_EXISTS,
									deviceInfo.getDeviceModel())));
				} else if (statusFlag == -4) {
					throw new ServiceExecutionException(
							"updateDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_MFG_SERIAL_NUMBER_ALREADY_EXISTS,
									deviceInfo.getMfgSerialNumber())));
				} else if (statusFlag == -5) {
					throw new ServiceExecutionException(
							"updateDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_MFG_MFG_MAC_ADDR_ALREADY_EXISTS,
									deviceInfo.getMfgMacAddr())));
				} else if (statusFlag == -6) {
					throw new ServiceExecutionException(
							"addDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_WIFI_MAC_ADDR_ALREADY_EXISTS,
									deviceInfo.getMfgMacAddr())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing updateDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfo;
	}

	@Override
	public void deleteDeviceInfo(int deviceId, int modifiedBy) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_device_id", deviceId);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_DELETE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"deleteDeviceInfo service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_CANNOT_DELETE, errorMsg)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void updateDeviceFirmware(int firmwareVersionId, String deviceIds, int modifiedBy)
			throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_firmware_version_id", firmwareVersionId);
		inputParams.put("p_device_ids", deviceIds);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_UPDATE_DEVICE_FIRMWARE,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");

			if (StringUtils.isNotEmpty(errorMsg) || statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateDeviceFirmware service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_TYPES_SHOULD_BE_SAME)));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException(
							"updateDeviceFirmware service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_MODELS_SHOULD_BE_SAME)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public DeviceInfo getDeviceInfoById(int deviceId) throws ServiceExecutionException {
		final DeviceInfo deviceInfo = new DeviceInfo();
		LOGGER.debug("getDeviceInfoById called");
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_INFO_GET_BY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					deviceInfo.setDeviceId(rs.getInt("device_id"));
					deviceInfo.setDeviceNumber(rs.getString("device_number"));
					deviceInfo.setDeviceType(rs.getString("device_type"));
					deviceInfo.setDeviceModel(rs.getString("device_model"));
					deviceInfo.setMfgSerialNumber(rs.getString("mfg_serial_number"));
					deviceInfo.setMfgFirmware(rs.getString("mfg_firmware"));
					deviceInfo.setMfgMacAddr(rs.getString("mfg_mac_addr"));
					deviceInfo.setWifiMacAddr(rs.getString("wifi_mac_addr"));
					deviceInfo.setAddDate(rs.getTimestamp("add_date").toLocalDateTime());
					deviceInfo.setIsGyroScopeDataEnabled(rs.getBoolean("is_gyro_scope_data_enabled"));
					deviceInfo.setIsActive(rs.getBoolean("is_active"));
					deviceInfo.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
					if (rs.getTimestamp("modified_date") != null) {
						deviceInfo.setModifiedDate(rs.getTimestamp("modified_date").toLocalDateTime());
					}
					deviceInfo.setDeviceLocationId(rs.getInt("device_location_id"));
					deviceInfo.setLocation(rs.getString("device_location"));
					deviceInfo.setDeviceLocationOthers(rs.getString("device_location_others"));
					deviceInfo.setStatusId(rs.getInt("STATUS_ID"));
					deviceInfo.setStatus(rs.getString("STATUS_NAME"));
					deviceInfo.setTrackingNumber(rs.getString("tracking_number"));
				}
			}, deviceId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceInfoById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfo;
	}

	@Override
	public Map<String, Integer> getDeviceCount(AssetUpdateFirmwareFilter filter) throws ServiceExecutionException {
		int totalCount = NumberUtils.INTEGER_ZERO;
		LOGGER.debug("getDeviceCount called");
		String counts;
		HashMap<String, Integer> map = new HashMap<>();
		try {
			counts = selectForObject(SQLConstants.DEVICE_INFO_LIST_COUNT, String.class, filter.getSearchText(),
					filter.getAssetType(), filter.getAssetLocation(), filter.getAssetModel(),
					filter.getUserId(),
					filter.getRoleTypeId()
					);
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<DeviceInfo> getDeviceInfo(AssetUpdateFirmwareFilter filter) throws ServiceExecutionException {
		List<DeviceInfo> deviceInfoList = new ArrayList<>();

		LOGGER.debug("getDeviceInfo called");
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_INFO_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DeviceInfo deviceInfo = new DeviceInfo();
					deviceInfo.setSlNumber(rs.getInt("slNo"));
					deviceInfo.setDeviceId(rs.getInt("device_id"));
					deviceInfo.setDeviceNumber(rs.getString("device_number"));
					deviceInfo.setDeviceType(rs.getString("device_type"));
					deviceInfo.setDeviceModel(rs.getString("device_model"));
					deviceInfo.setMfgSerialNumber(rs.getString("mfg_serial_number"));
					deviceInfo.setMfgFirmware(rs.getString("mfg_firmware"));
					deviceInfo.setMfgMacAddr(rs.getString("mfg_mac_addr"));
					deviceInfo.setWifiMacAddr(rs.getString("wifi_mac_addr"));
					deviceInfo.setTrackingNumber(rs.getString("tracking_number"));
					deviceInfo.setAddDate(rs.getTimestamp("add_date").toLocalDateTime());
					deviceInfo.setIsGyroScopeDataEnabled(rs.getBoolean("is_gyro_scope_data_enabled"));
					deviceInfo.setIsActive(rs.getBoolean("is_active"));
					deviceInfo.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
					if (rs.getTimestamp("modified_date") != null) {
						deviceInfo.setModifiedDate(rs.getTimestamp("modified_date").toLocalDateTime());
					}
					deviceInfo.setFirmwareVersionNumber(rs.getString("firmware_version_number"));
					deviceInfo.setLocation(rs.getString("device_location"));

					deviceInfoList.add(deviceInfo);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getAssetType(), filter.getAssetLocation(), filter.getAssetModel(),
					filter.getUserId(),
					filter.getRoleTypeId()
					);

		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceInfo", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfoList;
	}

	@Override
	public List<DeviceInfo> getAllDevices() throws ServiceExecutionException {
		List<DeviceInfo> deviceInfos = new ArrayList<>();
		LOGGER.debug("getAllDevices called");
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_INFO_GET_ALL_DEVICES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DeviceInfo deviceInfo = new DeviceInfo();
					deviceInfo.setDeviceId(rs.getInt("device_id"));
					deviceInfo.setDeviceNumber(rs.getString("device_number"));
					deviceInfo.setDeviceType(rs.getString("device_type"));
					deviceInfo.setDeviceModel(rs.getString("device_model"));
					deviceInfo.setIsActive(rs.getBoolean("is_active"));
					deviceInfos.add(deviceInfo);
				}
			});

		} catch (Exception e) {
			LOGGER.error("error while fetching getAllDevices", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfos;
	}

	@Override
	public DeviceResponse getDeviceTypesAndModels() throws ServiceExecutionException {
		LOGGER.debug("getDeviceTypesAndModels called");
		DeviceResponse deviceResponse = new DeviceResponse();
		List<String> deviceModels = new ArrayList<>();
		List<String> deviceTypes = new ArrayList<>();
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_INFO_GET_DEVICE_TYPES_AND_MODELS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					if (StringUtils.isNotEmpty(rs.getString("deviceTypes"))) {
						for (String type : rs.getString("deviceTypes").split(",")) {
							deviceTypes.add(type);
						}
					}
					if (StringUtils.isNotEmpty(rs.getString("models"))) {
						for (String model : rs.getString("models").split(",")) {
							deviceModels.add(model);
						}
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceTypesAndModels", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		deviceResponse.setDeviceTypes(deviceTypes);
		deviceResponse.setDeviceModels(deviceModels);
		LOGGER.debug("getDeviceInfo completed successfully");
		return deviceResponse;
	}

	@Override
	public FirmwareVersion addFirmwareVersion(FirmwareVersion firmwareVersion) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_firmware_version", firmwareVersion.getFirmwareVersionNumber());
		inputParams.put("p_asset_type", firmwareVersion.getAssetType());
		inputParams.put("p_model", firmwareVersion.getModel());
		inputParams.put("p_created_by", firmwareVersion.getCreatedBy());
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.FIRMWARE_VERSION_INSERT, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer firmwareVersionId = (int) outParams.get("last_insert_id");
				firmwareVersion.setFirmwareVersionId(firmwareVersionId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"addFirmwareVersion service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.FIRMWARE_VERSION_ALREADY_EXISTS,
									firmwareVersion.getFirmwareVersionNumber())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing addFirmwareVersion ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return firmwareVersion;
	}

	@Override
	public FirmwareVersion updateFirmwareVersion(FirmwareVersion firmwareVersion) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_firmware_version_id", firmwareVersion.getFirmwareVersionId());
		inputParams.put("p_asset_frimware_version_id", firmwareVersion.getAssetFrimwareVersionId());
		inputParams.put("p_firmware_version", firmwareVersion.getFirmwareVersionNumber());
		inputParams.put("p_asset_type", firmwareVersion.getAssetType());
		inputParams.put("p_model", firmwareVersion.getModel());
		inputParams.put("p_modified_by", firmwareVersion.getModifiedBy());
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.FIRMWARE_VERSION_UPDATE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || statusFlag < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateFirmwareVersion service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.FIRMWARE_VERSION_ALREADY_EXISTS,
									firmwareVersion.getFirmwareVersionNumber())));
				} else if (statusFlag == -3) {
					throw new ServiceExecutionException(
							"updateFirmwareVersion service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.FIRMWARE_VERSION_REFERENCED, errorMsg)));
				} else if (statusFlag == -4) {
					throw new ServiceExecutionException(
							"updateFirmwareVersion service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(
									new WearablesError(WearablesErrorCode.FIRMWARE_VERSION_REFERENCED, errorMsg)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing updateFirmwareVersion ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return firmwareVersion;
	}

	@Override
	public void deleteFirmwareVersion(int firmwareVersionId, int assetFirmwareVersionId, int modifiedBy) {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_firmware_version_id", firmwareVersionId);
		inputParams.put("p_asset_frimware_version_id", assetFirmwareVersionId);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.FIRMWARE_VERSION_DELETE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"deleteFirmwareVersion service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.FIRMWARE_CANNOT_DELETE,
									errorMsg)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}

			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteFirmwareVersion ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public FirmwareVersion getFirmwareVersionById(int firmwareVersionId) throws ServiceExecutionException {
		final FirmwareVersion firmwareVersion = new FirmwareVersion();
		LOGGER.debug("getFirmwareVersionById called");
		try {
			jdbcTemplate.query(SQLConstants.FIRMWARE_VERSION_GET_BY_ID, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					firmwareVersion.setFirmwareVersionId(rs.getInt("firmware_version_id"));
					firmwareVersion.setFirmwareVersionNumber(rs.getString("firmware_version_number"));
					firmwareVersion.setIsActive(rs.getBoolean("is_active"));
					firmwareVersion.setCreatedUser(rs.getString("createdUser"));
					firmwareVersion.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
					if (rs.getTimestamp("modified_date") != null) {
						firmwareVersion.setModifiedUser(rs.getString("modifiedUser"));
						firmwareVersion.setModifiedDate(rs.getTimestamp("modified_date").toLocalDateTime());
					}
				}
			}, firmwareVersionId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getFirmwareVersionById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return firmwareVersion;
	}

	@Override
	public Map<String, Integer> getFirmwareVersionCount(AssetFirmwareVersionsFilter filter)
			throws ServiceExecutionException {
		int totalCount = NumberUtils.INTEGER_ZERO;
		LOGGER.debug("getFirmwareVersionCount called");
		String counts;
		HashMap<String, Integer> map = new HashMap<>();
		try {
			counts = selectForObject(SQLConstants.FIRMWARE_VERSION_LIST_COUNT, String.class, filter.getSearchText(),
					filter.getAssetType(), filter.getAssetModel());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getFirmwareVersionCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<FirmwareVersion> getFirmwareVersions(AssetFirmwareVersionsFilter filter)
			throws ServiceExecutionException {
		List<FirmwareVersion> firmwareVersions = new ArrayList<>();
		LOGGER.debug("getFirmwareVersions called");
		try {
			jdbcTemplate.query(SQLConstants.FIRMWARE_VERSION_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					FirmwareVersion firmwareVersion = new FirmwareVersion();
					firmwareVersion.setSlNumber(rs.getInt("slNo"));
					firmwareVersion.setFirmwareVersionId(rs.getInt("firmware_version_id"));
					firmwareVersion.setAssetFrimwareVersionId(rs.getInt("ASSET_FIRMWARE_VERSION_ID"));
					firmwareVersion.setFirmwareVersionNumber(rs.getString("firmware_version_number"));
					firmwareVersion.setAssetType(rs.getString("asset_type"));
					firmwareVersion.setModel(rs.getString("model"));
					firmwareVersion.setIsActive(rs.getBoolean("is_active"));
					firmwareVersion.setCreatedUser(rs.getString("createdUser"));
					firmwareVersion.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
					if (rs.getTimestamp("modified_date") != null) {
						firmwareVersion.setModifiedUser(rs.getString("modifiedUser"));
						firmwareVersion.setModifiedDate(rs.getTimestamp("modified_date").toLocalDateTime());
					}
					firmwareVersions.add(firmwareVersion);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getAssetType(), filter.getAssetModel());

		} catch (Exception e) {
			LOGGER.error("error while fetching getFirmwareVersions", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return firmwareVersions;
	}

	@Override
	public List<FirmwareVersion> getAllFirmwareVersions(String assetType, String assetModel)
			throws ServiceExecutionException {
		List<FirmwareVersion> firmwareVersions = new ArrayList<>();
		LOGGER.debug("getAllFirmwareVersions called");
		try {
			jdbcTemplate.query(SQLConstants.FIRMWARE_VERSION_GET_ALL_VERSIONS, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					FirmwareVersion firmwareVersion = new FirmwareVersion();
					firmwareVersion.setFirmwareVersionId(rs.getInt("firmware_version_id"));
					firmwareVersion.setFirmwareVersionNumber(rs.getString("firmware_version_number"));
					firmwareVersion.setIsActive(rs.getBoolean("is_active"));
					firmwareVersions.add(firmwareVersion);
				}

			}, assetType, assetModel);

		} catch (Exception e) {
			LOGGER.error("error while fetching getAllFirmwareVersions", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return firmwareVersions;
	}

	@Override
	public Map<String, Integer> getAssetCount(AssetsFilter filter) throws ServiceExecutionException {
		int totalCount = NumberUtils.INTEGER_ZERO;
		LOGGER.debug("getAssetCount called");
		String counts;
		HashMap<String, Integer> map = new HashMap<>();
		try {
			counts = selectForObject(SQLConstants.ASSET_LIST_COUNT, String.class, filter.getSearchText(),
					filter.getStudyId(), filter.getAssetType(), filter.getStatusId(), filter.getUserId(),
					filter.getRoleTypeId());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while fetching getAssetCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<Asset> getAssetList(AssetsFilter filter) throws ServiceExecutionException {
		List<Asset> assetsList = new ArrayList<>();

		LOGGER.debug("getAssetList called");
		try {
			jdbcTemplate.query(SQLConstants.ASSET_GET_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Asset asset = new Asset();
					asset.setSlNumber(rs.getInt("slNo"));
					asset.setDeviceId(rs.getInt("device_id"));
					asset.setDeviceNumber(rs.getString("device_number"));
					asset.setDeviceType(rs.getString("device_type"));
					asset.setDeviceModel(rs.getString("device_model"));
					asset.setIsActive(rs.getBoolean("is_active"));
					asset.setStudy(rs.getString("study"));
					asset.setLocation(rs.getString("device_location"));
					asset.setStatusId(rs.getInt("STATUS_ID"));
					asset.setStatus(rs.getString("STATUS_NAME"));
					asset.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
					if (rs.getTimestamp("modified_date") != null) {
						asset.setModifiedDate(rs.getTimestamp("modified_date").toLocalDateTime());
					}
					asset.setMfgfirm(rs.getString("MFG_FIRMWARE") == null ? "" : rs.getString("MFG_FIRMWARE"));
					assetsList.add(asset);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getStudyId(), filter.getAssetType(), filter.getStatusId(), filter.getUserId(),
					filter.getRoleTypeId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getAssetList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return assetsList;
	}

	@Override
	public int[] saveBulkDevicesToStaging(List<BulkAssetUploadDeviceInfo> deviceInfo, String attachmentName,
			Integer userId) throws ServiceExecutionException {
		LOGGER.debug("saveBulkDevicesToStaging called");
		LOGGER.debug("saveBulkDevicesToStaging batch size " + deviceInfo.size());

		/* Deleting previously saved user Records from staging table. */
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_user_id", userId);

		/* Below code used to delete previous results. */
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_BULK_STAGING_RESULTS_DELETE,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			LOGGER.debug("saveBulkDevicesToStaging statusFlag " + statusFlag);
			LOGGER.debug("saveBulkDevicesToStaging errorMsg " + errorMsg);

		} catch (SQLException e) {
			LOGGER.error("error while executing addDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		int[] result = null;
		try {
			result = jdbcTemplate.batchUpdate(SQLConstants.DEVICE_INFO_BULK_INSERT_STAGING,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							BulkAssetUploadDeviceInfo info = deviceInfo.get(i);
							ps.setString(1, info.getDeviceNumber());
							ps.setString(2, info.getDeviceType());
							ps.setString(3, info.getDeviceModel());
							ps.setString(4, info.getLocation() != null ? info.getLocation().trim() : "");
							if (StringUtils.isBlank(info.getMfgSerialNumber())
									&& info.getDeviceModel().toUpperCase().equals("CMAS")) {
								ps.setString(5, info.getDeviceNumber());
							} else {
								ps.setString(5, info.getMfgSerialNumber());
							}
							// ps.setString(5, info.getMfgSerialNumber());
							ps.setString(6, info.getMfgFirmware());
							ps.setString(7, info.getMfgMacAddr());
							ps.setString(8, info.getWifiMacAddr());
							ps.setInt(9, userId);
							ps.setString(10, info.getTrackingNumber());
							ps.setString(11, attachmentName);
							ps.setString(12, info.getExceptionMsg());

						}

						@Override
						public int getBatchSize() {
							return deviceInfo.size();
						}
					});
		} catch (Exception e) {
			LOGGER.error("error while saveBulkDevicesToStaging", e);
		}
		LOGGER.debug("saveBulkDevicesToStaging Completed");
		LOGGER.debug("saveBulkDevicesToStaging - validateBulkDevicesList start.");

		validateBulkDevicesList(userId);

		LOGGER.debug("saveBulkDevicesToStaging - validateBulkDevicesList end.");
		return result;
	}

	@Override
	public DeviceInfo saveBulkUploadDevicesInfo(BulkAssetUploadRequest request) throws ServiceExecutionException {
		LOGGER.debug("saveDeviceInfoBulkUpload called");
		LOGGER.debug("saveDeviceInfoBulkUpload userId = " + request.getUserId());
		LOGGER.debug("saveDeviceInfoBulkUpload selectedIds = " + request.getStagingId());
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_user_id", request.getUserId());
		inputParams.put("p_selected_staging_ids", request.getStagingId());
		DeviceInfo deviceInfo = new DeviceInfo();
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_BULK_SAVE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer deviceID = (int) outParams.get("last_insert_id");
				LOGGER.info("Devices created successfully, Device ID is ", deviceID);
				deviceInfo.setDeviceId(deviceID);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException("AddPetParent service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.DEVICE_ALREADY_EXISTS, errorMsg)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}

			LOGGER.debug("saveDeviceInfoBulkUpload statusFlag " + statusFlag);
			LOGGER.debug("saveDeviceInfoBulkUpload errorMsg " + errorMsg);

		} catch (SQLException e) {
			LOGGER.error("error while executing addDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceInfo;
	}

	@Override
	public void validateBulkDevicesList(Integer userId) throws ServiceExecutionException {

		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_user_id", userId);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.DEVICE_INFO_BULK_VALIDATION_STAGING,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteDeviceInfo ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public int getBulkUploadDevicesListCount(BaseFilter filter) throws ServiceExecutionException {
		int totalCount = NumberUtils.INTEGER_ZERO;
		LOGGER.debug("getBulkUploadDevicesListCount called");
		try {
			totalCount = selectForObject(SQLConstants.DEVICE_INFO_BULK_STAGING_LIST_COUNT, Integer.class,
					filter.getUserId());
		} catch (Exception e) {
			LOGGER.error("error while fetching getAssetCount", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return totalCount;
	}

	@Override
	public List<BulkAssetUploadDeviceInfo> getBulkUploadDevicesList(BaseFilter filter)
			throws ServiceExecutionException {
		List<BulkAssetUploadDeviceInfo> deviceList = new ArrayList<>();

		LOGGER.debug("getAssetList called");
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_INFO_BULK_STAGING_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					BulkAssetUploadDeviceInfo device = new BulkAssetUploadDeviceInfo();
					device.setStagingId(rs.getInt("ID"));
					device.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					device.setDeviceType(rs.getString("DEVICE_TYPE"));
					device.setMfgSerialNumber(rs.getString("MFG_SERIAL_NUMBER"));
					device.setDeviceModel(rs.getString("DEVICE_MODEL"));
					device.setDeviceLocationName(rs.getString("DEVICE_LOCATION_NAME"));
					device.setDeviceLocationId(rs.getInt("DEVICE_LOCATION_ID"));
					device.setMfgFirmware(rs.getString("MFG_FIRMWARE"));
					device.setMfgMacAddr(rs.getString("MFG_MAC_ADDR"));
					device.setWifiMacAddr(rs.getString("WIFI_MAC_ADDR"));
					device.setTrackingNumber(rs.getString("TRACKING_NUMBER"));
					device.setExceptionMsg(rs.getString("EXCEPTION_TYPE"));

					deviceList.add(device);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getUserId());

		} catch (Exception e) {
			LOGGER.error("error while fetching getAssetList", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceList;
	}

	@Override
	public List<AssetType> getAllAssetTypes() throws ServiceExecutionException {
		List<AssetType> assetTypes = new ArrayList<>();
		LOGGER.debug("getAllAssetTypes called");
		try {
			jdbcTemplate.query(SQLConstants.ASSET_TYPE_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					AssetType assetType = new AssetType();
					assetType.setDeviceType(rs.getString("DEVICE_TYPE"));
					assetTypes.add(assetType);
				}

			});
			AssetType assetType = new AssetType();
			assetType.setDeviceType("Other");
			assetTypes.add(assetType);
		} catch (Exception e) {
			LOGGER.error("error while fetching getAllDevices", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return assetTypes;
	}

	@Override
	public List<DeviceModel> getDeviceModelById(String assetType) throws ServiceExecutionException {
		List<DeviceModel> deviceModels = new ArrayList<>();
		LOGGER.debug("getDeviceModelById called");
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_MODEL_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DeviceModel deviceModel = new DeviceModel();
					if (StringUtils.isNotBlank(rs.getString("DEVICE_MODEL"))) {
						deviceModel.setDeviceModel(rs.getString("DEVICE_MODEL"));
						deviceModels.add(deviceModel);
					}

				}
			}, assetType);
			DeviceModel deviceModel = new DeviceModel();
			deviceModel.setDeviceModel("Other");
			deviceModels.add(deviceModel);

		} catch (Exception e) {
			LOGGER.error("error while fetching getAllDevices", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return deviceModels;
	}

	@Override
	public List<DeviceUnAssignReason> getDeviceUnAssinReason() throws ServiceExecutionException {
		List<DeviceUnAssignReason> list = new ArrayList<>();
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_GET_UNASSIGN_REASON_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DeviceUnAssignReason reason = new DeviceUnAssignReason();
					reason.setReasonId(rs.getInt("REASON_ID"));
					reason.setReasonName(rs.getString("REASON_NAME"));
					reason.setIsActive(rs.getBoolean("IS_ACTIVE"));
					reason.setIsDeleted(rs.getBoolean("IS_DELETED"));
					list.add(reason);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getDeviceUnAssinReason", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return list;
	}

	@Override
	public List<FirmwareVersion> getAllFirmwareVersionsList() throws ServiceExecutionException {
		List<FirmwareVersion> firmwareVersions = new ArrayList<>();
		LOGGER.debug("getAllFirmwareVersions called");
		try {
			jdbcTemplate.query(SQLConstants.FIRMWARE_VERSION_GET_ALL_VERSIONS_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					FirmwareVersion firmwareVersion = new FirmwareVersion();
					firmwareVersion.setFirmwareVersionId(rs.getInt("firmware_version_id"));
					firmwareVersion.setFirmwareVersionNumber(rs.getString("firmware_version_number"));
					firmwareVersion.setIsActive(rs.getBoolean("is_active"));
					firmwareVersions.add(firmwareVersion);
				}

			});

		} catch (Exception e) {
			LOGGER.error("error while fetching getAllFirmwareVersions", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return firmwareVersions;
	}

	@Override
	public List<AssetHistory> getAssetHistory(int deviceId) throws ServiceExecutionException {
		List<AssetHistory> AssetHistoryList = new ArrayList<>();
		LOGGER.debug("getAllFirmwareVersions called");
		try {
			jdbcTemplate.query(SQLConstants.GET_ASSET_UPDATE_HISTORY, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					AssetHistory assetHistory = new AssetHistory();
					assetHistory.setNotificationDate(rs.getString("LAST_SAMPLE_DATE"));
					assetHistory.setUpdatedDate(rs.getString("LAST_FILE_DATE"));
					assetHistory.setVersion(rs.getString("FIRMWARE"));
					AssetHistoryList.add(assetHistory);
				}

			}, deviceId);

		} catch (Exception e) {
			LOGGER.error("error while fetching getAllFirmwareVersions", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return AssetHistoryList;
	}

	@Override
	public List<Asset> getUnConfiguredDevices() throws ServiceExecutionException {
		List<Asset> list = new ArrayList<>();
		try {
			jdbcTemplate.query(SQLConstants.DEVICE_GET_UNCONGIFURED_DEVICE_LIST, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Asset asset = new Asset();
					asset.setDeviceId(rs.getInt("DEVICE_ID"));
					asset.setDeviceNumber(rs.getString("DEVICE_NUMBER"));
					asset.setDeviceType(rs.getString("DEVICE_TYPE"));
					asset.setDeviceModel(rs.getString("DEVICE_MODEL"));
					asset.setStudy(rs.getString("STUDY_NAME"));
					asset.setPetName(rs.getString("PET_NAME"));
					asset.setPetId(rs.getString("PET_ID"));
					list.add(asset);
				}
			});
		} catch (Exception e) {
			LOGGER.error("error while fetching getUnConfiguredDevices", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return list;
	}
}
