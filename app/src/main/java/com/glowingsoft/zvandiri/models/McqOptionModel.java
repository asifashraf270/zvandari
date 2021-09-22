package com.glowingsoft.zvandiri.models;

public class McqOptionModel {
    String id, questionID, option, isCorrect;

    public McqOptionModel(String id, String questionID, String option, String isCorrect) {
        this.id = id;
        this.questionID = questionID;
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getId() {
        return id;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getOption() {
        return option;
    }

    public String getIsCorrect() {
        return isCorrect;
    }


}
