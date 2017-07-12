package com.qg.AnyWork.model;

import java.util.Date;
import java.util.List;

/**
 * Created by logan on 2017/7/10.
 */
public class StudentPaper {
    private int studentPaperId;               //学生答卷id
    private List<StudentAnswer> studentAnswer; //学生做的答案
    private String userName;            //答题者名字
    private int studentId;            //答题者Id
    private int testpaperId;            //试卷id
    private Long startTime;             //开始答题的时间
    private Long endTime;               //答题结束的时间

    public StudentPaper() {
    }

    @Override
    public String toString() {
        return "StudentPaper{" +
                "studentPaperId=" + studentPaperId +
                ", studentAnswer=" + studentAnswer +
                ", userName='" + userName + '\'' +
                ", studentId=" + studentId +
                ", testpaperId=" + testpaperId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public int getStudentPaperId() {
        return studentPaperId;
    }

    public void setStudentPaperId(int studentPaperId) {
        this.studentPaperId = studentPaperId;
    }

    public List<StudentAnswer> getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(List<StudentAnswer> studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTestpaperId() {
        return testpaperId;
    }

    public void setTestpaperId(int testpaperId) {
        this.testpaperId = testpaperId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
