package com.example.blog2.dao;

import com.example.blog2.po.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author mxp
 * @date 2023/1/24 19:27
 */
@Repository
public interface PicturesRepository extends JpaRepository<Pictures,Long>, JpaSpecificationExecutor<Pictures> {


}
