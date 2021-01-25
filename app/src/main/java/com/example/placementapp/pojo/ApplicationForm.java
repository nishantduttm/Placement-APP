package com.example.placementapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.List;
import java.util.Set;

public class ApplicationForm  extends GeneralApplicationForm implements Parcelable{


    private String companyName;
    private String companyId;
    private List<FormStatus> formStatusList;
    private String overallStatus;


    protected ApplicationForm(Parcel in) {
        super(in.readString(), in.readString(), in.readString(),in.readString());
        companyName = in.readString();
        companyId = in.readString();
        overallStatus = in.readString();

        in.readTypedList(formStatusList, FormStatus.CREATOR);
    }

    public static final Creator<ApplicationForm> CREATOR = new Creator<ApplicationForm>() {
        @Override
        public ApplicationForm createFromParcel(Parcel in) {
            return new ApplicationForm(in);
        }

        @Override
        public ApplicationForm[] newArray(int size) {
            return new ApplicationForm[size];
        }
    };

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public List<FormStatus> getFormStatusList() {
        return formStatusList;
    }

    public void setFormStatusList(List<FormStatus> formStatusList) {
        this.formStatusList = formStatusList;
    }

    public ApplicationForm(String studentMailID, String studentPRN, String studentName, String studentBranch, String companyName, String companyId, List<FormStatus> formStatusList, String overallStatus) {
        super(studentMailID, studentPRN, studentName, studentBranch);
        this.companyName = companyName;
        this.companyId = companyId;
        this.formStatusList = formStatusList;
        this.overallStatus = overallStatus;
    }

    public ApplicationForm() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getStudentMailID());
        dest.writeString(getStudentPRN());
        dest.writeString(getStudentName());
        dest.writeString(getStudentBranch());
        dest.writeString(companyName);
        dest.writeString(companyId);
        dest.writeString(overallStatus);
        dest.writeTypedList(formStatusList);
    }
}
