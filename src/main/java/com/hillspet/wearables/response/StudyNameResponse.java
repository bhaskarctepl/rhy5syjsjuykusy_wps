package com.hillspet.wearables.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hillspet.wearables.dto.StudyName;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyNameResponse {
    List<StudyName> studyNameList;

    public List<StudyName> getStudyNameList() {
        return studyNameList;
    }

    public void setStudyNameList(List<StudyName> studyNameList) {
        this.studyNameList = studyNameList;
    }
}
