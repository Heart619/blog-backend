package com.example.blog2.controller.web;

import com.example.blog2.entity.UserEntity;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.po.User;
import com.example.blog2.service.old.UserService;
import com.example.blog2.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/6 15:26
 */
@Slf4j
//@RestController
public class UserController {

//    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String, User> para) {
        User u = para.get("user");
        User user = userService.checkUser(u.getUsername(), u.getPassword());
        if (user != null) {
            String token = TokenUtil.sign(new UserEntity());
            Map<String, Object> info = new HashMap<>(2);
            user.setLoginProvince(u.getLoginProvince());
            user.setLoginCity(u.getLoginCity());
            user.setLoginLat(u.getLoginLat());
            user.setLoginLng(u.getLoginLng());
            user.setLastLoginTime(new Date());
            User newUser = userService.save(user);
            info.put("user", newUser);
            info.put("token", token);
            log.info("用户 {} 登陆，登陆地点：{}-{}", user.getNickname(), user.getLoginProvince(), user.getLoginCity());
            return new Result(true, StatusCode.OK, "管理员登录成功", info);
        } else {
            return new Result(true, StatusCode.ERROR, "管理员登录失败", null);
        }
    }

    @PostMapping(value = "/registor")
    public Result post(@RequestBody Map<String, User> para)  {
        User u = para.get("user");
        if (u != null) {
            User user = userService.save(u);
            String token = TokenUtil.sign(new UserEntity());
            Map<String, Object> info = new HashMap<>();
            info.put("user", user);
            info.put("token", token);
            return new Result(true, StatusCode.OK, "管理员登录成功", info);
        } else {
            return new Result(true, StatusCode.ERROR, "管理员登录失败", null);
        }
    }

    @GetMapping(value = "/users")
    public Result get(){
        return new Result(true, StatusCode.OK, "获取用户列表成功", userService.listUser());
    }
}
