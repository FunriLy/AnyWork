package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Textpaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface TextpaperDao {

    /**
     * 插入一张试卷简介
     * @param textpaper
     * @return
     */
    int insertTextpaper(@Param("textpaper")Textpaper textpaper);

    int updateTextpaper(@Param("textpaper")Textpaper textpaper);

    int deleteTextpaper(@Param("textpaperId")Integer textpaperId);

    Textpaper selectOne(@Param("textpaperId")Integer textpaperId);
}
