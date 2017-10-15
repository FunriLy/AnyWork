package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.model.User;
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

    public void removeQuestionList(int userId){
        String userStr = String.valueOf(userId);
        while (redisTemplate.opsForList().size(userStr) > 0){
            // TODO: 2017/7/13 清除不彻底
            redisTemplate.opsForList().rightPop(userStr);
        }
    }

    public List getQuestionList(int userId){
        return redisTemplate.opsForList().range(String.valueOf(userId), 0, -1);
    }

    public void  addUserMessage(String email, User user) {
        synchronized (user) {
            removeUserMessage(email);   // 清除已经有的缓存信息
            redisTemplate.opsForList().leftPush(email, user);
        }
    }

    private void removeUserMessage (String email) {
        while (redisTemplate.opsForList().size(email) > 0) {
            redisTemplate.opsForList().rightPop(email);
        }
    }

    public User getUserMessage(String email) {
        User user = null;
        try {
            user = (User) redisTemplate.opsForList().rightPop(email);
        }catch (Exception e){
            System.out.println("Redis 获取用户信息缓存失败，" + email);
            e.printStackTrace();
        }
        if (user != null){
            removeUserMessage(email);   // 清除
        }
        return user;
    }
}
