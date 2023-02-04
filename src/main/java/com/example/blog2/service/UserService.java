package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.exception.UserExistsNickNameException;
import com.example.blog2.exception.UserExistsUserNameException;
import com.example.blog2.exception.UserNotFoundException;
import com.example.blog2.exception.UserPasswordErrorException;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserLoginVo;
import com.example.blog2.vo.UserRecVo;

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
     * @throws UserNotFoundException
     * @throws UserPasswordErrorException
     */
    UserLoginVo login(UserRecVo user) throws Exception;

    /**
     * 注册
     * @param user
     * @return
     * @throws UserExistsNickNameException
     * @throws UserExistsUserNameException
     */
    UserLoginVo register(UserRecVo user) throws Exception;

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
    UserLoginVo updatePwd(PasswordUpdateVo vo) throws Exception;

    /**
     * 删除用户
     * @param id
     */
    void removeUser(Long id);

    /**
     * 更新用户信息
     * @param user
     * @throws UserExistsNickNameException
     * @throws UserExistsUserNameException
     */
    void updateUser(UserRecVo user) throws UserExistsNickNameException, UserExistsUserNameException;

    /**
     * 刷新token
     * @return
     */
    String refreshToken();
}

