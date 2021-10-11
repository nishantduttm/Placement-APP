package com.example.placementapp.pojo;

import java.util.Map;

public class User {
    private String mail;
    private String password;
    private String name;
    private String type;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("mail='").append(mail).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
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
