package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.user.UserNotExitException;
import com.qg.AnyWork.model.User;
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

    public RequestResult<?> sendMail(String email) {
        User user = userDao.selectByEmail(email);
        if (user == null) {
            throw new UserNotExitException("不存在的用户！");
        }
        mailUtil.send(email, user.getUserName());
        return new RequestResult<Object>(StatEnum.MAIL_SEND_SUCCESS);
    }


    public String resetPassword(String email){
        //随机一个六位数密码
        String password = String.valueOf((random.nextInt(900000) + 100000));
        //将密码存入数据库
        User user = userDao.selectByEmail(email);
        if (user == null){
            throw new UserNotExitException("不存在的用户！");
        }
        user.setPassword(password);
        userDao.updateUser(user);
        return password;
    }
}
