package com.example.placementapp.pojo;

public class Notification {
    public String companyName;
    public String message;
    public String branch;

    public Notification(String companyName, String message, String branch) {
        this.companyName = companyName;
        this.message = message;
        this.branch = branch;
    }

    public Notification() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
