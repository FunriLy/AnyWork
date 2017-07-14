package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface QuestionDao {

    /**
     * 插入题目
     * @param question
     * @return
     */
    int insertQuestion(@Param("question") Question question);

    /**
     * 批量插入问题
     * @param questlist
     * @return
     */
    int insertAllQuestion(List<Question> questlist);

    /**
     * 根据试卷id删除所有的题目
     * @param testpaperId
     * @return
     */
    int deleteQuestion(@Param("testpaperId") int testpaperId);
}
