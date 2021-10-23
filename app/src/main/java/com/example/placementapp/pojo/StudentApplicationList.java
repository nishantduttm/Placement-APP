package com.example.placementapp.pojo;

import java.util.List;

public class StudentApplicationList {
    private List<StudentApplicationDto> studentApplicationsDto;

    public StudentApplicationList() {
    }

    public List<StudentApplicationDto> getStudentApplicationsDto() {
        return studentApplicationsDto;
    }

    public void setStudentApplicationsDto(List<StudentApplicationDto> studentApplicationsDto) {
        this.studentApplicationsDto = studentApplicationsDto;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StudentApplicationList{");
        sb.append("studentApplicationsDto=").append(studentApplicationsDto);
        sb.append('}');
        return sb.toString();
    }

    public StudentApplicationList(List<StudentApplicationDto> studentApplicationsDto) {
        this.studentApplicationsDto = studentApplicationsDto;
    }
}
