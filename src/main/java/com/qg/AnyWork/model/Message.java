package com.qg.AnyWork.model;

import com.qg.AnyWork.utils.CommonDateUtil;

import java.util.Date;

/**
 * 系统通知实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Message {

    // TODO: 2017/7/10 需要确定有哪些类型的消息

    private int id;             // 通知id
    private int sendId;         // 发送者id
    private int receiveId;      // 接收者id
    private String content;     // 主要内容
    private int type;           // 类型
    private int flag;           // 状态标志
    private Date sendTime;      // 发送时间

    public Message(){
        this.sendTime = CommonDateUtil.getNowDate();
    }

    public Message(int sendId, int receiveId, int type){
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.type = type;
    }

    public Message(int sendId, int receiveId, String content, int type) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.content = content;
        this.type = type;
        this.sendTime = CommonDateUtil.getNowDate();
    }

    // get & set 方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
