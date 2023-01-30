package com.example.blog2.exception;

/**
 * @author mxp
 * @date 2023/1/30 14:25
 */
public class UserStatusException extends RuntimeException {

    public UserStatusException() {
        super("用户登陆状态异常！！！");
    }
}
