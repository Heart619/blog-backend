package com.example.blog2.service.old;

import com.example.blog2.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/4 9:46
 */

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    List<Type> listByNameExceptSelf(Long id,String name);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

}
