package com.example.placementapp.pojo;

import com.example.placementapp.utils.StringUtils;

import java.io.Serializable;

public class NotificationDto implements Serializable {
    public int notificationId;
    public String companyName;
    public String companyBranch;
    public String date;
    public String companyPackage;
    public String companyVenue;
    public String companyDescription;
    public long count;

    public NotificationDto(String companyName, String companyBranch, String date, String companyPackage, String companyVenue, String companyDescription) {
        this.companyName = companyName;
        this.companyBranch = companyBranch;
        this.date = date;
        this.companyPackage = companyPackage;
        this.companyVenue = companyVenue;
        this.companyDescription = companyDescription;
        count = 0;
    }

    public NotificationDto() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(String companyBranch) {
        this.companyBranch = companyBranch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyPackage() {
        return companyPackage;
    }

    public void setCompanyPackage(String companyPackage) {
        this.companyPackage = companyPackage;
    }

    public String getCompanyVenue() {
        return companyVenue;
    }

    public void setCompanyVenue(String companyVenue) {
        this.companyVenue = companyVenue;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotificationDto{");
        sb.append("notificationId='").append(notificationId).append('\'');
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", companyBranch='").append(companyBranch).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", companyPackage='").append(companyPackage).append('\'');
        sb.append(", companyVenue='").append(companyVenue).append('\'');
        sb.append(", companyDescription='").append(companyDescription).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
