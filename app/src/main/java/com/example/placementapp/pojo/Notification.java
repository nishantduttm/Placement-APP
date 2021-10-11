package com.example.placementapp.pojo;

import java.io.Serializable;

public class Notification implements Serializable {
    public String time;
    public String companyName;
    public String venue;
    public String branch;
    public String salary;
    public String eligibility;
    public String count;
    public String date;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Notification(String time,String companyName, String venue, String branch, String salary, String eligibility, String date, String count) {
        this.time = time;
        this.companyName = companyName;
        this.venue = venue;
        this.branch = branch;
        this.salary = salary;
        this.eligibility = eligibility;
        this.date = date;
        this.count = count;
    }

    public Notification() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
