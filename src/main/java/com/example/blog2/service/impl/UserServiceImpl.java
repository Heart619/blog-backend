package com.example.blog2.service.impl;

import com.example.blog2.config.TencentServerConfig;
import com.example.blog2.exception.UserNotFoundException;
import com.example.blog2.exception.UserPasswordErrorException;
import com.example.blog2.exception.UserExistsNickNameException;
import com.example.blog2.exception.UserExistsUserNameException;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.service.CommentService;
import com.example.blog2.service.MessageService;
import com.example.blog2.utils.*;
import com.example.blog2.vo.PasswordUpdateVo;
import com.example.blog2.vo.UserLocationVo;
import com.example.blog2.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.UserDao;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
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
    private TencentServerConfig tencentServerConfig;

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
    public UserLoginVo login(UserEntity user) throws UserNotFoundException, UserPasswordErrorException {
        UserEntity u = getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername()).last("limit 1"));
        if (u == null) {
            throw new UserNotFoundException();
        }
        if (!u.getPassword().equals(user.getPassword())) {
            throw new UserPasswordErrorException();
        }

        String ip = getIpAddress();
        UserLocationVo locationVo;
        if ("127.0.0.1".equals(ip)) {
            locationVo = getUserLocation(null);
        } else {
            locationVo = getUserLocation(ip);
        }

        try {
            UserLocationVo.Result result = locationVo.getResult();
            user.setLoginProvince(result.getAd_info().getProvince());
            user.setLoginCity(result.getAd_info().getCity());
            user.setLoginLat(result.getLocation().getLat());
            user.setLoginLng(result.getLocation().getLng());

            log.info("IP：{}，用户 [{}] 登陆，登陆地点：[{}-{}-{}-{}] ", result.getIp(), u.getNickname(), result.getAd_info().getNation(), user.getLoginProvince(), user.getLoginCity(), result.getAd_info().getDistrict());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

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
        return userLoginVo;
    }

    @Override
    public UserLoginVo register(UserEntity user) throws UserExistsNickNameException, UserExistsUserNameException {

        try {
            checkUserNameAndNickName(user);
        } catch (UserExistsNickNameException | UserExistsUserNameException e) {
            throw e;
        }

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setLastLoginTime(new Date());
        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar(DefaultImgUtils.getDefaultAvatarImg());
        }

        String ip = getIpAddress();
        UserLocationVo locationVo;
        if ("127.0.0.1".equals(ip)) {
            locationVo = getUserLocation(null);
        } else {
            locationVo = getUserLocation(ip);
        }

        try {
            UserLocationVo.Result result = locationVo.getResult();
            user.setLoginProvince(result.getAd_info().getProvince());
            user.setLoginCity(result.getAd_info().getCity());
            user.setLoginLat(result.getLocation().getLat());
            user.setLoginLng(result.getLocation().getLng());
            log.info("IP：{}， 用户 [{}] 注册成功，登陆地点：[{}-{}-{}-{}] ", result.getIp(), user.getNickname(), result.getAd_info().getNation(), user.getLoginProvince(), user.getLoginCity(), result.getAd_info().getDistrict());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        save(user);
        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setToken(TokenUtil.sign(user));
        loginVo.setUser(user);
        return loginVo;
    }

    private void checkUserNameAndNickName(UserEntity user) throws UserExistsNickNameException, UserExistsUserNameException {
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

        log.info("IP：{}， 用户 [{}] 修改密码", IPInterceptor.IP_INFO.get(), user.getNickname());
        return loginVo;
    }

    @Override
    public void removeUser(Long id) {
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
    public void updateUser(UserEntity user) throws UserExistsNickNameException, UserExistsUserNameException {

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

        updateById(user);
    }

    private String getIpAddress() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf (",") > 0) {
                ip = ip.substring (0, ip.indexOf (","));
            }
            if (ip.equals ("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                ip = inet.getHostAddress ();
            }
        }

        if (request.getHeader("X-Real-IP") != null && !"".equals(request.getHeader("X-Real-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("X-Real-IP"))) {
            ip = request.getHeader("X-Real-IP");
        }

        if (request.getHeader("Proxy-Client-IP") != null && !"".equals(request.getHeader("Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("Proxy-Client-IP"))) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (request.getHeader("WL-Proxy-Client-IP") != null && !"".equals(request.getHeader("WL-Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("WL-Proxy-Client-IP"))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private UserLocationVo getUserLocation(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder url = new StringBuilder("https://apis.map.qq.com/ws/location/v1/ip?key=");
        url.append(tencentServerConfig.getKey());
        if (!StringUtils.isEmpty(ip)) {
            url.append("&ip=").append(ip);
        }
        return restTemplate.getForObject(
                url.toString(),
                UserLocationVo.class
        );
    }
}
