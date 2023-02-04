package com.example.blog2.exception;

import lombok.Data;

/**
 * @author mxp
 * @date 2023/2/4 20:54
 */
@Data
public class RefreshExpiresException extends RuntimeException {
    private Integer code;

    public RefreshExpiresException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
