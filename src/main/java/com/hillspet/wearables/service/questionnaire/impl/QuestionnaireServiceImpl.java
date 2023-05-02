
/**
 * Created Date: 08-Jan-2021
 * Class Name  : QuestionnaireServiceImpl.java
 * Description : Description of the package.
 *
 * © Copyright 2020 Cambridge Technology Enterprises(India) Pvt. Ltd.,All rights reserved.
 *
 * * * * * * * * * * * * * * * Change History *  * * * * * * * * * * *
 * <Defect Tag>        <Author>        <Date>        <Comments on Change>
 * ID                sgorle          08-Jan-2021        Mentions the comments on change, for the new file it's not required.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.hillspet.wearables.service.questionnaire.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.questionnaire.QuestionnaireDao;
import com.hillspet.wearables.dto.PetQuestionnaireResponse;
import com.hillspet.wearables.dto.Question;
import com.hillspet.wearables.dto.Questionnaire;
import com.hillspet.wearables.dto.QuestionnaireInstruction;
import com.hillspet.wearables.dto.QuestionnaireListDTO;
import com.hillspet.wearables.dto.QuestionnaireResponseByStudyListDTO;
import com.hillspet.wearables.dto.QuestionnaireResponseListDTO;
import com.hillspet.wearables.dto.filter.QuestionnaireFilter;
import com.hillspet.wearables.dto.filter.QuestionnaireResponseByStudyFilter;
import com.hillspet.wearables.dto.filter.QuestionnaireResponseFilter;
import com.hillspet.wearables.dto.filter.QuestionnaireViewResponseFilter;
import com.hillspet.wearables.request.QuestionnaireRequest;
import com.hillspet.wearables.response.PetQuestionnaireResponseList;
import com.hillspet.wearables.response.QuestionnaireListResponse;
import com.hillspet.wearables.response.QuestionnaireResponse;
import com.hillspet.wearables.response.QuestionnaireResponseByStudyListResponse;
import com.hillspet.wearables.response.QuestionnaireResponseListResponse;
import com.hillspet.wearables.response.QuestionnaireViewResponse;
import com.hillspet.wearables.service.questionnaire.QuestionnaireService;

/**
 * Enter detailed explanation of the class here..
 * <p>
 * This class implementation of the <tt>Interface or class Name</tt> interface
 * or class. In addition to implementing the <tt>Interface Name</tt> interface,
 * this class provides methods to do other operations. (Mention other methods
 * purpose)
 *
 * <p>
 * More Description about the class need to be entered here.
 *
 * @author sgorle
 * @version Wearables Portal Relase Version..
 * @since Available Since Wearables Portal Version.
 * @see New line seperated Classes or Interfaces related to this class.
 */
