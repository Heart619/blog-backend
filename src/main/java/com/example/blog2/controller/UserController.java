package com.example.blog2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.blog2.constant.ConstantUser;
import com.example.blog2.exception.UserExistsNickNameException;
import com.example.blog2.exception.UserExistsUserNameException;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.R;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserRecVo;
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
@RequestMapping("/admin/user")
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
    public R update(@RequestBody UserRecVo user){
        try {
            userService.updateUser(user);
            return R.ok("更新成功");
        } catch (UserExistsNickNameException e) {
            return R.error(ConstantUser.EXISTS_NICK_NAME);
        } catch (UserExistsUserNameException e) {
            return R.error(ConstantUser.EXISTS_USER_NAME);
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/del/{id}")
    public R delUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return R.ok();
    }

    @GetMapping("/getUserAreaList")
    public R getUserAreaList() {
        List<UserEntity> users = userService.getUserAreaList();
        return R.ok().put("data", users);
    }

    @PostMapping("/setAvatar")
    public R setAvatar(@RequestBody UserEntity user) {
        UserEntity u =userService.setAvatar(user);
        return R.ok().put("data", u);
    }

    @PostMapping("/updatePwd")
    public R updatePwd(@RequestBody PasswordUpdateVo vo) {
        return R.ok().put("data", userService.updatePwd(vo));
    }
}
