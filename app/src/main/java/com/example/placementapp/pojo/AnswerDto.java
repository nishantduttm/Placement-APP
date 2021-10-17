package com.example.placementapp.pojo;

import java.io.Serializable;

public class AnswerDto implements Serializable {

    private Integer questionId;
    private Integer answerId;
    private String answer;
    private String studentName;

    public AnswerDto(Integer questionId, Integer answerId, String answer, String studentName) {
        this.questionId = questionId;
        this.answerId = answerId;
        this.answer = answer;
        this.studentName = studentName;
    }

    public AnswerDto() {
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnswerDto{");
        sb.append("questionId=").append(questionId);
        sb.append(", answerId=").append(answerId);
        sb.append(", answer='").append(answer).append('\'');
        sb.append(", studentName='").append(studentName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
