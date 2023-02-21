package com.example.blog2.vo;

import lombok.Data;

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

    private Integer type;
}
