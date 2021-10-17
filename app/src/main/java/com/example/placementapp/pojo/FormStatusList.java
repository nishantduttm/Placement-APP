package com.example.placementapp.pojo;

import java.util.List;

public class FormStatusList {

    private List<FormStatus> formStatusList;

    public FormStatusList() {
    }

    public FormStatusList(List<FormStatus> formStatusList) {
        this.formStatusList = formStatusList;
    }

    public List<FormStatus> getFormStatusList() {
        return formStatusList;
    }

    public void setFormStatusList(List<FormStatus> formStatusList) {
        this.formStatusList = formStatusList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FormStatusList{");
        sb.append("formStatusList=").append(formStatusList);
        sb.append('}');
        return sb.toString();
    }
}
