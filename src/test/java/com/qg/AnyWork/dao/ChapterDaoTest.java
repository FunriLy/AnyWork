package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Chapter;
import com.qg.AnyWork.model.Organization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by logan on 2017/7/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChapterDaoTest {
    @Resource
    ChapterDao chapterDao;

    @Test
    public void getByOrganizationId() throws Exception {
        List<Chapter> chapters = chapterDao.getByOrganizationId(1);
//        System.out.println(chapters.get(1));
        int i = chapters.size();
       while (i!=0){
        System.out.println(chapters.get(--i));
       }
    }

}