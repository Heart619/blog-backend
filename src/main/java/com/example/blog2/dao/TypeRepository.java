package com.example.blog2.dao;

import com.example.blog2.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/4 10:04
 */

@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {
    Type findByName(String name);

    @Query("select t from Type t where t.id <> ?1 and t.name = ?2")
    List<Type> findByNameExceptSelf(Long id,String name);

    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);

}
