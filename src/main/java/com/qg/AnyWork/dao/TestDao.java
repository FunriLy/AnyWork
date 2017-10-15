package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 * 操作学生的答卷
 */
@Mapper
@Repository
public interface TestDao {

    /***
     * 获取组织下的考试列表
     * @param organizationId 组织id
     * @return List<Testpaper> 返回该组织的考试列表
     */
    List<Testpaper> getTestByOrganizationId(@Param("organizationId") int organizationId);

    /***
     * 获取组织下的练习列表
     * @param organizationId 组织id
     * @return List<Testpaper> 返回该组织的练习列表
     */
    List<Testpaper> getPracticeByOrganizationId(@Param("organizationId") int organizationId);

    /***
     * 根据组织id和章id获取练习
     * @param organizationId 组织id
     * @param chapterId 章节id
     * @return List<Testpaper> 返回该组织某章节下的练习列表
     * TODO 名字未查出
     */
    List<Testpaper> getPracticeByOCId(@Param("organizationId") int organizationId,@Param("chapterId") int chapterId);

    List<CheckResult> getUserPracticeByOrganizationId(@Param("userId") int userId,@Param("organizationId") int organizaitonId);

    List<Testpaper> getMyPractice(@Param("userId") int userId);
    List<Testpaper> getMyTest(@Param("userId") int userId);

    /***
     * 根据试卷id获取试卷信息
     * @param testpaperId 试卷id
     * @return Testpaper 返回问卷对象
     */
    Testpaper getTestpaperByTestpaperId(@Param("testpaperId") int testpaperId);


    /***
     * 根据试卷id获取试题集合
     * @param testpaperId 试卷id
     * @return List<Question> 返回答题集合
     */
    List<Question> getQuestionByTestpaperId(@Param("testpaperId") int testpaperId);

    /***
     * 根据试题号获取试题
     * @param questionId 问题id
     * @return Question问题对象
     */
    Question getQuestionById(@Param("questionId") int questionId);

    /***
     * 查看用户是否提交过试卷了
     * @param testpaperId 试卷id
     * @param userId 用户id
     * @return 1为提交过了 0为未提交
     */
    int isSubmit(@Param("testpaperId") int testpaperId,@Param("userId") int userId);

    /***
     * 添加考试结果
     * @param testResult 试卷结果类
     * @return 1为添加成功 0为失败
     */
    int addTestResult(@Param("testResult") StudentTestResult testResult);

    /***
     * 更新 考试结果
     * @param testResult
     * @return
     */
    int updateTestResult(@Param("testResult") StudentTestResult testResult);

    /***
     * 添加学生的答案
     * @param studentAnswerAnalysis 学生答案类
     * @return 1为添加成功 0为添加失败
     */
    int addStudentAnswer(@Param("studentAnswerAnalysis") StudentAnswerAnalysis studentAnswerAnalysis);

    /***
     * 更新学生的答案
     * @param studentAnswerAnalysis
     * @return
     */
    int updateStudentAnswer(@Param("studentAnswerAnalysis") StudentAnswerAnalysis studentAnswerAnalysis);

    /**
     * 插入一张试卷，返回主键
     * @param testpaper 试卷
     * @return 1为添加成功 0为添加失败
     */
    int addTestpaper(@Param("testpaper") Testpaper testpaper);

    /**
     * 删除一张试卷
     * @param testpaperId
     * @return
     */
    int deleteTestpaper(@Param("testpaperId") int testpaperId);

    /**
     * 更新一张试卷/练习的分数
     * @param testpaperId 试卷
     * @param score 分数
     * @return 1为添加成功 0为添加失败
     */
    int updateSocreOfTestpaper(@Param("testpaperId") int testpaperId, @Param("score") int score);

    /***
     * 获取 学生在组织下完成过的试卷概要列表
     * @param organizationId
     * @param userId
     * @return
     */
    List<CheckResult> getCheckResultByUser(@Param("organizationId") int organizationId, @Param("userId") int userId);

    /***
     * 获得考试结果
     * @param testpaperId
     * @param userId
     * @return
     */
    StudentTestResult getTestResult(@Param("testpaperId") int testpaperId, @Param("userId") int userId);

    /***
     *
     * 获得详细学生试卷详情
     * @param testpaperId
     * @param userId
     * @return
     */
    List<StudentAnswerAnalysis> getStudentAnswer(@Param("testpaperId") int testpaperId, @Param("userId") int userId);

    /**
     * 获取某套题组织内成员的完成情况
     * @param organizationId
     * @param testpaperId
     * @return
     */
    List<CheckResult> getCheckResultByTestpaperId(@Param("organizationId") int organizationId, @Param("testpaperId") int testpaperId);

    /***
     * 添加教师查阅及评卷信息
     * @param checkResult
     * @return
     */
    int addCheckResult (@Param("checkResult")CheckResult checkResult);


    /***
     * 更新学生完成的每道题的分数
     * @param socre
     * @return
     */
    int updateStudentAnswerSocre(@Param("socre") double socre,@Param("studentId") int studentId,@Param("questionId") int questionId);

    /***
     * 更新批改详情
     * @param subject
     * @param testpaperId
     * @param studentId
     * @return
     */
    int updateCheckResult(@Param("subject") double subject,@Param("object") double object, @Param("testpaperId") int testpaperId,@Param("studentId") int studentId);

}
