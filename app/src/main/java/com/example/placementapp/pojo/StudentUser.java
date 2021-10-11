package com.example.placementapp.pojo;

import java.io.Serializable;
import java.util.Map;

public class StudentUser extends User implements Serializable {
    private String branch;
    private String prn;
    private String year;
    private String div;
    private String mobile;
    private Map<String,String> semResults;

    public StudentUser(String mail, String password, String name, String type, String branch, String prn) {
        super(mail, password, name, type);
        this.branch = branch;
        this.prn = prn;
    }

    public StudentUser(String mail, String password, String name, String type, String branch, String prn, String year, String div, String mobile, Map<String, String> semResults) {
        super(mail, password, name, type);
        this.branch = branch;
        this.prn = prn;
        this.year = year;
        this.div = div;
        this.mobile = mobile;
        this.semResults = semResults;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StudentUser{");
        sb.append("branch='").append(branch).append('\'');
        sb.append(", prn='").append(prn).append('\'');
        sb.append(", year='").append(year).append('\'');
        sb.append(", div='").append(div).append('\'');
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", semResults=").append(semResults);
        sb.append('}');
        return sb.toString();
    }

    public StudentUser() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String, String> getSemResults() {
        return semResults;
    }

    public void setSemResults(Map<String, String> semResults) {
        this.semResults = semResults;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
