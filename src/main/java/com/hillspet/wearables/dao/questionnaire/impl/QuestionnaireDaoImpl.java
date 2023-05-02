package com.hillspet.wearables.dao.questionnaire.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillspet.wearables.common.constants.SQLConstants;
import com.hillspet.wearables.common.constants.WearablesErrorCode;
import com.hillspet.wearables.common.dto.WearablesError;
import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dao.BaseDaoImpl;
import com.hillspet.wearables.dao.questionnaire.QuestionnaireDao;
import com.hillspet.wearables.dto.PetQuestionnaireResponse;
import com.hillspet.wearables.dto.Question;
import com.hillspet.wearables.dto.QuestionAnswerOption;
import com.hillspet.wearables.dto.QuestionSliderType;
import com.hillspet.wearables.dto.Questionnaire;
import com.hillspet.wearables.dto.QuestionnaireDetailsDTO;
import com.hillspet.wearables.dto.QuestionnaireInstruction;
import com.hillspet.wearables.dto.QuestionnaireListDTO;
import com.hillspet.wearables.dto.QuestionnaireResponseByStudyListDTO;
import com.hillspet.wearables.dto.QuestionnaireResponseDTO;
import com.hillspet.wearables.dto.QuestionnaireResponseListDTO;
import com.hillspet.wearables.dto.filter.QuestionnaireFilter;
import com.hillspet.wearables.dto.filter.QuestionnaireResponseByStudyFilter;
import com.hillspet.wearables.dto.filter.QuestionnaireResponseFilter;
import com.hillspet.wearables.request.QuestionnaireRequest;
import com.hillspet.wearables.response.QuestionnaireViewResponse;

@Repository
public class QuestionnaireDaoImpl extends BaseDaoImpl implements QuestionnaireDao {

	private static final Logger LOGGER = LogManager.getLogger(QuestionnaireDaoImpl.class);

	@Override
	public void addQuestionnaire(QuestionnaireRequest questionnaireRequest, Integer userId)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			Questionnaire questionnaire = questionnaireRequest.getQuestionnaire();
			inputParams.put("p_questionnaire_name", questionnaire.getQuestionnaireName());
			inputParams.put("p_start_date", questionnaire.getStartDate());
			inputParams.put("p_end_date", questionnaire.getEndDate());
			inputParams.put("p_is_published",
					questionnaire.getIsPublished() != null ? questionnaire.getIsPublished() : 0);
			inputParams.put("p_created_by", userId);

			inputParams.put("p_question_json", new ObjectMapper().writeValueAsString(questionnaire.getQuestions()));

			if (questionnaire.getInstructions() != null && questionnaire.getInstructions().size() > 0) {
				inputParams.put("p_instruction_json",
						new ObjectMapper().writeValueAsString(questionnaire.getInstructions()));
			} else {
				inputParams.put("p_instruction_json", null);
			}

