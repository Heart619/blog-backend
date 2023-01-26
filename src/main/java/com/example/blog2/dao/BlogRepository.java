package com.example.blog2.dao;

import com.example.blog2.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/13 23:03
 */

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    @Query("select function('date_format',b.createTime,'%Y') as year from Blog b group by function('date_format',b.createTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.createTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
//    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
//    Page<Blog> findByQuery(String query, Pageable pageable);
    @Query("select b from Blog b where b.title like ?1 ")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Query("select sum(b.views) from Blog b")
    Long countViews();

    @Query("select sum(b.appreciation) from Blog b")
    Long countAppreciate();

    @Query("select count (c) from Comment c")
    Long countComment();


    @Query("select function('date_format',b.createTime, '%Y-%m') AS MONTH,sum(b.views) as views from Blog b group by MONTH order by MONTH desc")
    List<String> ViewCountByMonth();

    @Query("select function('date_format',b.createTime, '%Y-%m') AS MONTH, count (b) as blogs from Blog b group by MONTH order by MONTH desc")
    List<String> BlogCountByMonth();

    @Query("select function('date_format',b.createTime, '%Y-%m') AS MONTH,sum(b.appreciation) as appreciate from Blog b group by MONTH order by MONTH desc")
    List<String> appreciateCountByMonth();

}
