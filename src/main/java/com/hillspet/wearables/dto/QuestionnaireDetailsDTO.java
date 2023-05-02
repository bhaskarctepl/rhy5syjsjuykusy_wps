package com.hillspet.wearables.dto;

import java.sql.Timestamp;

public class QuestionnaireDetailsDTO {
	private Integer questionnaireId;
	private String questionnaireName;
	private Integer studyId;
	private String studyName;
	private String petName;
	private String petParentName;
	private Timestamp submittedDate;

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getQuestionnaireName() {
		return questionnaireName;
	}

	public void setQuestionnaireName(String questionnaireName) {
		this.questionnaireName = questionnaireName;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public void setStudyId(Integer studyId) {
		this.studyId = studyId;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetParentName() {
		return petParentName;
	}

	public void setPetParentName(String petParentName) {
		this.petParentName = petParentName;
	}

	public Timestamp getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Timestamp submittedDate) {
		this.submittedDate = submittedDate;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("QuestionnaireDetailsDTO{");
		sb.append("questionnaireName='").append(questionnaireName).append('\'');
		sb.append(", studyName='").append(studyName).append('\'');
		sb.append(", petName='").append(petName).append('\'');
		sb.append(", petParentName='").append(petParentName).append('\'');
		sb.append(", submittedDate='").append(submittedDate).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
