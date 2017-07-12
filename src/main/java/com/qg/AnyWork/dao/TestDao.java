package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 */
@Mapper
@Repository
public interface TestDao {

    /***
     * 获取组织下的考试列表
     * @param organizationId
     * @return
     */
    List<Testpaper> getTestByOrganizationId(@Param("organizationId") int organizationId);

    /***
     * 获取组织下的练习列表
     * @param organizationId
     * @return
     */
    List<Testpaper> getPracticeByOrganizationId(@Param("organizationId") int organizationId);

    /***
     * 根据组织id和章id获取练习
     * @param organizationId
     * @param chapterId
     * @return
     * TODO 名字未查出
     */
    List<Testpaper> getPracticeByOCId(@Param("organizationId") int organizationId,@Param("chapterId") int chapterId);

    /***
     * 根据试卷id获取试题集合
     * @param testpaperId
     * @return
     */
    List<Question> getQuestionByTestpaperId(@Param("testpaperId") int testpaperId);

    /***
     * 根据试题号获取试题
     * @param questionId
     * @return
     */
    Question getQuestionById(@Param("questionId") int questionId);

    /***
     * 添加考试结果
     * @param testResult
     * @return
     */
    int addTestResult(@Param("testResult") StudentTestResult testResult);

    /***
     * 添加学生的答案
     * @param studentAnswerAnalysis
     * @return
     */
    int addStudentAnswer(@Param("studentAnswerAnalysis") StudentAnswerAnalysis studentAnswerAnalysis);
}
