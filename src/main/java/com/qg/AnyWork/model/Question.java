package com.qg.AnyWork.model;

/**
 * 问题实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Question {

    private int questionId;     //问题id
    private int type;           //题目类型  1-选择题 2-判断题 3-填空题 4-问答题 5-编程题 6-综合题
    private String A;
    private String B;
    private String C;
    private String D;
    private String key;         //答案
    private String content;     //题目内容
    private double socre;          //分数
    private int testpaperId;    //试卷id
    private int other;          //填空题个数

    public Question(){}

    // get & set

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSocre() {
        return socre;
    }

    public void setSocre(double socre) {
        this.socre = socre;
    }

    public int getTestpaperId() {
        return testpaperId;
    }

    public void setTestpaperId(int testpaperId) {
        this.testpaperId = testpaperId;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }


    //toString

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", type=" + type +
                ", A='" + A + '\'' +
                ", B='" + B + '\'' +
                ", C='" + C + '\'' +
                ", D='" + D + '\'' +
                ", key='" + key + '\'' +
                ", content='" + content + '\'' +
                ", socre=" + socre +
                ", testpaperId=" + testpaperId +
                ", other=" + other +
                '}';
    }
}
