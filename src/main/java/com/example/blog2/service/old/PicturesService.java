package com.example.blog2.service.old;

import com.example.blog2.po.Pictures;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author mxp
 * @date 2023/1/24 19:26
 */
public interface PicturesService {

    Pictures saveType(Pictures pictures);

    Page<Pictures> listType(Pageable pageable);

    List<Pictures> listType();

    void deleteType(Long id, String key);

    void delOssImg(String key);
}
