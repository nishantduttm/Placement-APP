package com.example.placementapp.pojo;

import java.io.Serializable;

public class GeneralApplicationForm {

    private String studentMailID;
    private String studentPRN;
    private String studentName;
    private String studentBranch;


    public GeneralApplicationForm(String studentMailID, String studentPRN, String studentName, String studentBranch) {
        this.studentMailID = studentMailID;
        this.studentPRN = studentPRN;
        this.studentName = studentName;
        this.studentBranch = studentBranch;
    }

    public GeneralApplicationForm() {
    }


    public String getStudentMailID() {
        return studentMailID;
    }

    public void setStudentMailID(String studentMailID) {
        this.studentMailID = studentMailID;
    }

    public String getStudentPRN() {
        return studentPRN;
    }

    public void setStudentPRN(String studentPRN) {
        this.studentPRN = studentPRN;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentBranch() {
        return studentBranch;
    }

    public void setStudentBranch(String studentBranch) {
        this.studentBranch = studentBranch;
    }

}
