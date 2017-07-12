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
public interface QuuestionDao {

    /**
     * 插入题目
     * @param question
     * @return
     */
    int insertQuestion(@Param("question") Question question);

    /**
     * 根据试卷id删除所有的题目
     * @param textpaperId
     * @return
     */
    int deleteQuestion(@Param("textpaperId") int textpaperId);

    /**
     * 根据问题id查找题目
     * @param questionId
     * @return
     */
    Question selectOne(@Param("questionId") int questionId);

    /**
     * 根据试卷号获得一套试卷
     * @param textpaperId
     * @return
     */
    List<Question> selectAll(@Param("textpaperId") int textpaperId);
}
