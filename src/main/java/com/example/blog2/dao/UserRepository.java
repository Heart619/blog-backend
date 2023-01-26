package com.example.blog2.dao;

import com.example.blog2.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/3/31 22:59
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);

}
