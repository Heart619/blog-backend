package com.example.blog2.dao;

import com.example.blog2.po.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 16:43
 */
@Repository
public interface EssayRepository extends JpaRepository<Essay,Long> {

}
