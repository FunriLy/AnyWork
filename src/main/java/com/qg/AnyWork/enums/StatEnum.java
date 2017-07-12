package com.qg.AnyWork.enums;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
public enum StatEnum {

    /**
     * 通用板块
     */
    VALCODE_WRONG(0,"验证码错误"),
    DEFAULT_WRONG(-1,"其他错误"),
    CAUSE_TROUBLE(-2,"你不要搞事"),
    ERROR_PARAM(-3, "输入参数有误"),

    /**
     * 注册板块
     */
    REGISTER_SUCESS(1001, "用户注册成功"),
    REGISTER_EMPTY_USER(1002,"空用户对象"),
    REGISTER_FAMMTER_FAULT(1003,"注册信息格式错误"),
    REGISTER_ALREADY_EXIST(1004,"已存在的用户"),

    /**
     * 登录板块
     */
    LOGIN_SUCCESS(2001, "用户登录成功"),
    LOGIN_NOT_EXIT_USER(2002,"不存在的用户"),
    LOGIN_USER_MISMATCH(2003,"用户名或密码错误"),

    /**
     * 更新用户信息板块
     */
    INFORMATION_CHANGE_SUCCESS(3001, "用户更改信息成功"),


    /**
     * 文件上传类型板块
     */
    FILE_UPLOAD_SUCCESS(4001, "文件上传成功"),
    FILE_UPLOAD_FAIL(4002, "文件上传失败"),
    FILE_FORMAT_ERROR(4003, "文件格式错误"),


    /**
     * 组织板块
     */
    ORGAN_SEARCH_SUCCESS(10001, "搜索组织成功"),
    ORGAN_SEARCH_FAIL(10002, "搜索组织失败"),
    ORGAN_JOIN_SUCCESS(10003, "加入组织成功"),
    ORGAN_JOIN_FAIL(10004, "加入组织失败"),

    ;

    private  int state;
    private  String stateInfo;

    StatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
    public  static  StatEnum statOf(int index) {
        for (StatEnum state : values()) {
            if (state.getState() == index) {
                return  state;
            }
        }
        return  null;
    }
}
