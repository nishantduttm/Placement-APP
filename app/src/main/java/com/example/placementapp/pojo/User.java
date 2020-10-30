package com.example.placementapp.pojo;

public class User {
    private String mail;
    private String password;
    private String name;
    private String branch;
    private String type;

    public User(String mail, String password, String name, String branch, String type) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.branch = branch;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
