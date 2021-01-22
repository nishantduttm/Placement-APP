package com.example.placementapp.pojo;

public class StudentUser extends User{
    private String branch;
    private String prn;

    public StudentUser(String mail, String password, String name, String type, String branch, String prn) {
        super(mail, password, name, type);
        this.branch = branch;
        this.prn = prn;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
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
