package com.hillspet.wearables.jaxrs.resource.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
//import com.sun.jmx.snmp.Timestamp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hillspet.wearables.common.builders.JaxrsJsonResponseBuilder;
import com.hillspet.wearables.common.response.SuccessResponse;
import com.hillspet.wearables.dto.CustomUserDetails;
import com.hillspet.wearables.dto.PetCampaignPointsDTO;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetDevice;
import com.hillspet.wearables.dto.PetExternalInfoListDTO;
import com.hillspet.wearables.dto.PetListDTO;
import com.hillspet.wearables.dto.PetNote;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.dto.PetObservationMediaListDTO;
import com.hillspet.wearables.dto.PetParentDTO;
import com.hillspet.wearables.dto.PetStudyDTO;
import com.hillspet.wearables.dto.filter.BaseFilter;
import com.hillspet.wearables.dto.filter.ExternalPetInfoFilter;
import com.hillspet.wearables.dto.filter.PetActivityFactorFilter;
import com.hillspet.wearables.dto.filter.PetEnthusiasmFilter;
import com.hillspet.wearables.dto.filter.PetFilter;
import com.hillspet.wearables.dto.filter.PetImageScaleFilter;
import com.hillspet.wearables.dto.filter.PetObservationMediaFilter;
import com.hillspet.wearables.jaxrs.resource.PetResource;
import com.hillspet.wearables.objects.common.response.CommonResponse;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.AssociateNewStudyRequest;
import com.hillspet.wearables.request.DownloadMediaRequest;
import com.hillspet.wearables.request.PetRequest;
import com.hillspet.wearables.response.ActivityFactorResultResponseList;
import com.hillspet.wearables.response.ExternalPetInfoResponse;
import com.hillspet.wearables.response.PetCampaignResponse;
import com.hillspet.wearables.response.PetDevicesResponse;
import com.hillspet.wearables.response.PetFeedingEnthusiasmScaleResponse;
import com.hillspet.wearables.response.PetImageScaleResponse;
import com.hillspet.wearables.response.PetNotesResponse;
import com.hillspet.wearables.response.PetObservationMediaListResponse;
import com.hillspet.wearables.response.PetObservationsResponse;
import com.hillspet.wearables.response.PetParentListResponse;
import com.hillspet.wearables.response.PetParentsResponse;
import com.hillspet.wearables.response.PetRedemptionHistoryResponse;
import com.hillspet.wearables.response.PetResponse;
import com.hillspet.wearables.response.PetStudyResponse;
import com.hillspet.wearables.response.PetWeightHistoryResponse;
import com.hillspet.wearables.response.PetsResponse;
import com.hillspet.wearables.security.Authentication;
import com.hillspet.wearables.service.pet.PetService;

/**
 * This class providing Pet details.
 * 
 * @author vvodyaram
 * @since w1.0
 * @version w1.0
 * @version Dec 7, 2020
 */
@Service
public class PetResourceImpl implements PetResource {

	private static final Logger LOGGER = LogManager.getLogger(PetResourceImpl.class);
	private static final int BUFFER_SIZE = 262144; // 4096;
	@Autowired
	private PetService petService;

	@Autowired
	private Authentication authentication;

	@Autowired
	private JaxrsJsonResponseBuilder responseBuilder;

