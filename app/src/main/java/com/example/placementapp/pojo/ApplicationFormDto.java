package com.example.placementapp.pojo;

import java.io.Serializable;

public class ApplicationFormDto implements Serializable {
    public int applicationId;
    public int notificationId;
    public String prn;
    public String overallStatus;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getPrn() {
        return prn;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }
}
