package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统通知实体操作接口
 * Created by FunriLy on 2017/9/25.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface MessageDao {

    /**
     * 插入一条消息实体记录
     * @param message 消息实体
     * @return
     */
    int insertMessage(@Param("message") Message message);

    /**
     * 批量插入消息数据
     * @param list 消息列表
     * @return
     */
    int insertMessageList(@Param("list") List<Message> list);

    /**
     * 班级老师获取发过的通知
     * @param userId 教师id
     * @param classId 班级id
     * @param startNum 开始标记
     * @param sizeNum 条数
     * @return
     */
    List<Message> getSendMessageList(int userId, int classId, int startNum, int sizeNum);

    /**
     * 获得用户收到的消息
     * @param userid 用户
     * @param startNum 开始标记
     * @param sizeNum 条数
     * @return
     */
    List<Message> getReceiveMessageList(int userid, int startNum, int sizeNum);

    /**
     * 删除一条消息记录
     * @param messageId 消息id
     * @return
     */
    int deleteMessage(@Param("messageId") int messageId);

    /**
     * 根据消息id获得实体消息
     * @param messageId 消息id
     * @return
     */
    Message getMessageById(@Param("messageId") int messageId);
}
