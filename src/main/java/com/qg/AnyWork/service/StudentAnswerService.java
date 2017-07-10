package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.StudentAnswerDao;
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
public class StudentAnswerService {

    @Autowired
    private StudentAnswerDao studentAnswerDao;

    public StudentTestResult getResult(StudentPaper studentPaper){

        StudentTestResult studentTestResult = new StudentTestResult();
        studentTestResult.setStudentId(studentPaper.getStudentId());
        studentTestResult.setTextpaperId(studentPaper.getTextpaperId());

        double socre = 0;


        List<StudentAnswer> studentAnswers = studentPaper.getStudentAnswer();

        for (StudentAnswer studentAnswer : studentAnswers){
            StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
            //获取相应试题（包含正确答案）
            Question question = studentAnswerDao.getQuestionById(studentAnswer.getQuestionId());
            studentAnswerAnalysis.setQuestion(question);
            studentAnswerAnalysis.setStudentAnswer(studentAnswer.getStudentAnswer());

            if (studentAnswer.getStudentAnswer().equals(question.getKey())) {
                socre += question.getSocre();
                studentAnswerAnalysis.setIsTrue(1);
                studentAnswerAnalysis.setSocre(question.getSocre());
            }

        }

        studentTestResult.setSocre(socre);
        return studentTestResult;
    }
}
