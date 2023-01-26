package com.example.blog2.vo;

import com.example.blog2.entity.UserEntity;
import lombok.Data;

/**
 * @author mxp
 * @date 2023/1/26 11:09
 */
@Data
public class UserLoginVo {

    private UserEntity user;
    private String token;
}