	@Value("${mediaPath}")
	private String mediaPath;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Response getPetList(PetFilter filter) {
		CustomUserDetails userDetails = authentication.getAuthUserDetails();
		filter.setUserId(userDetails.getUserId());
		filter.setRoleTypeId(userDetails.getRoleTypeId());
		PetsResponse response = petService.getPetList(filter);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPets() {
		List<PetListDTO> pets = petService.getPets();
		PetsResponse response = new PetsResponse();
		response.setPets(pets);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response addPet(PetRequest petRequest) {
		// Step 2: process
		petRequest.setUserId(authentication.getAuthUserDetails().getUserId());
		PetDTO petDTO = petService.addPet(petRequest);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);

		// Step 5: build a successful response
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updatePet(PetRequest petRequest) {
		// Step 2: process
		petRequest.setUserId(authentication.getAuthUserDetails().getUserId());
		PetDTO petDTO = petService.updatePet(petRequest);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);

		// Step 5: build a successful response
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetById(int petId) {
		PetDTO petDTO = petService.getPetById(petId);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetByIdAndStudyId(int petId, int studyId) {
		PetDTO petDTO = petService.getPetByIdAndStudyId(petId, studyId);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response deletePet(int petId) {
		int modifiedBy = authentication.getAuthUserDetails().getUserId();
		petService.deletePet(petId, modifiedBy);

		// Step 5: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Pet has been deleted successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetsByPetParent(int petParentId) {
		PetsResponse response = petService.getPetsByPetParent(petParentId);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetObservations(PetObservationMediaFilter filter) {
		PetObservationsResponse response = petService.getPetObservations(filter);
		SuccessResponse<PetObservationsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetParents(PetFilter filter) {
		PetParentListResponse parentDTOs = petService.getPetParents(filter);
		SuccessResponse<PetParentListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(parentDTOs);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetNotes(PetFilter filter) {
		PetNotesResponse petNotes = petService.getPetNotes(filter);
		SuccessResponse<PetNotesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petNotes);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetDevices(int petId) {
		List<PetDevice> petDevices = petService.getPetDevices(petId);
		PetDevicesResponse response = new PetDevicesResponse();
		response.setPetDevices(petDevices);
		SuccessResponse<PetDevicesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response addPetNote(PetNote petNote, int petId) {
		// Step 1: process
		petNote.setCreatedBy(authentication.getAuthUserDetails().getUserId());
		petNote.setPetId(petId);
		petService.addPetNote(petNote);

		// Step 2: build a successful response
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetObservationMediaList(PetObservationMediaFilter filter) {
		PetObservationMediaListResponse response = petService.getPetObservationMediaList(filter);
		SuccessResponse<PetObservationMediaListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetObservationMediaById(int petId) {
		List<PetObservationMediaListDTO> PetObservationMediaListDTO = petService.getPetObservationMediaById(petId);
		PetObservationMediaListResponse response = new PetObservationMediaListResponse();
		response.setPetObservationMediaList(PetObservationMediaListDTO);
		response.setNoOfElements(PetObservationMediaListDTO.size());
		response.setTotalRecords(PetObservationMediaListDTO.size());
		response.setSearchElments(PetObservationMediaListDTO.size());
		SuccessResponse<PetObservationMediaListResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetViewPane(PetFilter filter) {
		PetsResponse response = petService.getPetViewPane(filter);
		SuccessResponse<PetsResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response disassociateStudy(int petId, int studyId) {
		// Step 1: process
		petService.disassociateStudy(petId, studyId);

		// Step 2: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Study has been disassociated successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response downloadPetMedia(DownloadMediaRequest downloadMediaRequest) throws IOException {

		String[] csvUrlArray = downloadMediaRequest.getMediaUrl().split(",");

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String userMediaPath = mediaPath + File.separator + authentication.getAuthUserDetails().getUserId()
				+ File.separator + timestamp.getTime();

		File userFolder = new File(mediaPath + File.separator + authentication.getAuthUserDetails().getUserId());
		userFolder.mkdir();

		File userFolderWithTime = new File(mediaPath + File.separator + authentication.getAuthUserDetails().getUserId()
				+ File.separator + timestamp.getTime());
		userFolderWithTime.mkdir();

		// set file (and path) to be download

		if (csvUrlArray.length > NumberUtils.INTEGER_ONE) {
			String zipFilePath = userMediaPath + File.separator + "PetMediaPath.zip";
			List<String> files = downloadPetMedia(csvUrlArray, userMediaPath);
			zipFiles(files, zipFilePath);
			File file = new File(zipFilePath);
			ResponseBuilder responseBuilder = Response.ok((Object) file);
			responseBuilder.header("Content-Disposition", "attachment; filename=\"PetMedia.zip\"");
			return responseBuilder.build();
		} else {
			try {
				String fileName = downloadSingleFile(csvUrlArray[0], userMediaPath);
				String filePath = userMediaPath + File.separator + fileName;
				File file = new File(filePath);
				ResponseBuilder responseBuilder = Response.ok((Object) file);
				responseBuilder.header("Content-Disposition", "attachment; filename=" +fileName);
				return responseBuilder.build();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return Response.noContent().build();
	}

	private String downloadSingleFile(String fileURL, String downloadMediaTo) throws IOException {
		URL url = new URL(fileURL);
		HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();
		String fileName = "";
		
		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename");
				if (index > 0) {
					// fileName = disposition.substring(index + 10, disposition.length() - 1);
					fileName = disposition.split("\\=")[1];
					fileName = fileName.replaceAll("utf-8''", "");
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			LOGGER.info("Content-Type = " + contentType);
			LOGGER.info("Content-Disposition = " + disposition);
			LOGGER.info("Content-Length = " + contentLength);
			LOGGER.info("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			
			
			if(fileName.contains("?")) {
		    fileName  =	fileName.substring(0,fileName.indexOf("?"));
		    	LOGGER.info("fileName updated to = " + fileName);
			}
			
			String saveFilePath = downloadMediaTo + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			// after downloading creating the file
			File mediaFile = new File(saveFilePath);

			LOGGER.info("imageAbsPath:" + mediaFile.getAbsolutePath());
			outputStream.close();
			inputStream.close();
			LOGGER.info("File downloaded");
		} else {
			LOGGER.info("No file to download. Server replied HTTP code: " + responseCode);
		}
		
		httpConn.disconnect();
		return fileName;
	}

	private List<String> downloadPetMedia(String[] csvUrlArray, String userMediaPath) {
		List<String> files = new ArrayList<String>();
		try {
			for (String filePath : csvUrlArray)
				downloadFile(filePath, userMediaPath, files);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return files;
	}

	/**
	 * Zips the list of files in the specified zip file path.. TODO Throw the
	 * exceptions properly.
	 * 
	 * @author rmaram
	 * @param files
	 * @param zipFilePath
	 */
	private void zipFiles(List<String> files, String zipFilePath) {
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		try {
			fos = new FileOutputStream(zipFilePath);
			zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
			for (String filePath : files) {
				File input = new File(filePath);
				fis = new FileInputStream(input);
				ZipEntry ze = new ZipEntry(input.getName());
				LOGGER.info("Zipping the file: " + input.getName());
				zipOut.putNextEntry(ze);
				byte[] tmp = new byte[4 * 1024];
				int size = 0;
				while ((size = fis.read(tmp)) != -1) {
					zipOut.write(tmp, 0, size);
				}
				zipOut.flush();
				fis.close();
			}
			zipOut.close();
			LOGGER.info("Done... Zipped the files...");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Downloads the file to the specified directory.
	 * 
	 * @author rmaram
	 * @param fileURL
	 * @param saveDir
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void downloadFile(String fileURL, String downloadedMediaTo, List<String> files)
			throws IOException {

		URL url = new URL(fileURL);
		HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename");
				if (index > 0) {
					// fileName = disposition.substring(index + 10, disposition.length() - 1);
					fileName = disposition.split("\\=")[1];
					fileName = fileName.replaceAll("utf-8''", "");
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			LOGGER.info("Content-Type = " + contentType);
			LOGGER.info("Content-Disposition = " + disposition);
			LOGGER.info("Content-Length = " + contentLength);
			LOGGER.info("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			if(fileName.contains("?")) {
			    fileName  =	fileName.substring(0,fileName.indexOf("?"));
			    	LOGGER.info("fileName updated to = " + fileName);
				}
			String saveFilePath = downloadedMediaTo + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			// after downloading creating the file
			File imageFile = new File(saveFilePath);

			LOGGER.info("imageAbsPath:" + imageFile.getAbsolutePath());
			outputStream.close();
			inputStream.close();
			files.add(saveFilePath); // Adding files path for zipping
			LOGGER.info("File downloaded");
		} else {
			LOGGER.info("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}

	@Override
	public void downloadPetMedia(DownloadMediaRequest downloadMediaRequest, HttpServletResponse response)
			throws IOException {
		
		String[] csvUrlArray = downloadMediaRequest.getMediaUrl().split(",");
		LOGGER.error(csvUrlArray);
		if (csvUrlArray.length > NumberUtils.INTEGER_ONE) {
			try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
				String zipName = "PetMedia.zip";
				byte bytes[] = new byte[4096];
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setHeader("Content-Disposition", "attachment; filename=" + zipName);

				for (String fileUrl : csvUrlArray) {
					putEntryIntoZip(zos, bytes, fileUrl);
				}
			} catch (Exception exception) {
				LOGGER.error("Error while zipping a file. due to ", exception);
			}
		} else {
			downloadPetMediaSingleFile(downloadMediaRequest, response, csvUrlArray[0]);
		}
	}
	
	
	
	public void downloadPetMediaSingleFile(DownloadMediaRequest downloadMediaRequest, HttpServletResponse response,
			String fileUrl) throws IOException {
		try {
			if (StringUtils.isNotEmpty(fileUrl)) {
				String fileName = fileUrl.split("\\?")[NumberUtils.INTEGER_ZERO];
				String[] fileNameArr = fileName.split("/");
				fileName = fileNameArr[fileNameArr.length - 1];
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

				URI url = new URI(fileUrl);
				InputStream input = new ByteArrayInputStream(restTemplate.getForObject(url, byte[].class));
				// response.setContentLength(input.read(buffer));
				byte[] buffer = new byte[10240];

				try (ServletOutputStream output = response.getOutputStream();) {
					for (int length = 0; (length = input.read(buffer)) > 0;) {
						output.write(buffer, 0, length);
					}
				}
			}

		} catch (URISyntaxException | IOException e) {
			LOGGER.error("Error while downloading a single file. due to ", e);
		}

	}
	

	private void putEntryIntoZip(ZipOutputStream zos, byte[] bytes, String fileUrl) throws URISyntaxException {
		
		LOGGER.error(fileUrl);
		if (StringUtils.isNotEmpty(fileUrl)) {
			String fileName = fileUrl.split("\\?")[NumberUtils.INTEGER_ZERO];
			LOGGER.error(fileName);
			// fileName = fileName.substring(fileName.lastIndexOf("\\") + NumberUtils.INTEGER_TWO);			
			//fileName = fileName.substring(fileName.lastIndexOf("/")+1 ,fileName.length() );
			
			String[] fileNameArr = fileName.split("/");
			fileName = fileNameArr[fileNameArr.length-1];
			 
			URI url = new URI(fileUrl);
			InputStream in = new ByteArrayInputStream(restTemplate.getForObject(url, byte[].class));
			ZipEntry zipentry = new ZipEntry(fileName);
			zipentry.setTime(System.currentTimeMillis());
			try {
				/*
				 * File file = restTemplate.execute(form.getFormUrl(), HttpMethod.GET, null,
				 * clientHttpResponse -> { File ret = File.createTempFile(fileName, "tmp");
				 * StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
				 * return ret; });
				 */
				zos.putNextEntry(zipentry);
				int bytesRead = NumberUtils.INTEGER_MINUS_ONE;

				while ((bytesRead = in.read(bytes)) != NumberUtils.INTEGER_MINUS_ONE) {
					zos.write(bytes, NumberUtils.INTEGER_ZERO, bytesRead);
				}
				LOGGER.info("Finsih Writing putEntryIntoZip::" + fileName);
			} catch (Exception exception) {
				LOGGER.error("Error while zipping a file. due to ", exception);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Response getPetWeightHistory(BaseFilter filter, int petId, String fromDate, String toDate)
			throws IOException {

		PetWeightHistoryResponse response = petService.getPetWeightHistory(filter, petId, fromDate, toDate);
		SuccessResponse<PetWeightHistoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getCurrentStudies(int petId) {
		List<PetStudyDTO> petDTO = petService.getCurrentStudies(petId);
		PetStudyResponse response = new PetStudyResponse();
		response.setPetStudy(petDTO);
		SuccessResponse<PetStudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getArchiveStudies(int petId) {
		List<PetStudyDTO> petDTO = petService.getArchiveStudies(petId);
		PetStudyResponse response = new PetStudyResponse();
		response.setPetStudy(petDTO);
		SuccessResponse<PetStudyResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetDevicesByStudy(int petId, int studyId,PetFilter filter) {
		filter.setPetId(petId);
		filter.setStudyId(String.valueOf(studyId));
		PetDevicesResponse petDevices = petService.getPetDevicesByStudy(filter);
		SuccessResponse<PetDevicesResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petDevices);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetDetailsById(int petId) {
		PetDTO petDTO = petService.getPetDetailsById(petId);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetCampaignPoints(int petId) {
		PetCampaignPointsDTO petCampaignDTO = petService.getPetCampaignPoints(petId);
		SuccessResponse<PetCampaignPointsDTO> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(petCampaignDTO);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetCampaignPointsList(int petId, int activityId, BaseFilter filter) {
		PetCampaignResponse response = petService.getPetCampaignPointsList(petId, activityId, filter);
		SuccessResponse<PetCampaignResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetRedemptionHistory(int petId) {
		PetRedemptionHistoryResponse response = petService.getPetRedemptionHistory(petId);
		SuccessResponse<PetRedemptionHistoryResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response redeemRewardPoints(int petId, int redeemPoints) {

		Integer userId = authentication.getAuthUserDetails().getUserId();
		petService.redeemRewardPoints(petId, redeemPoints, userId);

		CommonResponse response = new CommonResponse();
		response.setMessage("Points redeemed successfully!");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getExternalPetInfoList(int studyId) {
		List<PetExternalInfoListDTO> response = petService.getExternalPetInfoList(studyId);
		SuccessResponse<List<PetExternalInfoListDTO>> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getExternalPetInfo(ExternalPetInfoFilter filter) {
		ExternalPetInfoResponse response = petService.getExternalPetInfo(filter);
		SuccessResponse<ExternalPetInfoResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response associateNewStudy(@Valid AssociateNewStudyRequest petRequest) {
		// Step 2: process
		petRequest.setUserId(authentication.getAuthUserDetails().getUserId());
		PetDTO petDTO = petService.associateNewStudy(petRequest);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);

		// Step 5: build a successful response
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response addWeight(@Valid AddPetWeight addPetWeight) {
		addPetWeight.setUserId(authentication.getAuthUserDetails().getUserId());
		PetDTO petDTO = petService.addPetWeight(addPetWeight);
		PetResponse response = new PetResponse();
		response.setPetDTO(petDTO);

		// Step 5: build a successful response
		SuccessResponse<PetResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response updateWeight(int weightId, String weight, String unit) {
		// Step 1: process
		petService.updateWeight(weightId, weight, unit);

		// Step 2: build a successful response
		CommonResponse response = new CommonResponse();
		response.setMessage("Pet weight updated successfully");
		SuccessResponse<CommonResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetFeedingEnthusiasmScaleDtls(PetEnthusiasmFilter filter) {
		CustomUserDetails userDetails = authentication.getAuthUserDetails();
		filter.setUserId(userDetails.getUserId());
		filter.setRoleTypeId(userDetails.getRoleTypeId());
		PetFeedingEnthusiasmScaleResponse response = petService.getPetFeedingEnthusiasmScaleDtls(filter);
		SuccessResponse<PetFeedingEnthusiasmScaleResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetImageScaleDtls(PetImageScaleFilter filter) {
		CustomUserDetails userDetails = authentication.getAuthUserDetails();
		filter.setUserId(userDetails.getUserId());
		filter.setRoleTypeId(userDetails.getRoleTypeId());
		PetImageScaleResponse response = petService.getPetImageScaleDtls(filter);
		SuccessResponse<PetImageScaleResponse> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}

	@Override
	public Response getPetActivityFactorResult(PetActivityFactorFilter filter) {
		CustomUserDetails userDetails = authentication.getAuthUserDetails();
		filter.setUserId(userDetails.getUserId());
		filter.setRoleTypeId(userDetails.getRoleTypeId());
		ActivityFactorResultResponseList response = petService.getPetActivityFactorResult(filter);
		SuccessResponse<ActivityFactorResultResponseList> successResponse = new SuccessResponse<>();
		successResponse.setServiceResponse(response);
		return responseBuilder.buildResponse(successResponse);
	}
}
