package com.example.placementapp.pojo;

import java.io.Serializable;

public class StudentApplicationDto implements Serializable {
    private int applicationId;
    private int notificationId;
    private String prn;
    private String name;
    private String email;
    private String branch;
    private String companyName;
    private String overallStatus;

    public StudentApplicationDto() {
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StudentApplicationDto{");
        sb.append("applicationId=").append(applicationId);
        sb.append(", notificationId=").append(notificationId);
        sb.append(", prn='").append(prn).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", branch='").append(branch).append('\'');
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", overallStatus='").append(overallStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
