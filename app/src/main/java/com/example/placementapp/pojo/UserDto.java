package com.example.placementapp.pojo;

import java.io.Serializable;
import java.util.Map;

public class UserDto implements Serializable {

    private boolean loginSuccessful;

    private Map<String,Double> semResults;

    private String prn;
    private String name;
    private String password;
    private String email;
    private String branch;
    private int division;
    private String mobile;
    private String userType;
    private String year;

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }

    public Map<String, Double> getSemResults() {
        return semResults;
    }

    public void setSemResults(Map<String, Double> semResults) {
        this.semResults = semResults;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDto{");
        sb.append("loginSuccessful=").append(loginSuccessful);
        sb.append(", semResults=").append(semResults);
        sb.append(", prn='").append(prn).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", branch='").append(branch).append('\'');
        sb.append(", division=").append(division);
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", userType='").append(userType).append('\'');
        sb.append(", year='").append(year).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
