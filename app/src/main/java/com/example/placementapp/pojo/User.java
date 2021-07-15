package com.example.placementapp.pojo;

import java.util.Map;

public class User {
    private String mail;
    private String password;
    private String name;
    private String type;
    private String year;
    private String div;
    private String mobile;
    private Map<String,String> semResults;

    public User(String mail, String password, String name, String type, String year, String div, String mobile, Map<String, String> semResults) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.type = type;
        this.year = year;
        this.div = div;
        this.mobile = mobile;
        this.semResults = semResults;
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

    public User(String mail, String password, String name, String type) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
