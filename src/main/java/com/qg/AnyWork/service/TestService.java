package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.TestDao;
import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by logan on 2017/7/10.
 */
@Service
public class TestService {

    @Autowired
    private TestDao testDao;

    /***
     * 获得考试结果
     * @param studentPaper  考试试卷
     * @return
     */
    public StudentTestResult getResult(StudentPaper studentPaper){

        StudentTestResult studentTestResult = new StudentTestResult();
        studentTestResult.setStudentId(studentPaper.getStudentId());
        studentTestResult.setTestpaperId(studentPaper.getTestpaperId());

        List<StudentAnswerAnalysis> studentAnswerAnalysises = new ArrayList<StudentAnswerAnalysis>();

        double socre = 0;


        List<StudentAnswer> studentAnswers = studentPaper.getStudentAnswer();

        for (StudentAnswer studentAnswer : studentAnswers){

            StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
            //获取相应试题（包含正确答案）
            Question question = testDao.getQuestionById(studentAnswer.getQuestionId());
            //分析题目类注入值
            studentAnswerAnalysis.setQuestion(question);
            studentAnswerAnalysis.setStudentAnswer(studentAnswer.getStudentAnswer());
            //选择判断
            if (question.getType()==1||question.getType()==2) {
                if (studentAnswer.getStudentAnswer().equals(question.getKey())) {
                    socre += question.getSocre();
                    studentAnswerAnalysis.setIsTrue(1);
                    studentAnswerAnalysis.setSocre(question.getSocre());
                }else{
                    studentAnswerAnalysis.setIsTrue(0);
                    studentAnswerAnalysis.setSocre(0);
                }
            }
            //填空
            if (question.getType()==3){
                int isTrue = 1;
                String SPLIT = "$";
                int index;
                int number = question.getOther();
                double fillingSocre = 0;
                //正确答案数组
                String[] answer = question.getKey().split(SPLIT);
               //学生答案数组
                String[] studentFillingAnswer = studentAnswer.getStudentAnswer().split(SPLIT);

                for (index = 0 ; index < number ; index++){
                    if (answer[index].equals(studentFillingAnswer[index]))
                        fillingSocre += question.getSocre()/number;
                    else
                        isTrue = 0;
                }
                studentAnswerAnalysis.setIsTrue(isTrue==1?1:0);
                studentAnswerAnalysis.setSocre(fillingSocre);
                socre += fillingSocre;
            }
            studentAnswerAnalysises.add(studentAnswerAnalysis);
        }

        studentTestResult.setStudentAnswerAnalysis(studentAnswerAnalysises);
        studentTestResult.setSocre(socre);
        return studentTestResult;
    }


    /**
     * 增加一张试卷/练习
     * @param testpaper
     */
    public void addTestpaper(Testpaper testpaper){
        testDao.addTestpaper(testpaper);
    }

    //更新一张试卷/练习的总分
    public boolean updateTextpaper(int testpaperId, int socre){
        if(testDao.updateSocreOfTestpaper(testpaperId, socre) == 1){
            return true;
        }
        return false;
    }
}