			inputParams.put("p_questionnaire_type_id", questionnaire.getQuestionnaireTypeId());
			inputParams.put("p_questionnaire_category_id", questionnaire.getQuestionnaireCategoryId());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_INSERT, inputParams);
			// System.out.println(outParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				// getting the inserted flag value
				Integer questionnaireId = (int) outParams.get("last_insert_id");
				LOGGER.info("Questionnaire has been created successfully, Questionnaire id is ", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"addQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_NAME_ALREADY_EXISTS,
									questionnaireRequest.getQuestionnaire().getQuestionnaireName())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing addQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateQuestionnaire(QuestionnaireRequest questionnaireRequest, Integer userId)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			Questionnaire questionnaire = questionnaireRequest.getQuestionnaire();

			inputParams.put("p_questionnaire_id", questionnaire.getQuestionnaireId());
			inputParams.put("p_questionnaire_name", questionnaire.getQuestionnaireName());
			inputParams.put("p_start_date", questionnaire.getStartDate());
			inputParams.put("p_end_date", questionnaire.getEndDate());
			inputParams.put("p_is_published", questionnaire.getIsPublished());
			inputParams.put("p_modified_by", userId);

			inputParams.put("p_question_json", new ObjectMapper().writeValueAsString(questionnaire.getQuestions()));
			inputParams.put("p_instruction_json",
					new ObjectMapper().writeValueAsString(questionnaire.getInstructions()));

			inputParams.put("p_questionnaire_type_id", questionnaire.getQuestionnaireTypeId());
			inputParams.put("p_questionnaire_category_id", questionnaire.getQuestionnaireCategoryId());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_UPDATE, inputParams);
			// System.out.println(outParams);
			// System.out.println("--------------");
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Questionnaire has been updated successfully, Questionnaire id is ",
						questionnaireRequest.getQuestionnaire().getQuestionnaireId());
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_NAME_ALREADY_EXISTS,
									questionnaireRequest.getQuestionnaire().getQuestionnaireName())));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing updateQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void addNewInstruction(int questionnaireId, Integer userId, QuestionnaireInstruction instruction)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_questionnaire_id", questionnaireId);
			inputParams.put("p_created_by", userId);
			inputParams.put("p_instruction", instruction.getInstruction());
			inputParams.put("p_order", instruction.getInstructionOrder());

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_INSERT_INSTRUCTION,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Instruction has been added successfully to Questionnaire id ", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"addNewInstruction service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(), Arrays.asList(new WearablesError(
									WearablesErrorCode.QUESTIONNAIRE_INSTRUCTION_ALREADY_EXISTS, questionnaireId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing addNewInstruction ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void deleteInstruction(int questionnaireId, Integer userId, int instructionId)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_questionnaire_id", questionnaireId);
			inputParams.put("p_instruction_id", instructionId);
			inputParams.put("p_modified_by", userId);

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_DELETE_INSTRUCTION,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Instruction has been deleted successfully from the Questionnaire, id is", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"deleteInstruction service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_INSTRUCTION_ID_INVALID,
									questionnaireId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteInstruction ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateInstruction(int questionnaireId, Integer userId, QuestionnaireInstruction instruction)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_questionnaire_id", questionnaireId);
			inputParams.put("p_instruction_id", instruction.getInstructionId());
			inputParams.put("p_instruction", instruction.getInstruction());
			inputParams.put("p_instruction_order", instruction.getInstructionOrder());
			inputParams.put("p_modified_by", userId);

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_UPDATE_INSTRUCTION,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Instruction has been updated successfully for Questionnaire id ", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateInstruction service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_INSTRUCTION_ID_INVALID,
									questionnaireId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing updateInstruction ", e);
			throw new ServiceExecutionException(e.getMessage());
		}

	}

	@Override
	public void addNewQuestion(int questionnaireId, Integer userId, Question question)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_questionnaire_id", questionnaireId);
			inputParams.put("p_question_json", new ObjectMapper().writeValueAsString(question));
			inputParams.put("p_created_by", userId);

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_INSERT_QUESTION,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Question has been added successfully to Questionnaire, id is ", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_QUESTION_ALREADY_EXISTS,
									questionnaireId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing updateQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void deleteQuestion(int questionnaireId, Integer userId, int questionId) throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_questionnaire_id", questionnaireId);
			inputParams.put("p_question_id", questionId);
			inputParams.put("p_modified_by", userId);

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_DELETE_QUESTION,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Question has been deleted successfully from the Questionnaire, id is", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"deleteQuestion service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_QUESTION_ID_INVALID,
									questionnaireId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteQuestion ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public void updateQuestion(int questionnaireId, Integer userId, Question question)
			throws ServiceExecutionException {
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("p_questionnaire_id", questionnaireId);
			inputParams.put("p_question_json", new ObjectMapper().writeValueAsString(question));
			inputParams.put("p_created_by", userId);

			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_UPDATE_QUESTION,
					inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			int statusFlag = (int) outParams.get("out_flag");
			if (StringUtils.isEmpty(errorMsg) && statusFlag > NumberUtils.INTEGER_ZERO) {
				LOGGER.info("Question has been added successfully to Questionnaire, id is ", questionnaireId);
			} else {
				if (statusFlag == -2) {
					throw new ServiceExecutionException(
							"updateQuestionnaire service validation failed cannot proceed further",
							Status.BAD_REQUEST.getStatusCode(),
							Arrays.asList(new WearablesError(WearablesErrorCode.QUESTIONNAIRE_QUESTION_ID_INVALID,
									questionnaireId)));
				} else {
					throw new ServiceExecutionException(errorMsg);
				}
			}
		} catch (SQLException | JsonProcessingException e) {
			LOGGER.error("error while executing updateQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Questionnaire getQuestionnaireById(int questionnaireId) throws ServiceExecutionException {
		LOGGER.debug("getQuestionnaireById called");
		Questionnaire questionnaire = new Questionnaire();
		List<QuestionnaireInstruction> instructions = new ArrayList<>();
		List<Question> questions = new ArrayList<>();
		Map<Integer, List<QuestionAnswerOption>> questionAnsOptsMap = new HashMap<>();
		try {
			// in params
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("p_questionnaire_id", questionnaireId);

			Map<String, Object> simpleJdbcCallResult = callStoredProcedure(SQLConstants.QUESTIONNAIRE_GET_BY_ID,
					inputParams);
			Iterator<Entry<String, Object>> itr = simpleJdbcCallResult.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
				String key = entry.getKey();

				if (key.equals(SQLConstants.RESULT_SET_1)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(quesObj -> {
						questionnaire.setQuestionnaireId((Integer) quesObj.get("QUESTIONNAIRE_ID"));
						questionnaire.setQuestionnaireName((String) quesObj.get("QUESTIONNAIRE_NAME"));

						questionnaire.setQuestionnaireTypeId(quesObj.get("QUESTIONNAIRE_TYPE_ID") != null
								? (Integer) quesObj.get("QUESTIONNAIRE_TYPE_ID")
								: null);
						questionnaire.setQuestionnaireCategoryId(quesObj.get("QUESTIONNAIRE_CATEGORY_ID") != null
								? (Integer) quesObj.get("QUESTIONNAIRE_CATEGORY_ID")
								: null);
						questionnaire.setQuestionnaireType((String) quesObj.get("QUESTIONNAIRE_TYPE"));
						questionnaire.setQuestionnaireCategory((String) quesObj.get("QUESTIONNAIRE_CATEGORY"));

						Date startDate = (Date) quesObj.get("START_DATE");
						questionnaire.setStartDate((startDate.toLocalDate()));

						Date endDate = (Date) quesObj.get("END_DATE");
						questionnaire.setEndDate((endDate.toLocalDate()));

						questionnaire.setIsPublished(quesObj.get("IS_PUBLISHED") != null
								&& (Integer) quesObj.get("IS_PUBLISHED") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);
						questionnaire.setIsActive(
								(Integer) quesObj.get("IS_ACTIVE") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);

					});
				}

				if (key.equals(SQLConstants.RESULT_SET_2)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(instruction -> {
						QuestionnaireInstruction questionnaireInstruction = new QuestionnaireInstruction();
						questionnaireInstruction
								.setInstructionId((Integer) instruction.get("QUESTIONNAIRE_INSTRUCTION_ID"));
						questionnaireInstruction.setInstruction((String) instruction.get("INSTRUCTION"));
						questionnaireInstruction.setInstructionOrder((Integer) instruction.get("INSTRUCTION_ORDER"));
						instructions.add(questionnaireInstruction);
					});
					questionnaire.setInstructions(instructions);
				}

				if (key.equals(SQLConstants.RESULT_SET_3)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					list.forEach(answerOpts -> {
						QuestionAnswerOption ansOptions = new QuestionAnswerOption();
						ansOptions.setQuestionAnswerId((Integer) answerOpts.get("QUESTION_ANSWER_OPTION_ID"));
						ansOptions.setQuestionAnswer((String) answerOpts.get("ANSWER"));

						Integer questionId = (Integer) answerOpts.get("QUESTION_ID");

						questionAnsOptsMap.computeIfAbsent(questionId, k -> new ArrayList<QuestionAnswerOption>())
								.add(ansOptions);

					});
				}

				if (key.equals(SQLConstants.RESULT_SET_4)) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();

					list.forEach(quest -> {
						Question question = new Question();
						QuestionSliderType other = new QuestionSliderType();
						Integer questionId = (Integer) quest.get("QUESTION_ID");
						question.setQuestionId(questionId);
						question.setQuestion((String) quest.get("QUESTION"));
						question.setQuestionTypeId((Integer) quest.get("QUESTION_TYPE_ID"));
						question.setQuestionType((String) quest.get("QUESTION_TYPE"));
						question.setQuestionOrder((Integer) quest.get("QUESTION_ORDER"));
						question.setIsMandatory(quest.get("IS_MANDATORY") != null
								&& (Integer) quest.get("IS_MANDATORY") > NumberUtils.INTEGER_ZERO ? Boolean.TRUE
										: Boolean.FALSE);
						other.setCeil((Integer) quest.get("SCALE_MAX"));
						other.setFloor((Integer) quest.get("SCALE_MIN"));
						other.setTickStep((Integer) quest.get("STEP_VALUE"));

						question.setOther(other);

						question.setQuestionAnswerOptions(
								questionAnsOptsMap.get(questionId) != null ? questionAnsOptsMap.get(questionId)
										: new ArrayList<>());
						questions.add(question);
					});
					questionnaire.setQuestions(questions);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error while fetching getQuestionnaireById", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaire;
	}

	@Override
	public Map<String, Integer> getQuestionnairesCount(QuestionnaireFilter filter) throws ServiceExecutionException {
		HashMap<String, Integer> map = new HashMap<>();
		try {
			String counts = selectForObject(SQLConstants.QUESTIONNAIRE_GET_LIST_COUNT, String.class,
					filter.getSearchText(), filter.getQuestionnaireTypeId(), filter.getStatusId(),
					filter.getStartDate(), filter.getEndDate());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnairesCount ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<QuestionnaireListDTO> getQuestionnaires(QuestionnaireFilter filter) throws ServiceExecutionException {
		List<QuestionnaireListDTO> questionnaireList = new ArrayList<>();
		try {
			String sql = SQLConstants.QUESTIONNAIRE_GET_LIST;
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireListDTO questionnaire = new QuestionnaireListDTO();
					questionnaire.setSlNumber(rs.getInt("slNo"));
					questionnaire.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					questionnaire.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					questionnaire.setQuestionnaireTypeId(rs.getInt("QUESTIONNAIRE_TYPE_ID"));
					questionnaire.setQuestionnaireCategoryId(rs.getInt("QUESTIONNAIRE_CATEGORY_ID"));
					questionnaire.setQuestionnaireType(rs.getString("QUESTIONNAIRE_TYPE"));
					questionnaire.setQuestionnaireCategory(rs.getString("QUESTIONNAIRE_CATEGORY"));
					questionnaire.setStartDate(rs.getDate("START_DATE").toLocalDate());
					questionnaire.setEndDate(rs.getDate("END_DATE").toLocalDate());
					questionnaire.setIsActive(rs.getBoolean("IS_ACTIVE"));
					questionnaire.setIsPublished(rs.getBoolean("IS_PUBLISHED"));
					questionnaireList.add(questionnaire);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getQuestionnaireTypeId(), filter.getStatusId(), filter.getStartDate(), filter.getEndDate());
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnaires ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireList;
	}

	@Override
	public void deleteQuestionnaire(int questionnaireId, int modifiedBy) throws ServiceExecutionException {
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("p_questionnaire_id", questionnaireId);
		inputParams.put("p_modified_by", modifiedBy);
		try {
			Map<String, Object> outParams = callStoredProcedure(SQLConstants.QUESTIONNAIRE_DELETE, inputParams);
			String errorMsg = (String) outParams.get("out_error_msg");
			if (StringUtils.isNotEmpty(errorMsg) || (int) outParams.get("out_flag") < NumberUtils.INTEGER_ONE) {
				throw new ServiceExecutionException(errorMsg);
			}
		} catch (SQLException e) {
			LOGGER.error("error while executing deleteQuestionnaire ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
	}

	@Override
	public List<QuestionnaireListDTO> getActiveQuestionnaires() throws ServiceExecutionException {
		List<QuestionnaireListDTO> questionnaireList = new ArrayList<>();
		LOGGER.debug("getActiveQuestionnaires called");
		try {
			jdbcTemplate.query(SQLConstants.QUESTIONNAIRE_GET_ACTIVE_QUESTIONNAIRES, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireListDTO questionnaireListDTO = new QuestionnaireListDTO();
					questionnaireListDTO.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					questionnaireListDTO.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					questionnaireListDTO.setStartDate(rs.getDate("START_DATE").toLocalDate());
					questionnaireListDTO.setEndDate(rs.getDate("END_DATE").toLocalDate());
					questionnaireListDTO.setIsActive(rs.getBoolean("IS_ACTIVE"));
					questionnaireList.add(questionnaireListDTO);
				}
			});

		} catch (Exception e) {
			LOGGER.error("error while fetching getActiveQuestionnaires", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireList;
	}

	@Override
	public Map<String, Integer> getQuestionnaireResponseCount(QuestionnaireResponseFilter filter)
			throws ServiceExecutionException {
		HashMap<String, Integer> map = new HashMap<>();
		try {
			String counts = selectForObject(SQLConstants.QUESTIONNAIRE_RESPONSE_GET_LIST_COUNT, String.class,
					filter.getSearchText(), filter.getQuestionnaireTypeId(), filter.getStudyId(), filter.getStartDate(),
					filter.getEndDate(), filter.getUserId());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnairesCount ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Integer> getQuestionnaireResponseByStudyCount(QuestionnaireResponseByStudyFilter filter)
			throws ServiceExecutionException {
		HashMap<String, Integer> map = new HashMap<>();
		try {
			String counts = selectForObject(SQLConstants.QUESTIONNAIRE_RESPONSE_LIST_BY_STUDY_COUNT, String.class,
					filter.getSearchText(), filter.getFilterType(), filter.getFilterValue(), filter.getStartDate(),
					filter.getEndDate(), filter.getQuestionnaireId(), filter.getStudyId());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnaireResponseByStudyCount ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<QuestionnaireResponseListDTO> getQuestionnaireResponseList(QuestionnaireResponseFilter filter)
			throws ServiceExecutionException {
		List<QuestionnaireResponseListDTO> questionnaireResponseList = new ArrayList<>();
		try {
			String sql = SQLConstants.QUESTIONNAIRE_RESPONSE_GET_LIST;
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireResponseListDTO questionnaire = new QuestionnaireResponseListDTO();
					questionnaire.setSlNumber(rs.getInt("slNo"));
					questionnaire.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					questionnaire.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					questionnaire.setStudy(rs.getString("STUDY_NAME"));
					questionnaire.setStudyId(rs.getInt("STUDY_ID"));
					questionnaire.setStartDate(rs.getDate("START_DATE").toLocalDate());
					questionnaire.setEndDate(rs.getDate("END_DATE").toLocalDate());

					questionnaire.setSubmitedON(rs.getDate("SUBMITED_ON").toLocalDate());
					questionnaire.setPetName(rs.getString("PET_NAME"));
					questionnaire.setRespondentPetParentName(rs.getString("PET_PARENT_NAME"));
					questionnaire.setCompletionPercentage(rs.getDouble("COMPLETION_PERCENTAGE"));

					questionnaireResponseList.add(questionnaire);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getQuestionnaireTypeId(), filter.getStudyId(), filter.getStartDate(), filter.getEndDate(),
					filter.getUserId());
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnaires ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireResponseList;
	}

	@Override
	public List<QuestionnaireResponseByStudyListDTO> getQuestionnaireResponseByStudyList(
			QuestionnaireResponseByStudyFilter filter) throws ServiceExecutionException {
		List<QuestionnaireResponseByStudyListDTO> questionnaireResponseList = new ArrayList<>();
		try {
			String sql = SQLConstants.QUESTIONNAIRE_RESPONSE_LIST_BY_STUDY;
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireResponseByStudyListDTO questionnaire = new QuestionnaireResponseByStudyListDTO();
					questionnaire.setPetParentName(rs.getString("FULL_NAME"));
					questionnaire.setPetId(rs.getInt("PET_ID"));
					questionnaire.setQuestionnaireResponseId(rs.getInt("QUESTIONNAIRE_RESPONSE_ID"));
					questionnaire.setPetName(rs.getString("PET_NAME"));
					questionnaire.setSubmittedDate(rs.getTimestamp("CREATED_DATE"));
					questionnaire.setSharedDate(rs.getDate("START_DATE") == null ? null : rs.getDate("START_DATE").toLocalDate());
					questionnaire.setQuestionnaireId(Integer.valueOf(filter.getQuestionnaireId()));
					questionnaire.setStudyId(Integer.valueOf(filter.getStudyId()));
					questionnaireResponseList.add(questionnaire);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getFilterType(), filter.getFilterValue(), filter.getStartDate(), filter.getEndDate(),
					filter.getQuestionnaireId(), filter.getStudyId());
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnaireResponseByStudyList ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return questionnaireResponseList;
	}

	@Override
	public QuestionnaireViewResponse getViewQuestionnaireResponse(int questionnaireResponseId, int studyId)
			throws ServiceExecutionException {
		QuestionnaireViewResponse response = new QuestionnaireViewResponse();
		List<QuestionnaireResponseDTO> questionnaireResponseDTOList = new ArrayList<>();
		response.setQuestionnaireResponseList(questionnaireResponseDTOList);
		try {
			String sql = SQLConstants.QUESTIONNAIRE_VIEW_RESPONSE_DETAILS;
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireDetailsDTO detailsDTO = new QuestionnaireDetailsDTO();
					detailsDTO.setPetName(rs.getString("PET_NAME"));
					detailsDTO.setPetParentName(rs.getString("FULL_NAME"));
					detailsDTO.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					detailsDTO.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					detailsDTO.setSubmittedDate(rs.getTimestamp("CREATED_DATE"));
					detailsDTO.setStudyId(rs.getInt("STUDY_ID"));
					detailsDTO.setStudyName(rs.getString("STUDY_NAME"));
					response.setQuestionnaireDetails(detailsDTO);
				}
			}, questionnaireResponseId, studyId);
			sql = SQLConstants.QUESTIONNAIRE_VIEW_RESPONSE;
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					QuestionnaireResponseDTO questionnaire = new QuestionnaireResponseDTO();
					questionnaire.setQuestion(rs.getString("QUESTION"));
					questionnaire.setAnswer(rs.getString("ANSWER"));
					questionnaireResponseDTOList.add(questionnaire);
				}
			}, questionnaireResponseId);
		} catch (Exception e) {
			LOGGER.error("error while executing getViewQuestionnaireResponse ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Integer> getQuestionnaireResponseByPetCount(QuestionnaireResponseFilter filter)
			throws ServiceExecutionException {
		HashMap<String, Integer> map = new HashMap<>();
		try {
			String counts = selectForObject(SQLConstants.QUESTIONNAIRE_RESPONSE_BY_PET_COUNT, String.class,
					filter.getSearchText(), filter.getPetId(), filter.getFilterType(), filter.getFilterValue(),
					filter.getStartDate(), filter.getEndDate());
			map = new ObjectMapper().readValue(counts, HashMap.class);
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnaireResponseByPetCount ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return map;
	}

	@Override
	public List<PetQuestionnaireResponse> getQuestionnaireResponseByPet(QuestionnaireResponseFilter filter)
			throws ServiceExecutionException {
		List<PetQuestionnaireResponse> petQuestionnaireResponses = new ArrayList<>();
		try {
			String sql = SQLConstants.QUESTIONNAIRE_RESPONSE_LIST_BY_PET;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					PetQuestionnaireResponse petQuestionnaireResponse = new PetQuestionnaireResponse();
					petQuestionnaireResponse.setPetId(rs.getInt("PET_ID"));
					petQuestionnaireResponse.setPetName(rs.getString("PET_NAME"));
					petQuestionnaireResponse.setQuestionnaireResponseId(rs.getInt("QUESTIONNAIRE_RESPONSE_ID"));
					petQuestionnaireResponse.setQuestionnaireId(rs.getInt("QUESTIONNAIRE_ID"));
					petQuestionnaireResponse.setQuestionnaireName(rs.getString("QUESTIONNAIRE_NAME"));
					petQuestionnaireResponse.setQuestionId(rs.getInt("QUESTION_ID"));
					petQuestionnaireResponse.setQuestionName(rs.getString("QUESTION"));
					petQuestionnaireResponse.setQuestionTypeId(rs.getInt("QUESTION_TYPE_ID"));
					petQuestionnaireResponse.setQuestionType(rs.getString("QUESTION_TYPE"));
					petQuestionnaireResponse.setAnswerOpts(rs.getString("ANSWER_OPTS"));
					petQuestionnaireResponse.setAnswer(rs.getString("ANSWER"));
					petQuestionnaireResponse.setStudyNames(rs.getString("STUDY_NAMES"));
					petQuestionnaireResponse.setSubmittedDate(rs.getTimestamp("CREATED_DATE"));
					petQuestionnaireResponse.setSubmittedOn(rs.getTimestamp("CREATED_DATE") != null
							? rs.getTimestamp("CREATED_DATE").toLocalDateTime().format(formatter)
							: null);
					petQuestionnaireResponses.add(petQuestionnaireResponse);
				}
			}, filter.getStartIndex(), filter.getLimit(), filter.getOrder(), filter.getSortBy(), filter.getSearchText(),
					filter.getPetId(), filter.getFilterType(), filter.getFilterValue(), filter.getStartDate(),
					filter.getEndDate());
		} catch (Exception e) {
			LOGGER.error("error while executing getQuestionnaireResponseByPet ", e);
			throw new ServiceExecutionException(e.getMessage());
		}
		return petQuestionnaireResponses;
	}
}
