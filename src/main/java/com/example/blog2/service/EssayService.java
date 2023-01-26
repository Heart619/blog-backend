package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.EssayEntity;
import com.example.blog2.utils.PageUtils;

import java.util.Map;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
public interface EssayService extends IService<EssayEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增或修改随笔
     * @param essay
     */
    void saveEssay(EssayEntity essay);
}

