package com.example.blog2.service.impl;

import com.example.blog2.po.User;
import com.example.blog2.utils.OSSUtils;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import com.example.blog2.utils.TokenUtil;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.UserDao;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.service.UserService;


/**
 * @author mxp
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<UserEntity> getUserAreaList() {
        return baseMapper.selectUserAreaList();
    }

    @Override
    public UserLoginVo login(UserEntity user) {
        UserEntity u = getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername()));
        if (u == null) {
            return null;
        }
        if (!u.getPassword().equals(user.getPassword())) {
            return null;
        }

        String token = TokenUtil.sign(u);
        u.setLoginProvince(user.getLoginProvince());
        u.setLoginCity(user.getLoginCity());
        u.setLoginLat(user.getLoginLat());
        u.setLoginLng(user.getLoginLng());
        user.setLastLoginTime(new Date());
        u.setLastLoginTime(user.getLastLoginTime());
        user.setId(u.getId());
        updateById(user);

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUser(u);
        userLoginVo.setToken(token);
        return userLoginVo;
    }

    @Override
    public UserLoginVo register(UserEntity user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (user.getAvatar() == null || "".equals(user.getAvatar())) {
            user.setAvatar("default/avatar.png");
        }
        save(user);

        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setUser(user);
        loginVo.setToken(TokenUtil.sign(user));
        return loginVo;
    }

    @Override
    public UserEntity setAvatar(UserEntity user) {
        UserEntity userEntity = getById(user.getId());
        String avatar = userEntity.getAvatar();
        if (!"default/avatar.png".equals(avatar)) {
            ossUtils.del(avatar);
        }
        updateById(user);
        userEntity.setAvatar(user.getAvatar());
        return userEntity;
    }

    @Override
    public List<UserEntity> getUserAvatarAndNickName() {
        return baseMapper.selectUserAvatarAndNickName();
    }

    @Override
    public UserLoginVo updatePwd(PasswordUpdateVo vo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(vo.getId());
        userEntity.setPassword(vo.getNewPwd());
        userEntity.setUpdateTime(new Date());
        updateById(userEntity);

        UserEntity user = getById(userEntity.getId());
        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setUser(user);
        loginVo.setToken(TokenUtil.sign(user));
        return loginVo;
    }

}
