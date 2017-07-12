package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Service
public class TextpaperService {

    @Autowired
    private RedisDao redisDao;
}
