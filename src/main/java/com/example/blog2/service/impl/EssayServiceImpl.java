package com.example.blog2.service.impl;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.utils.MyBeanUtils;
import com.example.blog2.utils.OSSUtils;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.EssayDao;
import com.example.blog2.entity.EssayEntity;
import com.example.blog2.service.EssayService;


/**
 * @author mxp
 */
@Service
public class EssayServiceImpl extends ServiceImpl<EssayDao, EssayEntity> implements EssayService {

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private OSSConfig ossConfig;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EssayEntity> page = this.page(
                new Query<EssayEntity>().getPage(params),
                new QueryWrapper<EssayEntity>().orderByDesc("create_time")
        );

        return new PageUtils(page);
    }

    @Override
    public void saveEssay(EssayEntity essay) {
        EssayEntity old = null;
        if (essay.getId() != null) {
            old = getById(essay.getId());
        }

        if (old != null) {
            String k = old.getContent();
            BeanUtils.copyProperties(essay, old, MyBeanUtils.getNullPropertyNames(old));
            ossUtils.delText(k);
            ossUtils.uploadText(k, old.getContent().getBytes(StandardCharsets.UTF_8));
            this.updateById(old);
        } else {
            ossUtils.uploadText(ossConfig.getEssay() + LocalDate.now() + "/" + essay.getTitle(), essay.getContent().getBytes(StandardCharsets.UTF_8));
            essay.setCreateTime(new Date());
            this.save(essay);
        }
    }

}
