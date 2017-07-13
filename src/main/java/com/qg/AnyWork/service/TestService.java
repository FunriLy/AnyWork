package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.OrganizationDao;
import com.qg.AnyWork.dao.TestDao;
import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.TestException;
import com.qg.AnyWork.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by logan on 2017/7/10.
 */
@Service
public class TestService {

    @Autowired
    private TestDao testDao;
    @Autowired
    private OrganizationDao organizationDao;
    /***
     * 获取试题卷集合
     * @param organizationId
     * @return
     */
    public RequestResult<List<Testpaper>> getTestList(int organizationId){
        if (organizationDao.getById(organizationId)==null) throw new TestException("无效的组织");
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testDao.getTestByOrganizationId(organizationId));
    }


    /***
     * 获取练习卷集合
     * @param organizationId
     * @return
     */
    public RequestResult<List<Testpaper>> getPracticeList(int organizationId){
        if (organizationDao.getById(organizationId)==null) throw new TestException("无效的组织");
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testDao.getPracticeByOrganizationId(organizationId));
    }

    /***
     * 获取问题集合
     * @param testpaperId
     * @return
     */
    public RequestResult<List<Question>> getQuestion(int testpaperId){
        Testpaper testpaper = testDao.getTestpaperByTestpaperId(testpaperId);
        if (new Date().before(testpaper.getCreateTime())&&testpaper.getTestpaperType()==1) throw new TestException("考试未开始");
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testDao.getQuestionByTestpaperId(testpaperId));
    }


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

            //TODO  本道题是否在改试卷里边 没验证

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
                String SPLIT = "#";
                int index;
                int number = question.getOther();
                double fillingSocre = 0.0;
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

    /***
     * 提交试卷获取结果
     * @param studentPaper
     * @return
     */
    public RequestResult<StudentTestResult> submit(StudentPaper studentPaper){


        Testpaper testpaper = testDao.getTestpaperByTestpaperId(studentPaper.getTestpaperId());
        StudentTestResult studentTestResult = this.getResult(studentPaper);

        //如果为考试则必须将数据存起来
        if (testpaper.getTestpaperType()==1){
            if (testDao.isSubmit(testpaper.getTestpaperId(),studentPaper.getStudentId())>0)throw new TestException("已经提交过试卷了，无法再次提交");
            if (new Date().after(testpaper.getEndingTime()))throw new TestException("考试已经结束");
            testDao.addTestResult(studentTestResult);
            List<StudentAnswerAnalysis> studentAnswerAnalysis = studentTestResult.getStudentAnswerAnalysis();
            for (StudentAnswerAnalysis s : studentAnswerAnalysis){
                testDao.addStudentAnswer(s);
            }
        }

        return new RequestResult<StudentTestResult>(StatEnum.SUBMIT_TEST_SUCCESS,studentTestResult);
    }
}
