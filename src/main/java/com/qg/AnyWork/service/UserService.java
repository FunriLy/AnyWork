package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.user.EmptyUserException;
import com.qg.AnyWork.exception.user.FormatterFaultException;
import com.qg.AnyWork.exception.user.UserLoginFailException;
import com.qg.AnyWork.exception.user.UserNotExitException;
import com.qg.AnyWork.model.User;
import com.qg.AnyWork.utils.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户注册
     * @param user
     * @return
     */
    public RequestResult<Integer> register(User user){
        if (null == user){
            throw new EmptyUserException("空用户对象");
        }
        else if(
                !user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                        !user.getUserName().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}") ||
                        !user.getPassword().matches("\\w{6,15}")
                ) {
            throw new FormatterFaultException("注册信息格式错误");
        }else {
            //格式正确，加密密码并存入数据库
            user.setPassword(Encryption.getMD5(user.getPassword()));
            userDao.insertUser(user);
            int userid = user.getUserId();
            return new RequestResult<Integer>(StatEnum.REGISTER_SUCESS, userid);
        }
    }

    /**
     * 用户登录
     * @param email
     * @param password
     * @return
     */
    public RequestResult<User> login(String email, String password){
        if (email == null || password == null){
            throw new FormatterFaultException("空对象");
        }

        User user = userDao.selectByEmail(email);
        if (user == null){
            throw new UserNotExitException("不存在的用户");
        } else if (!user.getPassword().equals(Encryption.getMD5(password))){
            throw new UserLoginFailException("错误的用户名或密码");
        } else {
            //登录成功
            // TODO: 2017/7/10 用户登录成功，检索出所有的组织
            user.setPassword("");
            return new RequestResult<User>(StatEnum.LOGIN_SUCCESS, user);
        }
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public RequestResult<User> updateUser(User user){
        if (user == null){
            throw new FormatterFaultException("空用户对象");
        }
        else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                        !user.getUserName().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}") //TODO
                ) {
            throw new FormatterFaultException("修改信息格式错误");
        } else {
            //置空密码
            user.setPassword("");
            userDao.updateUser(user);
            //查找新的用户实体
            User realUser = userDao.selectById(user.getUserId());
            user.setPassword("");
            return new RequestResult<User>(StatEnum.INFORMATION_CHANGE_SUCCESS, realUser);
        }
    }

    public RequestResult<User> passwordChange(User user){
        if (user == null){
            throw new FormatterFaultException("空用户对象");
        }
        else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                !user.getPassword().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}") //TODO
                ) {
            throw new FormatterFaultException("修改信息格式错误");
        } else {
           User r_user = userDao.selectByEmail(user.getEmail());
            //加密
            r_user.setPassword(Encryption.getMD5(user.getPassword()));
            userDao.updateUser(r_user);
            r_user.setPassword("");
            return new RequestResult<User>(StatEnum.INFORMATION_CHANGE_SUCCESS, null);
        }
    }
}
