package com.example.blog2.controller.admin;

import com.example.blog2.entity.UserEntity;
import com.example.blog2.po.Result;
import com.example.blog2.po.StatusCode;
import com.example.blog2.po.User;
import com.example.blog2.service.old.UserService;
import com.example.blog2.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/3/31 23:10
 */

//@RestController
//@RequestMapping("/admin")
//@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestHeader Map<String, Object> head, @RequestBody Map<String, Object> para) {
        String username = (String) para.get("username");
        String password = (String) para.get("password");
        User user = userService.checkUser(username, password);
        if (user != null) {
            String token = TokenUtil.sign(new UserEntity());
            Map<String,Object> info = new HashMap<>();
            info.put("user",user);
            info.put("token",token);
            return new Result(true, StatusCode.OK, "管理员登录成功", info);
        } else {
            return new Result(true, StatusCode.ERROR, "管理员登录失败", null);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
