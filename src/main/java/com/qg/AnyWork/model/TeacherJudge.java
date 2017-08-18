package com.qg.AnyWork.model;

public class TeacherJudge {

    int questionId;     //问题id
    double socre;       //老师所给分数

    public TeacherJudge() {
    }


    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public double getSocre() {
        return socre;
    }

    public void setSocre(double socre) {
        this.socre = socre;
    }
}
