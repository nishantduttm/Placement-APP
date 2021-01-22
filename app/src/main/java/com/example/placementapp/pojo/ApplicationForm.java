package com.example.placementapp.pojo;

import java.util.List;

public class ApplicationForm extends GeneralApplicationForm{

    private String companyName;
    private String companyId;
    private List<FormStatus> formStatusList;

    public List<FormStatus> getFormStatusList() {
        return formStatusList;
    }

    public void setFormStatusList(List<FormStatus> formStatusList) {
        this.formStatusList = formStatusList;
    }

    public ApplicationForm(String studentMailID, String studentPRN, String studentName, String studentBranch, String companyName, String companyId, List<FormStatus> formStatusList) {
        super(studentMailID, studentPRN, studentName, studentBranch);
        this.companyName = companyName;
        this.companyId = companyId;
        this.formStatusList = formStatusList;
    }

    public ApplicationForm(String companyName, String companyId) {
        this.companyName = companyName;
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
