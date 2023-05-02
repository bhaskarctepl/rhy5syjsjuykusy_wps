package com.hillspet.wearables.response;

import com.hillspet.wearables.dto.QuestionnaireDetailsDTO;
import com.hillspet.wearables.dto.QuestionnaireResponseDTO;

import java.util.List;

public class QuestionnaireViewResponse {
    private QuestionnaireDetailsDTO questionnaireDetails;
    private List<QuestionnaireResponseDTO> questionnaireResponseList;

    public QuestionnaireDetailsDTO getQuestionnaireDetails() {
        return questionnaireDetails;
    }

    public void setQuestionnaireDetails(QuestionnaireDetailsDTO questionnaireDetails) {
        this.questionnaireDetails = questionnaireDetails;
    }

    public List<QuestionnaireResponseDTO> getQuestionnaireResponseList() {
        return questionnaireResponseList;
    }

    public void setQuestionnaireResponseList(List<QuestionnaireResponseDTO> questionnaireResponseList) {
        this.questionnaireResponseList = questionnaireResponseList;
    }
}
