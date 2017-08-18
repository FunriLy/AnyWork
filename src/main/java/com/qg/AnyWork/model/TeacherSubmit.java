package com.qg.AnyWork.model;

import java.util.List;

/**
 * Created by logan on 2017/8/1.
 */
public class TeacherSubmit {

    private int studentId;            //答题者Id
    private int testpaperId;            //试卷id
    private List<TeacherJudge> teacherJudge;  //老师评分

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

    public List<TeacherJudge> getTeacherJudge() {
        return teacherJudge;
    }

    public void setTeacherJudge(List<TeacherJudge> teacherJudge) {
        this.teacherJudge = teacherJudge;
    }
}
