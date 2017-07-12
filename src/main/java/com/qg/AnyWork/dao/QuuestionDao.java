package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    int deleteQuestion();
}
