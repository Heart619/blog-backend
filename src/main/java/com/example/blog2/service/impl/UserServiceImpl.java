package com.example.blog2.service.impl;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.blog2.config.TencentServerConfig;
import com.example.blog2.exception.*;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.interceptor.TokenInterceptor;
import com.example.blog2.service.CommentService;
import com.example.blog2.service.MessageService;
import com.example.blog2.utils.*;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserLocationVo;
import com.example.blog2.vo.UserLoginVo;
import com.example.blog2.vo.UserRecVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.UserDao;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author mxp
 */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommentService commentService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        String search = (String) params.get("search");
        if (!StringUtils.isEmpty(search)) {
            queryWrapper.like("nickname", search).or().like("username", search);
        }
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<UserEntity> getUserAreaList() {
        return baseMapper.selectUserAreaList();
    }

    @Override
    public UserLoginVo login(UserRecVo vo) throws Exception {
        UserEntity u = getOne(new QueryWrapper<UserEntity>().eq("username", vo.getUsername()).last("limit 1"));
        if (u == null) {
            throw new UserNotFoundException();
        }

        String password = vo.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(RSAUtil.decrypt(password), u.getPassword())) {
            throw new UserPasswordErrorException();
        }

        String ip = IPInterceptor.IP_INFO.get();
        UserLocationVo locationVo = LocationUtil.getUserLocation(ip);
        UserEntity user = new UserEntity();
        UserLocationVo.Result result = locationVo.getResult();
        user.setLoginProvince(result.getAd_info().getProvince());
        user.setLoginCity(result.getAd_info().getCity());
        user.setLoginLat(result.getLocation().getLat());
        user.setLoginLng(result.getLocation().getLng());
        user.setLastLoginTime(new Date());
        user.setId(u.getId());
        updateById(user);

        u.setLoginProvince(user.getLoginProvince());
        u.setLoginCity(user.getLoginCity());
        u.setLoginLat(user.getLoginLat());
        u.setLoginLng(user.getLoginLng());
        u.setLastLoginTime(user.getLastLoginTime());

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setToken(TokenUtil.sign(u));
        userLoginVo.setUser(u);
        userLoginVo.setRefreshToken(TokenUtil.getRefreshToken(u));

        log.info("IP：{}，用户 [{}] 登陆，登陆地点：[{}-{}-{}-{}] ", result.getIp(), u.getNickname(), result.getAd_info().getNation(), user.getLoginProvince(), user.getLoginCity(), result.getAd_info().getDistrict());
        return userLoginVo;
    }

    @Override
    public UserLoginVo register(UserRecVo u) throws Exception {

        try {
            checkUserNameAndNickName(u);
        } catch (UserExistsNickNameException | UserExistsUserNameException e) {
            throw e;
        }
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(u, user);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setLastLoginTime(new Date());
        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar(DefaultImgUtils.getDefaultAvatarImg());
        }

        String ip = IPInterceptor.IP_INFO.get();
        UserLocationVo locationVo = LocationUtil.getUserLocation(ip);
        UserLocationVo.Result result = locationVo.getResult();
        user.setLoginProvince(result.getAd_info().getProvince());
        user.setLoginCity(result.getAd_info().getCity());
        user.setLoginLat(result.getLocation().getLat());
        user.setLoginLng(result.getLocation().getLng());
        String decrypt = RSAUtil.decrypt(user.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(decrypt));
        user.setType(0);
        save(user);
        log.info("IP：{}， 用户 [{}] 注册成功，登陆地点：[{}-{}-{}-{}] ", result.getIp(), user.getNickname(), result.getAd_info().getNation(), user.getLoginProvince(), user.getLoginCity(), result.getAd_info().getDistrict());
        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setToken(TokenUtil.sign(user));
        loginVo.setUser(user);
        loginVo.setRefreshToken(TokenUtil.getRefreshToken(user));

        return loginVo;
    }

    private void checkUserNameAndNickName(UserRecVo user) throws UserExistsNickNameException, UserExistsUserNameException {
        UserEntity one = getOne(new QueryWrapper<UserEntity>().eq("nickname", user.getNickname()).or().eq("username", user.getUsername()).last("limit 1"));
        if (one == null) {
            return;
        }

        if (user.getId() != null && user.getId().equals(one.getId())) {
            return;
        }

        if (one.getUsername().equals(user.getUsername())) {
            throw new UserExistsUserNameException();
        }
        throw new UserExistsNickNameException();
    }

    @Override
    public UserEntity setAvatar(UserEntity user) {

        if (!TokenUtil.checkCurUserStatus(user.getId())) {
            throw new UserStatusException();
        }

        UserEntity userEntity = getById(user.getId());
        String avatar = userEntity.getAvatar();
        if (!DefaultImgUtils.isDefaultAvatarImg(avatar)) {
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
    public UserLoginVo updatePwd(PasswordUpdateVo vo) throws Exception {
        if (!TokenUtil.checkCurUserStatus(vo.getId())) {
            throw new UserStatusException();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setId(vo.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(RSAUtil.decrypt(vo.getNewPwd())));
        userEntity.setUpdateTime(new Date());
        updateById(userEntity);

        UserEntity user = getById(userEntity.getId());
        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setUser(user);
        loginVo.setToken(TokenUtil.sign(user));

        log.info("IP：{}， 用户 [{}] 修改密码", IPInterceptor.IP_INFO.get(), user.getNickname());
        return loginVo;
    }

    @Override
    public void removeUser(Long id) {

        if (!TokenUtil.checkCurUserStatus(id)) {
            throw new UserStatusException();
        }

        UserEntity user = getById(id);
        removeById(user);

        CompletableFuture.runAsync(() -> {
            if (!DefaultImgUtils.isDefaultAvatarImg(user.getAvatar())) {
                ossUtils.del(user.getAvatar());
            }
        }, executor);

        CompletableFuture.runAsync(() -> {
            messageService.userDelUpdateMessage(id);
        }, executor);

        CompletableFuture.runAsync(() -> {
            commentService.userDelUpdateComment(id);
        }, executor);


    }

    @Override
    public void updateUser(UserRecVo user) throws UserExistsNickNameException, UserExistsUserNameException {
        if (!TokenUtil.checkCurUserStatus(user.getId())) {
            throw new UserStatusException();
        }

        try {
            checkUserNameAndNickName(user);
        } catch (UserExistsNickNameException | UserExistsUserNameException e) {
            throw e;
        }

        UserEntity old = getById(user.getId());

        if (!old.getNickname().equals(user.getNickname())) {
            CompletableFuture.runAsync(() -> {
                commentService.updateCommentForUserUpdate(user.getId(), user.getNickname());
                messageService.updateMessageForUserUpdate(user.getId(), user.getNickname());
            }, executor);
        }
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(user, entity);
        updateById(entity);
    }

    @Override
    public String refreshToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String refresh = request.getHeader("refresh");
        if (StringUtils.isEmpty(refresh)) {
            throw new RuntimeException("用户身份异常");
        }
        refresh = refresh.substring(1, refresh.length() - 1);
        TokenInterceptor.UserTokenInfo adminVerify = null;
        try {
            adminVerify = TokenUtil.adminVerify(refresh);
        } catch (TokenExpiredException e) {
            throw new RefreshExpiresException(410, "用户登陆过期，请重新登陆");
        }
        UserEntity user = new UserEntity();
        user.setId(adminVerify.getId());
        user.setType(adminVerify.getType());

        CompletableFuture.runAsync(() -> {
            user.setLastLoginTime(new Date());
            updateById(user);
        }, executor);

        return TokenUtil.sign(user);
    }
}
