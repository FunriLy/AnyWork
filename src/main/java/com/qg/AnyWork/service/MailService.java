package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.RedisDao;
import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.MailSendException;
import com.qg.AnyWork.exception.user.UserNotExitException;
import com.qg.AnyWork.model.User;
import com.qg.AnyWork.utils.Encryption;
import com.qg.AnyWork.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by FunriLy on 2017/8/20.
 * From small beginnings comes great things.
 */
@Service
public class MailService {

    private static final Random random = new Random();

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private RedisDao redisDao;

    public RequestResult<?> sendPsaawordMail(String email) {
        User user = userDao.selectByEmail(email);
        if (user == null) {
            throw new UserNotExitException("不存在的用户！");
        }
        mailUtil.send(email, user.getUserName(), 2);
        return new RequestResult<Object>(StatEnum.MAIL_SEND_SUCCESS);
    }

    public RequestResult<Integer> sendRegisterMail(User user) throws MailSendException {
        String email = user.getEmail();
        mailUtil.send(email, user.getUserName(), 1);    // 发送验证邮件
        redisDao.addUserMessage(email, user);                 // 将资料存入缓存
        return new RequestResult<Integer>(StatEnum.MAIL_SEND_SUCCESS);
    }


    public String resetPassword(String email){
        //随机一个六位数密码
        String password = String.valueOf((random.nextInt(900000) + 100000));
        //将密码存入数据库
        User user = userDao.selectByEmail(email);
        if (user == null){
            throw new UserNotExitException("不存在的用户！");
        }
        //密码加密并存入数据库
        user.setPassword(Encryption.getMD5(password));
        userDao.updateUser(user);
        return password;
    }
}
