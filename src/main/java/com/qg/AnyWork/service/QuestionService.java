package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.QuestionDao;
import com.qg.AnyWork.dao.RedisDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.question.ExcelReadException;
import com.qg.AnyWork.exception.question.RedisNotExitException;
import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FunriLy on 2017/7/13.
 * From small beginnings comes great things.
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private RedisDao redisDao;


    /**
     * 用户上传并读取文件
     * @param input
     * @param userId
     * @return
     */
    public RequestResult<List<Question>> addQuestionList(InputStream input, int userId){

        List<Question> list = null;
        try {
            list = ExcelUtil.getQuestionList(input);
        } catch (Exception e) {
            throw new ExcelReadException("Excel读取出错："+e.getMessage(), e);
        }

        if (null != list){
            //清空并存入redis
            redisDao.removeQuestionList(userId);
            redisDao.addQuestionList(userId, new ArrayList<>(list));
        }
        return new RequestResult<List<Question>>(StatEnum.FILE_READ_SUCCESS, list);
    }

    /**
     * 发布试卷/练习
     * @param userId
     * @param testpaperId
     * @return
     */
    public int addTestpaper(int userId, int testpaperId){
        List<Question> list = null;
        int socre = 0;
        //拿到题目缓存
        list = redisDao.getQuestionList(userId);
        if (null != list){
//            Iterator<Question> iterator = list.iterator();
//            while (iterator.hasNext()){
//                //设置试卷号
//                iterator.next().settestpaperId(testpaperId);
//            }

            // TODO: 2017/7/13 未解决事务回滚问题
            for (int i=0; i<list.size(); i++){
                list.get(i).setTestpaperId(testpaperId);    //更新试卷号
                questionDao.insertQuestion(list.get(i));    //插入数据库
                socre += list.get(i).getSocre();            //将总分加起来
            }

            // TODO: 2017/7/13 MyBatis 3.3.1 修复批量插入返回主键的问题，但在SpringBoot和MyBatis3.4.0的实验中出现字段找不到的问题
            // 暂时使用遍历
            //questionDao.insertAllQuestion(list);


        } else {
            throw new RedisNotExitException("未找到相应的缓存文件！");
        }

        return socre;

    }
}
