package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.ChapterDao;
import com.qg.AnyWork.dao.OrganizationDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.exception.OrganizationException;
import com.qg.AnyWork.model.Chapter;
import com.qg.AnyWork.model.Organization;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 */
@Service
public class ChapterService {
    @Autowired
    private ChapterDao chapterDao;
    @Autowired
    private OrganizationDao organizationDao;

    /***
     * 获取章节列表
     * @param organizationId
     * @return
     */
    public RequestResult<List<Chapter>> getByOrganizationId(int organizationId){
        if (organizationDao.getById(organizationId)==null) throw new OrganizationException("组织id不存在");
        return new RequestResult<List<Chapter>>(1,"获取成功",chapterDao.getByOrganizationId(organizationId));
    }

    /***
     * 添加章节
     * @param chapter
     * @return
     */
    public RequestResult addChapter(Chapter chapter) {
        if (organizationDao.getById(chapter.getOrganizationId()) == null) throw new OrganizationException("组织id不存在");
        if (chapter.getChapterName().length() > 10) throw new OrganizationException("章节名过长");
        chapterDao.addChapter(chapter);
        return new RequestResult(1, "添加成功",chapter);
    }

    /***
     * 删除章节
     * @param chapterId
     * @return
     */
    public RequestResult deleteChapter(int chapterId) {
        chapterDao.deleteChapter(chapterId);
        return new RequestResult(1, "删除成功");
    }


}
