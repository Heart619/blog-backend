package com.example.blog2.controller.web;

import com.example.blog2.constant.ConstantUser;
import com.example.blog2.exception.UserNotFoundException;
import com.example.blog2.exception.UserPasswordErrorException;
import com.example.blog2.exception.UserExistsNickNameException;
import com.example.blog2.exception.UserExistsUserNameException;
import com.example.blog2.service.UserService;
import com.example.blog2.utils.R;
import com.example.blog2.vo.UserRecVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mxp
 * @date 2023/1/27 14:07
 */

@RestController
@RequestMapping("/user")
public class UserWebController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody UserRecVo user) throws Exception {
        try {
            return R.ok("登陆成功").put("data", userService.login(user));
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                return R.error(ConstantUser.LOGIN_FAIL_NOT_FOUND);
            } else if (e instanceof UserPasswordErrorException) {
                return R.error(ConstantUser.LOGIN_FAIL_ERROR);
            } else {
                throw e;
            }
        }
    }

    @GetMapping("/refresh")
    public R refreshToken() {
        String token = userService.refreshToken();
        return R.ok().put("data", token);
    }

    @PostMapping("/register")
    public R register(@RequestBody UserRecVo user) throws Exception {
        try {
            return R.ok().put("data", userService.register(user));
        } catch (Exception e) {
            if (e instanceof UserExistsUserNameException) {
                return R.error(ConstantUser.REGISTER_FAIL_EXISTS_USER_NAME);
            } else if (e instanceof UserExistsNickNameException) {
                return R.error(ConstantUser.REGISTER_FAIL_EXISTS_NICK_NAME);
            } else {
                throw e;
            }
        }
    }
}
