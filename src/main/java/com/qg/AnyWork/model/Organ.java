package com.qg.AnyWork.model;

/**
 * 组织实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public class Organ {

    private int organId;            //ID
    private int teacherId;          //教师ID
    private String organName;       //组织名
    private String description;     //描述
    private long token;             //口令
    private int count;              //组织人数
    private int isJoin;             //判断字段，标志学生是否是该组织成员

    //TODO:考虑老师登录后是否要带上学生列表或者试卷列表

    public Organ(){}

    //get & set

    public int getOrganId() {
        return organId;
    }

    public void setOrganId(int organId) {
        this.organId = organId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //toString

    @Override
    public String toString() {
        return "Organ{" +
                "organId=" + organId +
                ", teacherId=" + teacherId +
                ", organName='" + organName + '\'' +
                ", description='" + description + '\'' +
                ", token=" + token +
                ", count=" + count +
                '}';
    }
}
