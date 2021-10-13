package com.example.placementapp.pojo;

import java.io.Serializable;

public class QuestionDto implements Serializable {

    private int questionId;
    private String question;

    public QuestionDto() {
    }

    public QuestionDto(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionDto{");
        sb.append("questionId=").append(questionId);
        sb.append(", question='").append(question).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
