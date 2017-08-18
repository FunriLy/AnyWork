package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 * 对于章节的数据库操作
 */
@Mapper
@Repository
public interface ChapterDao {
    /***
     * 根据组织id获取章节列表
     * @param organizationId 组织id
     * @return List<Chapter> 返回章节列表
     */
    List<Chapter> getByOrganizationId(@Param("organizationId") int organizationId);

    /***
     * 增加章节
     * @param chapter
     * @return
     */
    int addChapter(@Param("chapter") Chapter chapter);

    /***
     * 删除章节
     * @param chapterId
     * @return
     */
    int deleteChapter(@Param("chapterId") int chapterId);

    /***
     * 更新章节
     * @param chapter
     * @return
     */
    int updateChapter(@Param("chapter") Chapter chapter);
}
