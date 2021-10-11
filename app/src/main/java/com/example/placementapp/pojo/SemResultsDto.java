package com.example.placementapp.pojo;

public class SemResultsDto {
    private double sem1;
    private double sem2;
    private double sem3;
    private double sem4;
    private double sem5;
    private double sem6;
    private double sem7;
    private double sem8;
    private double cgpa;
    private double percentage;

    public SemResultsDto() {
    }

    public double getSem1() {
        return sem1;
    }

    public void setSem1(double sem1) {
        this.sem1 = sem1;
    }

    public double getSem2() {
        return sem2;
    }

    public void setSem2(double sem2) {
        this.sem2 = sem2;
    }

    public double getSem3() {
        return sem3;
    }

    public void setSem3(double sem3) {
        this.sem3 = sem3;
    }

    public double getSem4() {
        return sem4;
    }

    public void setSem4(double sem4) {
        this.sem4 = sem4;
    }

    public double getSem5() {
        return sem5;
    }

    public void setSem5(double sem5) {
        this.sem5 = sem5;
    }

    public double getSem6() {
        return sem6;
    }

    public void setSem6(double sem6) {
        this.sem6 = sem6;
    }

    public double getSem7() {
        return sem7;
    }

    public void setSem7(double sem7) {
        this.sem7 = sem7;
    }

    public double getSem8() {
        return sem8;
    }

    public void setSem8(double sem8) {
        this.sem8 = sem8;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SemResultsDto{");
        sb.append("sem1=").append(sem1);
        sb.append(", sem2=").append(sem2);
        sb.append(", sem3=").append(sem3);
        sb.append(", sem4=").append(sem4);
        sb.append(", sem5=").append(sem5);
        sb.append(", sem6=").append(sem6);
        sb.append(", sem7=").append(sem7);
        sb.append(", sem8=").append(sem8);
        sb.append(", cgpa=").append(cgpa);
        sb.append(", percentage=").append(percentage);
        sb.append('}');
        return sb.toString();
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
