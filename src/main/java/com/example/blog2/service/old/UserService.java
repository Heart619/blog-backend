package com.example.blog2.service.old;

import com.example.blog2.po.User;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/3/31 22:56
 */
public interface UserService {
    User checkUser(String username, String password);

    User findUserById(Long id);

    User save(User user);

    User updateUser(Long id, User admin);

    List<User> listUser();

    void deleteUserById(Long id);
}
