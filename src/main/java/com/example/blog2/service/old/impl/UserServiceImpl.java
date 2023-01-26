package com.example.blog2.service.old.impl;

import com.example.blog2.dao.UserRepository;
import com.example.blog2.po.User;
import com.example.blog2.service.old.PicturesService;
import com.example.blog2.service.old.UserService;
import com.example.blog2.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/3/31 22:58
 */

//@Service
public class UserServiceImpl implements UserService {

//    @Autowired
    private UserRepository userRepository;

//    @Autowired
    private PicturesService picturesService;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return user;
    }

    @Transactional
    @Override
    public User findUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User save(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (user.getAvatar() == null || "".equals(user.getAvatar())){
            user.setAvatar("default/avatar.png");
        }

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(Long id, User admin) {
        try {
            User u = userRepository.getOne(id);
            String old = u.getAvatar();
            BeanUtils.copyProperties(admin, u, MyBeanUtils.getNullPropertyNames(admin));
            User user = userRepository.save(u);
            if (!StringUtils.isEmpty(admin.getAvatar())) {
                picturesService.delOssImg(old);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> listUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.getOne(id);
        if (!StringUtils.isEmpty(user.getAvatar())) {
            picturesService.delOssImg(user.getAvatar());
        }
        userRepository.deleteById(id);
    }

}
