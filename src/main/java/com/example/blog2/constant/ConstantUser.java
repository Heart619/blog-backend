package com.example.blog2.constant;

/**
 * @author mxp
 * @date 2023/1/28 19:47
 */
public enum ConstantUser {

    /**
     * 登陆成功
     */
    LOGIN_SUCCESS(0, "登陆成功！"),
    LOGIN_FAIL_NOT_FOUND(10000, "用户不存在！"),
    LOGIN_FAIL_ERROR(10001, "用户名或密码错误！"),
    REGISTER_SUCCESS(0, "注册成功！"),
    REGISTER_FAIL_EXISTS_NICK_NAME(10002, "注册失败，昵称已存在！"),
    REGISTER_FAIL_EXISTS_USER_NAME(10003, "注册失败，用户名已存在！"),
    EXISTS_NICK_NAME(10002, "修改失败，昵称已存在！"),
    EXISTS_USER_NAME(10003, "修改失败，用户名已存在！");


    private final String msg;
    private final int code;

    ConstantUser(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
