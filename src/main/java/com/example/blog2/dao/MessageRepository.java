package com.example.blog2.dao;

import com.example.blog2.po.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/13 11:19
 */
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

}
