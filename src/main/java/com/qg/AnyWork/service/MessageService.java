package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.MessageDao;
import com.qg.AnyWork.dao.OrganizationDao;
import com.qg.AnyWork.dao.UserDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.ValcodeWrongException;
import com.qg.AnyWork.exception.testpaper.NotPowerException;
import com.qg.AnyWork.exception.user.UserException;
import com.qg.AnyWork.model.Message;
import com.qg.AnyWork.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息实体 Service 层
 * Created by FunriLy on 2017/9/25.
 * From small beginnings comes great things.
 */
@Service
public class MessageService {

    // 每页数据条数
    private static final int MESSAGE_NUMBER = 10;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private UserDao userDao;

    /**
     * 获取用户收到的消息
     * @param userId
     * @param page
     * @param userName
     * @return
     */
    public RequestResult<List<Message>> getReceiveMessage(int userId, int page, String userName){
        if (page < 0){
            throw new ValcodeWrongException("页面值非法，小于0");
        }
        int start = page * MESSAGE_NUMBER;
        int end = start + 10;
        // 获得 MESSAGE_NUMBER 条记录
        List<Message> messageList = messageDao.getReceiveMessageList(userId, start, MESSAGE_NUMBER);
        List<Message> list = new ArrayList<>();
        for (Message message : messageList) {
            try {
                list.add(replaceMessageName(userId, userName, message));
            } catch (Exception e){
                // 不做处理，跳过该条消息
                logger.warn("用户接收消息获取未知异常：" + e.getMessage());
            }
        }
        return new RequestResult<List<Message>>(StatEnum.MESSAGE_LIST, list);
    }

    /**
     * 获取用户发送的消息
     * @param userId
     * @param organId
     * @param page
     * @param userName
     * @return
     */
    public RequestResult<List<Message>> getSendMessage(int userId, int organId, int page, String userName){
        Organization oragn = organizationDao.getById(organId);
        if (oragn.getTeacherId() != userId){    // 用户不是组织的创建者
            throw  new NotPowerException("用户没有相应的权限来查看消息！");
        }
        if (page < 0){
            throw new ValcodeWrongException("页面值非法，小于0");
        }
        int start = page * MESSAGE_NUMBER;
        int end = start + 10;
        List<Message> messageList = messageDao.getSendMessageList(userId, organId, start, end);
        List<Message> list = new ArrayList<>();
        for (Message message : messageList) {
            try {
                list.add(replaceMessageName(userId, userName, message));
            }catch (Exception e){
                logger.warn("用户发送消息获取未知异常：" + e.getMessage());
            }
        }
        return new RequestResult<List<Message>>(StatEnum.MESSAGE_LIST, list);
    }

    /**
     * 将用户消息做用户名与用户id转换处理
     * @param userId
     * @param username
     * @param message
     * @return
     */
    private Message replaceMessageName(int userId, String username, Message message) {
        String sendName, receiveName;
        try {
            if (userId != message.getSendId()) { // user 不是 发送者
                sendName = userDao.selectById(message.getSendId()).getUserName();
                receiveName = username;
            } else {
                sendName = username;
                receiveName = userDao.selectById(message.getReceiveId()).getUserName();
            }
        } catch (NullPointerException e) {
            throw new UserException("不存在的用户！");
        }
        String content = message.getContent();
        content.replaceAll(String.valueOf(message.getSendId()), sendName);
        content.replaceAll(String.valueOf(message.getReceiveId()), receiveName);
        message.setContent(content);
        return message;
    }
}
