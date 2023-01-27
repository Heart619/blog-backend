package com.example.blog2.controller.web;

import com.example.blog2.entity.UserEntity;
import com.example.blog2.service.UserService;
import com.example.blog2.utils.R;
import com.example.blog2.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mxp
 * @date 2023/1/27 14:07
 */

@Slf4j
@RestController
@RequestMapping("user")
public class UserWebController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody UserEntity user) {
        try {
            UserLoginVo res = userService.login(user);
            if (res == null) {
                return R.error("用户名或密码错误");
            }
            log.info("用户 {} 登陆，登陆地点：{}-{}", res.getUser().getNickname(), res.getUser().getLoginProvince(), res.getUser().getLoginCity());
            return R.ok("登陆成功").put("data", res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public R register(@RequestBody UserEntity user) {
        UserLoginVo vo = null;
        try {
            vo = userService.register(user);
            return R.ok().put("data", vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error();
        }
    }
}
