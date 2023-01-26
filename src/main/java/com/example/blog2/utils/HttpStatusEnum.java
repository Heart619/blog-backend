package com.example.blog2.utils;

/**
 * @author mxp
 * @date 2023/1/25 10:18
 */
enum HttpStatusEnum {
    /**
     * 成功
     */
    OK(200, "成功"),
    ERROR(201, "失败"),
    LOGIN_ERROR(202, "用户名或密码错误"),
    ACCESS_ERROR(203, "权限不足"),
    REMOTE_ERROR(204, "远程调用失败"),
    REP_ERROR(205, "重复操作");


    private final int code;
    private final String msg;

    HttpStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
