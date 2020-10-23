package com.example.placementapp.pojo;

public class User {
    private String mail;
    private String password;
    private String username;
    private String type;

    public User(String mail, String password, String username, String type) {
        this.mail = mail;
        this.password = password;
        this.username = username;
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
