package com.example.blog2.controller.web;

import com.example.blog2.constant.ConstantUser;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.exception.UserNotFoundException;
import com.example.blog2.exception.UserPasswordErrorException;
import com.example.blog2.exception.UserExistsNickNameException;
import com.example.blog2.exception.UserExistsUserNameException;
import com.example.blog2.service.UserService;
import com.example.blog2.utils.R;
import com.example.blog2.vo.UserRecVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxp
 * @date 2023/1/27 14:07
 */

@RestController
@RequestMapping("user")
public class UserWebController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody UserRecVo user) {
        try {
            return R.ok("登陆成功").put("data", userService.login(user));
        } catch (UserNotFoundException e) {
            return R.error(ConstantUser.LOGIN_FAIL_NOT_FOUND);
        } catch (UserPasswordErrorException e) {
            return R.error(ConstantUser.LOGIN_FAIL_ERROR);
        }
    }

    @PostMapping("/register")
    public R register(@RequestBody UserRecVo user) {
        try {
            return R.ok().put("data", userService.register(user));
        } catch (UserExistsUserNameException e) {
            return R.error(ConstantUser.REGISTER_FAIL_EXISTS_USER_NAME);
        } catch (UserExistsNickNameException e) {
            return R.error(ConstantUser.REGISTER_FAIL_EXISTS_NICK_NAME);
        }
    }
}
