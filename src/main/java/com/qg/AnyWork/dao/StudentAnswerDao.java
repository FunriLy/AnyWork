package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/10.
 * 对学生提交的试题答案进行数据库操作
 */
@Mapper
@Repository
public interface StudentAnswerDao {

    /***
     * 根据试卷号获取试题集
     * @param textpaperId 试卷号id
     * @return List<Question> 问题的列表
     */
    List<Question> getQuestionByTestId(@Param("textpaperId") int textpaperId);

    /***
     * 根据试题号获取试题
     * @param questionId 问题id
     * @return 问题对象
     */
    Question getQuestionById(@Param("questionId") int questionId);



}