@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

	private static final Logger LOGGER = LogManager.getLogger(QuestionnaireServiceImpl.class);

	@Autowired
	private QuestionnaireDao questionnaireDao;

	@Override
	public void addQuestionnaire(QuestionnaireRequest questionnaireRequest, Integer userId)
			throws ServiceExecutionException {
		LOGGER.debug("addQuestionnaire called");
		questionnaireDao.addQuestionnaire(questionnaireRequest, userId);
		LOGGER.debug("addQuestionnaire completed successfully");
	}

	@Override
	public void updateQuestionnaire(QuestionnaireRequest questionnaireRequest, Integer userId)
			throws ServiceExecutionException {
		LOGGER.debug("updateQuestionnaire called");
		questionnaireDao.updateQuestionnaire(questionnaireRequest, userId);
		LOGGER.debug("updateQuestionnaire completed successfully");
	}

	@Override
	public void addNewInstruction(int questionnaireId, Integer userId, QuestionnaireInstruction instruction)
			throws ServiceExecutionException {
		LOGGER.debug("addNewInstruction called");
		questionnaireDao.addNewInstruction(questionnaireId, userId, instruction);
		LOGGER.debug("addNewInstruction completed successfully");
	}

	@Override
	public void deleteInstruction(int questionnaireId, Integer userId, int instructionId)
			throws ServiceExecutionException {
		LOGGER.debug("deleteInstruction called");
		questionnaireDao.deleteInstruction(questionnaireId, userId, instructionId);
		LOGGER.debug("deleteInstruction completed successfully");
	}

	@Override
	public void updateInstruction(int questionnaireId, Integer userId, QuestionnaireInstruction instruction)
			throws ServiceExecutionException {
		LOGGER.debug("updateInstruction called");
		questionnaireDao.updateInstruction(questionnaireId, userId, instruction);
		LOGGER.debug("updateInstruction completed successfully");
	}

	@Override
	public void addNewQuestion(int questionnaireId, Integer userId, Question question)
			throws ServiceExecutionException {
		LOGGER.debug("addNewQuestion called");
		questionnaireDao.addNewQuestion(questionnaireId, userId, question);
		LOGGER.debug("addNewQuestion completed successfully");
	}

	@Override
	public void deleteQuestion(int questionnaireId, Integer userId, int questionId) throws ServiceExecutionException {
		LOGGER.debug("deleteQuestion called");
		questionnaireDao.deleteQuestion(questionnaireId, userId, questionId);
		LOGGER.debug("deleteQuestion completed successfully");
	}

	@Override
	public void updateQuestion(int questionnaireId, Integer userId, Question question)
			throws ServiceExecutionException {
		LOGGER.debug("updateQuestion called");
		questionnaireDao.updateQuestion(questionnaireId, userId, question);
		LOGGER.debug("updateQuestion completed successfully");
	}

	@Override
	public QuestionnaireResponse getQuestionnaireById(int questionnaireId) throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireById called");
		Questionnaire questionnaire = questionnaireDao.getQuestionnaireById(questionnaireId);

		QuestionnaireResponse response = new QuestionnaireResponse();
		response.setQuestionnaire(questionnaire);

		LOGGER.debug("getQuestionnaireById completed successfully");
		return response;
	}

	@Override
	public QuestionnaireListResponse getQuestionnaires(QuestionnaireFilter filter) throws ServiceExecutionException {
		Map<String, Integer> mapper = questionnaireDao.getQuestionnairesCount(filter);
		int total =	mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<QuestionnaireListDTO> supportList = total > 0 ? questionnaireDao.getQuestionnaires(filter)
				: new ArrayList<>();
		QuestionnaireListResponse response = new QuestionnaireListResponse();
		response.setQuestionnaireList(supportList);
		response.setNoOfElements(supportList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		return response;
	}

	@Override
	public void deleteQuestionnaire(int questionnaireId, int modifiedBy) throws ServiceExecutionException {
		LOGGER.debug("deleteQuestionnaire called");
		questionnaireDao.deleteQuestionnaire(questionnaireId, modifiedBy);
		LOGGER.debug("deleteQuestionnaire completed successfully");

	}

	@Override
	public QuestionnaireListResponse getActiveQuestionnaires() throws ServiceExecutionException {
		LOGGER.debug("getActiveQuestionnaires called");
		List<QuestionnaireListDTO> questionnaires = questionnaireDao.getActiveQuestionnaires();

		QuestionnaireListResponse response = new QuestionnaireListResponse();
		response.setQuestionnaires(questionnaires);

		LOGGER.debug("getActiveQuestionnaires study count is {}", questionnaires.size());
		LOGGER.debug("getActiveQuestionnaires completed successfully");
		return response;
	}
	
	@Override
	public QuestionnaireResponseListResponse getQuestionnaireResponseList(QuestionnaireResponseFilter filter) throws ServiceExecutionException {
		Map<String, Integer> mapper = questionnaireDao.getQuestionnaireResponseCount(filter);
		int total =	mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<QuestionnaireResponseListDTO> questionnaireResponseList = total > 0 ? questionnaireDao.getQuestionnaireResponseList(filter) : new ArrayList<>();
		QuestionnaireResponseListResponse response = new QuestionnaireResponseListResponse();
		response.setQuestionnaireResponseList(questionnaireResponseList);
		response.setNoOfElements(questionnaireResponseList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		return response;
	}

	@Override
	public QuestionnaireResponseByStudyListResponse getQuestionnaireResponseByStudyList(QuestionnaireResponseByStudyFilter filter) throws ServiceExecutionException {
		Map<String, Integer> mapper = questionnaireDao.getQuestionnaireResponseByStudyCount(filter);
		int total =	mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<QuestionnaireResponseByStudyListDTO> questionnaireResponseByStudyListDTOList = total > 0 ? questionnaireDao.getQuestionnaireResponseByStudyList(filter) : new ArrayList<>();
		QuestionnaireResponseByStudyListResponse response = new QuestionnaireResponseByStudyListResponse();
		response.setQuestionnaireResponseByStudyList(questionnaireResponseByStudyListDTOList);
		response.setNoOfElements(questionnaireResponseByStudyListDTOList.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		return response;
	}

	@Override
	public QuestionnaireViewResponse getViewQuestionnaireResponse(int questionnaireResponseId, int studyId) throws ServiceExecutionException {
		LOGGER.debug("getViewQuestionnaireResponse called");
		return questionnaireDao.getViewQuestionnaireResponse(questionnaireResponseId, studyId);
	}
	
	@Override
	public PetQuestionnaireResponseList getQuestionnaireResponseByPet(QuestionnaireResponseFilter filter) throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireResponseByPet called");

		Map<String, Integer> mapper = questionnaireDao.getQuestionnaireResponseByPetCount(filter);
		int total =	mapper.get("count");
		int totalCount = mapper.get("totalCount");
		List<PetQuestionnaireResponse> petQuestionnaireResponses = total > 0 ? questionnaireDao.getQuestionnaireResponseByPet(filter) : new ArrayList<>();
		PetQuestionnaireResponseList response = new PetQuestionnaireResponseList();
		response.setPetQuestionnaireResponses(petQuestionnaireResponses);
		response.setNoOfElements(petQuestionnaireResponses.size());
		response.setTotalRecords(totalCount);
		response.setSearchElments(total);
		
		LOGGER.debug("getQuestionnaireResponseByPet called");
		return response;
	
	}
}
