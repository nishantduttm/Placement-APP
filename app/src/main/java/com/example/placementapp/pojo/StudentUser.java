package com.example.placementapp.pojo;

public class StudentUser extends User{
    private String branch;

    public StudentUser(String mail, String password, String name, String type, String branch) {
        super(mail, password, name, type);
        this.branch = branch;
    }

    public StudentUser() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
