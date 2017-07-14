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
    DEFAULT_WRONG(0,"其他错误"),
    CAUSE_TROUBLE(0,"你不要搞事"),
    ERROR_PARAM(0, "输入参数有误"),
    REQUEST_ERROR(0,"请求信息异常"),
    /**
     * 注册板块
     */
    REGISTER_SUCESS(1, "用户注册成功"),
    REGISTER_EMPTY_USER(0,"空用户对象"),
    REGISTER_FAMMTER_FAULT(0,"注册信息格式错误"),
    REGISTER_ALREADY_EXIST(0,"已存在的用户"),

    /**
     * 登录板块
     */
    LOGIN_SUCCESS(1, "用户登录成功"),
    LOGIN_NOT_EXIT_USER(0,"不存在的用户"),
    LOGIN_USER_MISMATCH(0,"用户名或密码错误"),
    USER_NOT_LOGIN(0, "用户还未登录"),
    USER_LOGIN_OUT(1, "用戶退出登录"),

    /**
     * 更新用户信息板块
     */
    INFORMATION_CHANGE_SUCCESS(1, "用户更改信息成功"),
    FROMATTER_WARNING(0, "格式有误"),
    INFORMATION_GET_MYSELF(1, "获取个人信息"),


    /**
     * 文件上传类型板块
     */
    FILE_UPLOAD_SUCCESS(1, "文件上传成功"),
    FILE_UPLOAD_FAIL(0, "文件上传失败"),
    FILE_FORMAT_ERROR(0, "文件格式错误"),
    PICTURE_UPLOAD_SUCCESS(1, "头像上传成功"),
    FILE_READ_SUCCESS(1, "文件读取成功"),
    FILE_READ_FAIL(0, "文件读取失败"),
    FILE_NOT_EXIT(0, "文件不存在"),


    /**
     * 试卷、练习板块
     */
    TEST_RELEASE_SUCESS(1, "发布试卷/练习成功"),
    TEST_RELEASE_FAIL(0, "发布试卷/练习失败"),
    NOT_HAVE_POWER(0, "没有相应的权限"),

    /**
     * 组织板块
     */
    ORGAN_SEARCH_SUCCESS(1, "搜索组织成功"),
    ORGAN_SEARCH_FAIL(0, "搜索组织失败"),
    ORGAN_JOIN_SUCCESS(1, "加入组织成功"),
    ORGAN_JOIN_FAIL(0, "加入组织失败"),

    /***
     * 做题模块
     */
    GET_TEST_SUCCESS(1, "获取成功"),
    GET_TEST_FAIL(0, "获取失败" ),
    SUBMIT_TEST_SUCCESS(1, "提交成功" ),
    SUBMIT_TEST_FAIL(0, "提交失败" ),
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
