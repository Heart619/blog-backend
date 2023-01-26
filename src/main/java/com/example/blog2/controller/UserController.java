package com.example.blog2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.blog2.po.User;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.blog2.entity.UserEntity;
import com.example.blog2.service.UserService;



/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user){
		userService.save(user);

        return R.ok().put("data", user);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user){
		userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/del/{id}")
    public R delUser(@PathVariable("id") Long id) {
        try {
            userService.removeById(id);
            return R.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error();
        }
    }

    @GetMapping("/getUserAreaList")
    public R getUserAreaList() {
        List<UserEntity> users = userService.getUserAreaList();
        return R.ok().put("data", users);
    }

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

    @PostMapping("/setAvatar")
    public R setAvatar(@RequestBody UserEntity user) {
        try {
            UserEntity u =userService.setAvatar(user);
            return R.ok().put("data", u);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error();
        }
    }

    @PostMapping("/updatePwd")
    public R updatePwd(@RequestBody PasswordUpdateVo vo) {
        try {
            return R.ok().put("data", userService.updatePwd(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }
}
