package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.model.StudentAnswerAnalysis;
import com.qg.AnyWork.model.StudentTestResult;
import com.qg.AnyWork.model.Testpaper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by logan on 2017/7/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDaoTest {
    @Resource
    TestDao testDao;
    private StudentAnswerAnalysis studentAnswerAnalysis;

    @Test
    public void getTestByOrganizationId() throws Exception {
        List<Testpaper> testpapers = testDao.getTestByOrganizationId(1);
        int i = testpapers.size();
        while (i!=0){
            System.out.println(testpapers.get(--i));
        }
    }

    @Test
    public void getPracticeByOrganizationId() throws Exception {
        List<Testpaper> testpapers = testDao.getPracticeByOrganizationId(1);
        int i = testpapers.size();
        while (i!=0){
            System.out.println(testpapers.get(--i));
        }
    }


    @Test
    public void getPracticeByOCId() throws Exception {
        List<Testpaper> testpapers = testDao.getPracticeByOCId(1,2);
        int i = testpapers.size();
        while (i!=0){
            System.out.println(testpapers.get(--i));
        }
    }

    @Test
    public void getQuestionByTestpaperId() throws Exception {
        List<Question> questions = testDao.getQuestionByTestpaperId(1);
        int i = questions.size();
        while (i!=0){
            System.out.println(questions.get(--i));
        }
    }

    @Test
    public void getQuestionById() throws Exception {
        System.out.println(testDao.getQuestionById(1));
    }

    @Test
    public void addTestResult() throws Exception {
        StudentTestResult studentTestResult= new StudentTestResult();
        studentTestResult.setStudentId(5584);
        studentTestResult.setTestpaperId(1);
        studentTestResult.setSocre(100);
        System.out.println(testDao.addTestResult(studentTestResult));
    }

    @Test
    public void addStudentAnswer() throws Exception {
        StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
        studentAnswerAnalysis.setStudentId(5584);
        studentAnswerAnalysis.setStudentAnswer("啊啊啊啊");
        studentAnswerAnalysis.setSocre(5);
        studentAnswerAnalysis.setIsTrue(1);
        Question question = new Question();
        question.setQuestionId(1);
        studentAnswerAnalysis.setQuestion(question);
        System.out.println(testDao.addStudentAnswer(studentAnswerAnalysis));
    }

}