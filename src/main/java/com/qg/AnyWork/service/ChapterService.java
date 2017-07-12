package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.ChapterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by logan on 2017/7/11.
 */
@Service
public class ChapterService {
    @Autowired
    private ChapterDao chapterDao;
}
