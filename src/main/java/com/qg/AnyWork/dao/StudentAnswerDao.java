package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/10.
 */
@Mapper
@Repository
public interface StudentAnswerDao {

    /***
     * 根据试卷号获取试题集
     * @param textpaperId
     * @return
     */
    List<Question> getQuestionByTestId(@Param("textpaperId") int textpaperId);

    /***
     * 根据试题号获取试题
     * @param questionId
     * @return
     */
    Question getQuestionById(@Param("questionId") int questionId);
}
