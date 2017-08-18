package com.qg.AnyWork.model;

public class CheckResult {

    int studentId;          //学生id
    String studentName;     //学生姓名
    int ifCheck;            //是否评卷
    double object;          //客观题分数
    double subject;         //主观题分数
    int ifAttend;               //是否参与考试
    Testpaper testpaper;    //相应的试卷类

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getIfCheck() {
        return ifCheck;
    }

    public void setIfCheck(int ifCheck) {
        this.ifCheck = ifCheck;
    }

    public double getObject() {
        return object;
    }

    public void setObject(double object) {
        this.object = object;
    }

    public double getSubject() {
        return subject;
    }

    public void setSubject(double subject) {
        this.subject = subject;
    }

    public int getIfAttend() {
        return ifAttend;
    }

    public void setIfAttend(int ifAttend) {
        this.ifAttend = ifAttend;
    }

    public Testpaper getTestpaper() {
        return testpaper;
    }

    public void setTestpaper(Testpaper testpaper) {
        this.testpaper = testpaper;
    }
}
