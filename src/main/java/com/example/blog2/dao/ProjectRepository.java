package com.example.blog2.dao;

import com.example.blog2.po.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 21:07
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

}
