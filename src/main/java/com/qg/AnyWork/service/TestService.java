package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.OrganizationDao;
import com.qg.AnyWork.dao.TestDao;
import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.TestException;
import com.qg.AnyWork.model.*;
import org.apache.ibatis.annotations.Param;
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
    @Autowired
    private UserDao userDao;
    /***
     * 获取试题卷集合
     * @param organizationId
     * @return
     */
    public RequestResult<List<Testpaper>> getTestList(int organizationId,int userId){
        if (organizationDao.getById(organizationId)==null) throw new TestException("无效的组织");
        List<Testpaper> testpapers = testDao.getTestByOrganizationId(organizationId);
        List<Testpaper> studentpapers = testDao.getMyTest(userId);
        List<Testpaper> testpaperList = checkIfDo(testpapers,studentpapers);
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testpaperList);
    }


    /***
     * 获取练习卷集合
     * @param organizationId
     * @return
     */
    public RequestResult<List<Testpaper>> getPracticeList(int organizationId,int userId){
        if (organizationDao.getById(organizationId)==null) throw new TestException("无效的组织");
        List<Testpaper> practiceList = testDao.getPracticeByOrganizationId(organizationId);
        List<Testpaper> studentpapers = testDao.getMyPractice(userId);
        List<Testpaper> testpaperList = checkIfDo(practiceList,studentpapers);
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testpaperList);
    }

    /***
     * 检查是否有做
     * @param testpapers
     * @param studentpapers
     * @return
     */
    List<Testpaper> checkIfDo(List<Testpaper> testpapers,List<Testpaper> studentpapers){
        List<Testpaper> testpapersList = new ArrayList<>();
        for (Testpaper t : testpapers){
            int flag = 0;       //没做的flag
            for (Testpaper testpaper : studentpapers){
                if (t.getTestpaperId()==testpaper.getTestpaperId()) {
                    t.setChapterId(0);
                    flag = 1 ;
                }
            }
            if(flag == 0) t.setChapterId(-1);
            testpapersList.add(t);
        }

        return testpapersList;
    }

    /***
     * 根据章节id和组织id获取练习题
     * @param organizationId
     * @param chapterId
     * @return
     */
    public RequestResult<List<Testpaper>> getPracticeByOCId(int organizationId,int chapterId,int userId){
        if (organizationDao.getById(organizationId)==null) throw new TestException("无效的组织");
        List<Testpaper> practiceList = testDao.getPracticeByOCId(organizationId,chapterId);
        List<Testpaper> studentpapers = testDao.getMyPractice(userId);
        List<Testpaper> testpaperList = checkIfDo(practiceList,studentpapers);

        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testpaperList);
    }


    /***
     * 获取我做过的练习卷集合
     * @return
     */
    public RequestResult<List<Testpaper>> getMyPracticeList(int userId){

        List<Testpaper> testpapers = testDao.getMyPractice(userId);
        for (Testpaper t : testpapers){
            t.setChapterId(0);
        }
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testpapers);
    }

    /***
     * 获取某学生组织下做过的练习卷集合
     * @return
     */
    public RequestResult<List<CheckResult>> getPraceticeByOrganizationId(int userId, int organizationId){

        List<CheckResult> practice = testDao.getUserPracticeByOrganizationId(userId,organizationId);

        List<CheckResult> checkResults = new ArrayList<CheckResult>();
        List<Testpaper> testpapers = testDao.getPracticeByOrganizationId(organizationId);
        for (Testpaper t : testpapers){
            int flag = 0;
            for (CheckResult c : practice){
                if (t.getTestpaperId() == c.getTestpaper().getTestpaperId()) {
                    c.setIfAttend(1);
                    checkResults.add(c);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0 ){
                CheckResult checkResult = new CheckResult();
                checkResult.setIfAttend(0);
                checkResult.setObject(0);
                checkResult.setSubject(0);
                checkResult.setStudentId(userId);
                checkResult.setStudentName(userDao.selectById(userId).getUserName());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(t);
                checkResults.add(checkResult);
            }
        }

        return  new RequestResult<List<CheckResult>>(1,"获取成功",checkResults);

    }

    /***
     * 获取我做过的考试卷集合
     * @return
     */
    public RequestResult<List<Testpaper>> getMyTestList(int userId){
        List<Testpaper> testpapers = testDao.getMyTest(userId);
        for (Testpaper t : testpapers){
            t.setChapterId(0);
        }
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,testpapers);
    }

    /***
     * 获取问题集合
     * @param testpaperId
     * @return
     */
    public RequestResult<List<Question>> getQuestion(int testpaperId){
        Testpaper testpaper = testDao.getTestpaperByTestpaperId(testpaperId);
        if (new Date().before(testpaper.getCreateTime())&&testpaper.getTestpaperType()==1) throw new TestException("考试未开始");
        List<Question> questions = testDao.getQuestionByTestpaperId(testpaperId);
        for (Question question:questions){
            question.setKey(null);
        }
        return new RequestResult(StatEnum.GET_TEST_SUCCESS,questions);
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

        //学生写的答案列表
        List<StudentAnswer> studentAnswers = studentPaper.getStudentAnswer();
        //试题集合
        List<Question> questions = testDao.getQuestionByTestpaperId(studentPaper.getTestpaperId());

        for (Question question : questions) {
            int ifDo=0;
            for (StudentAnswer studentAnswer : studentAnswers) {
                if (studentAnswer.getQuestionId() == question.getQuestionId()) {
                    ifDo=1;
                    StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
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
                        String SPLIT = "∏";
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
                    studentAnswerAnalysis.setStudentId(studentPaper.getStudentId());
                    studentAnswerAnalysises.add(studentAnswerAnalysis);

                }

            }
            if (ifDo==0) {
                //没写该道题
                StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
                //分析题目类注入值
                studentAnswerAnalysis.setQuestion(question);
                studentAnswerAnalysis.setStudentAnswer("");
                studentAnswerAnalysis.setIsTrue(0);
                studentAnswerAnalysis.setSocre(0);
                studentAnswerAnalysis.setStudentId(studentPaper.getStudentId());
                studentAnswerAnalysises.add(studentAnswerAnalysis);
            }
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

        //是否已经做过该套题的标志
        boolean flag = testDao.isSubmit(testpaper.getTestpaperId(),studentPaper.getStudentId()) > 0;

        if (flag && testpaper.getTestpaperType()==1)throw new TestException("已经提交过试卷了，无法再次提交");

        StudentTestResult studentTestResult = this.getResult(studentPaper);

        //如果为考试则必须校验考试时间
        if (testpaper.getTestpaperType()==1){
            if (new Date().after(testpaper.getEndingTime()))throw new TestException("考试已经结束");
        }

        if (flag) {
            testDao.updateTestResult(studentTestResult);
        }
        else {
            testDao.addTestResult(studentTestResult);
            if (testpaper.getTestpaperType()==1) {
                CheckResult checkResult = new CheckResult();
                checkResult.setStudentId(studentPaper.getStudentId());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(testpaper);
                checkResult.setObject(studentTestResult.getSocre());
                checkResult.setSubject(0);
                testDao.addCheckResult(checkResult);
            }
        }
        List<StudentAnswerAnalysis> studentAnswerAnalysis = studentTestResult.getStudentAnswerAnalysis();
        if (!flag) {
            for (StudentAnswerAnalysis s : studentAnswerAnalysis) {
                testDao.addStudentAnswer(s);
            }
        }
        if (flag){
            for (StudentAnswerAnalysis s : studentAnswerAnalysis) {
                testDao.updateStudentAnswer(s);
            }
        }
        return new RequestResult<StudentTestResult>(StatEnum.SUBMIT_TEST_SUCCESS,studentTestResult);
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

    /***
     * 获取学生试卷详情
     * @param testpaperId
     * @param userId
     * @return
     */
    public RequestResult<StudentTestResult> getDetail(int testpaperId , int userId){
        StudentTestResult studentTestResult = testDao.getTestResult(testpaperId,userId);
        if (studentTestResult==null) return  new RequestResult(0,"没有此记录");
        List<StudentAnswerAnalysis> studentAnswerAnalysises = testDao.getStudentAnswer(testpaperId,userId);
        studentTestResult.setStudentAnswerAnalysis(studentAnswerAnalysises);
        return  new RequestResult<StudentTestResult>(1,"成功",studentTestResult);
    }

    /***
     * 获取组织下某学生的考试列表
     * @param organizationId
     * @param userId
     * @return
     */
    public RequestResult<List<CheckResult>> getCheckResultByUser(int organizationId, int userId){
        List<CheckResult> checkResultsByUser = testDao.getCheckResultByUser(organizationId,userId);
        List<CheckResult> checkResults = new ArrayList<CheckResult>();
        List<Testpaper> testpapers = testDao.getTestByOrganizationId(organizationId);
        for (Testpaper t : testpapers){
            int flag = 0;
            for (CheckResult c : checkResultsByUser){
                if (t.getTestpaperId() == c.getTestpaper().getTestpaperId()) {
                    c.setIfAttend(1);
                    checkResults.add(c);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0 ){
                CheckResult checkResult = new CheckResult();
                checkResult.setIfAttend(0);
                checkResult.setObject(0);
                checkResult.setSubject(0);
                checkResult.setStudentId(userId);
                checkResult.setStudentName(userDao.selectById(userId).getUserName());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(t);
                checkResults.add(checkResult);
            }
        }

        return  new RequestResult<List<CheckResult>>(1,"获取成功",checkResults);
    }

    /***
     *获取某套题组织内成员的完成情况
     * @param organizationId
     * @param testpaperId
     * @return
     */
    public RequestResult<List<CheckResult>> getCheckResultByTestpaperId(int organizationId, int testpaperId){
        List<CheckResult> checkResultsByTest = testDao.getCheckResultByTestpaperId(organizationId,testpaperId);
       List<CheckResult> checkResults = new ArrayList<CheckResult>();
        List<User> users = organizationDao.getOrganizationPeople(organizationId);
        for(User u : users){
            int flag = 0;
            for (CheckResult c : checkResultsByTest) {
                if (c.getStudentId() == u.getUserId()) {
                    c.setIfAttend(1);
                    checkResults.add(c);
                    flag = 1;
                    break;
                }
            }
                if (flag == 0 ){
                    CheckResult checkResult = new CheckResult();
                    checkResult.setIfAttend(0);
                    checkResult.setObject(0);
                    checkResult.setSubject(0);
                    checkResult.setStudentId(u.getUserId());
                    checkResult.setStudentName(u.getUserName());
                    checkResult.setIfCheck(0);
                    checkResult.setTestpaper(testDao.getTestpaperByTestpaperId(testpaperId));
                    checkResults.add(checkResult);
                }
        }


        return  new RequestResult<List<CheckResult>>(1,"获取成功",checkResults);
    }

    /***
     * 更新相关分数
     * @param teacherSubmit
     */
    public void updateStudentTest(TeacherSubmit teacherSubmit){
        List<TeacherJudge> teacherJudges = teacherSubmit.getTeacherJudge();
        double subject = 0;
        double object = 0;
        for (TeacherJudge t : teacherJudges){
            int type =testDao.getQuestionById(t.getQuestionId()).getType();
            if (type==4 || type==6 || type==5) subject+=t.getSocre();
            else object+=t.getSocre();
            //更新studentAnswer
            testDao.updateStudentAnswerSocre(t.getSocre(),teacherSubmit.getStudentId(),t.getQuestionId());
        }
        //更新checkResult
        testDao.updateCheckResult(subject,object,teacherSubmit.getTestpaperId(),teacherSubmit.getStudentId());

        //更新testResult
        StudentTestResult s = new StudentTestResult();
        s.setSocre(object);
        s.setStudentId(teacherSubmit.getStudentId());
        s.setTestpaperId(teacherSubmit.getTestpaperId());
        testDao.updateTestResult(s);

        StudentTestResult studentTestResult = testDao.getTestResult(teacherSubmit.getTestpaperId(),teacherSubmit.getStudentId());
        studentTestResult.setSocre(studentTestResult.getSocre()+subject);
        testDao.updateTestResult(studentTestResult);
    }


}
