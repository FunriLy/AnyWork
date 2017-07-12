package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Testpaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface TestpaperDao {

    /**
     * 插入一张试卷简介
     * @param Testpaper
     * @return
     */
    int insertTestpaper(@Param("Testpaper")Testpaper Testpaper);

    int updateTestpaper(@Param("Testpaper")Testpaper Testpaper);

    int deleteTestpaper(@Param("TestpaperId")Integer TestpaperId);

    Testpaper selectOne(@Param("TestpaperId")Integer TestpaperId);
}
