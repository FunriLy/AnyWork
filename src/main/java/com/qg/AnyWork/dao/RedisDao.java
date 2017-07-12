package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis 数据库缓存操作
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Repository
public class RedisDao {

    // TODO: 2017/7/12 Redis操作，还未试验！！！

    @Autowired
    private RedisTemplate redisTemplate;

    public void addQuestionList(int userId, ArrayList<Question> list){
        String userStr = String.valueOf(userId);
        synchronized (list){
            for (Question question : list){
                if (redisTemplate.opsForList().size(userStr) >= 100) {
                    redisTemplate.opsForList().rightPop(userStr);
                }
                redisTemplate.opsForList().leftPush(userStr, question);
            }
        }
    }

    public List getQuestionList(int userId){
        return redisTemplate.opsForList().range(String.valueOf(userId), 0, -1);
    }
}
