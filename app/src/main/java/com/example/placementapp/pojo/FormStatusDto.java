package com.example.placementapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class FormStatusDto{
    private int formStatusId;
    private int applicationFormId;
    private String processRound;
    private String processDate;

    public FormStatusDto() {
    }

    public int getFormStatusId() {
        return formStatusId;
    }

    public void setFormStatusId(int formStatusId) {
        this.formStatusId = formStatusId;
    }

    public int getApplicationFormId() {
        return applicationFormId;
    }

    public void setApplicationFormId(int applicationFormId) {
        this.applicationFormId = applicationFormId;
    }

    public String getProcessRound() {
        return processRound;
    }

    public void setProcessRound(String processRound) {
        this.processRound = processRound;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FormStatusDto{");
        sb.append("formStatusId=").append(formStatusId);
        sb.append(", applicationFormId=").append(applicationFormId);
        sb.append(", processRound='").append(processRound).append('\'');
        sb.append(", processDate='").append(processDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
