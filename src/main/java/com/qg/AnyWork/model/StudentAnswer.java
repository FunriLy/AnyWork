package com.qg.AnyWork.model;

import java.util.Date;

/**
 * 学生答案实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class StudentAnswer {

    private int answerId;               //答案id
    private String choiceAnswer;        //选择题答案
    private String judgeAnswer;         //判断题答案
    private String fillingAnswer;       //填空题答案
    private String askingAnswer;        //问答题答案
    private String codeAnswer;          //编程题答案
    private String comprehensiveAnswer; //综合题答案
    private String userName;            //答题者名字
    private int textpaperId;            //试卷id
    private double source;              //答题分数
    private Date startTime;             //开始答题的时间
    private Date endTime;               //答题结束的时间
    private String rightAnswer;         //正确答案
    private String judege;              //答题情况

    // TODO: 2017/7/10 该实体为学生提交试卷答案时使用，需要与前端移动端商量答案的间隔符
}
