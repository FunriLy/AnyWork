package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 */
@Mapper
@Repository
public interface ChapterDao {
    /***
     * 根据组织id获取章节列表
     * @param organizationId
     * @return
     */
    List<Chapter> getByOrganizationId(@Param("organizationId") int organizationId);
}
