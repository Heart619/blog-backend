package com.example.blog2.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author mxp
 * @date 2023/1/29 20:40
 */
@Data
public class UserRecVo {

    private Long id;

    private String avatar;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private String nickname;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String username;
}
