package com.example.placementapp.pojo;

public class Notification {
    public String companyName;
    public String message;
    public String timestamp;
    public String[] branch;

    public Notification(String companyName, String message, String timestamp) {
        this.companyName = companyName;
        this.message = message;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
