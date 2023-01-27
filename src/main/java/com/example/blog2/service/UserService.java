package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserLoginVo;

import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获得所有用户省份信息
     * @return
     */
    List<UserEntity> getUserAreaList();

    /**
     * 用户登录
     * @param user
     * @return
     */
    UserLoginVo login(UserEntity user);

    /**
     * 注册
     * @param user
     * @return
     */
    UserLoginVo register(UserEntity user);

    /**
     * 修改头像
     * @param user
     * @return
     */
    UserEntity setAvatar(UserEntity user);

    /**
     * 获得用户头像和昵称
     * @return
     */
    List<UserEntity> getUserAvatarAndNickName();

    /**
     * 修改密码
     * @param vo
     */
    UserLoginVo updatePwd(PasswordUpdateVo vo);

    /**
     * 删除用户
     * @param id
     */
    void removeUser(Long id);
}

