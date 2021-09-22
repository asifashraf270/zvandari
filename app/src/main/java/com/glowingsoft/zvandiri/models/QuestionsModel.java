package com.glowingsoft.zvandiri.models;

import java.util.List;

/**
 * Created by Asif on 2/8/2019.
 */

public class QuestionsModel {
    String question;
    String questionId, Answer, cat_id;
    List<McqOptionModel> model;

    public QuestionsModel(String question, String questionId, String answer, String cat_id, List<McqOptionModel> model) {
        this.question = question;
        this.questionId = questionId;
        Answer = answer;
        this.cat_id = cat_id;
        this.model = model;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getCat_id() {
        return cat_id;
    }

    public List<McqOptionModel> getModel() {
        return model;
    }
}
