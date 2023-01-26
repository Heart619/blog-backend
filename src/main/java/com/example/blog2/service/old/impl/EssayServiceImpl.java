package com.example.blog2.service.old.impl;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.dao.EssayRepository;
import com.example.blog2.po.Essay;
import com.example.blog2.service.old.EssayService;
import com.example.blog2.utils.MyBeanUtils;
import com.example.blog2.utils.OSSUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 16:41
 */
//@Service
public class EssayServiceImpl implements EssayService {

//    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private OSSUtils OSSUtils;

    @Override
    public List<Essay> listEssay() {
        List<Essay> res = essayRepository.findAll();
        res.forEach(x -> {
            x.setContent(new String(OSSUtils.loadText(x.getContent()), StandardCharsets.UTF_8));
        });
        return res;
    }

    @Override
    public void deleteEssay(Long id) {
        Essay essay = essayRepository.getOne(id);
        if (!StringUtils.isEmpty(essay.getContent())) {
            OSSUtils.delText(essay.getContent());
        }
        essayRepository.deleteById(id);
    }

    @Override
    public Essay saveEssay(Essay essay) {
        essay.setCreateTime(new Date());
        byte[] bytes = essay.getContent().getBytes(StandardCharsets.UTF_8);
        String k = ossConfig.getEssay() + LocalDate.now() + "/" + essay.getTitle();
        essay.setContent(OSSUtils.uploadText(k, bytes));
        return essayRepository.save(essay);
    }

    @Override
    public Essay updateEssay(Long id, Essay essay) {
        Essay e = essayRepository.getOne(id);
        String t = e.getContent();
        BeanUtils.copyProperties(essay, e, MyBeanUtils.getNullPropertyNames(essay));
        if (!StringUtils.isEmpty(essay.getContent())) {
            OSSUtils.delText(t);
            OSSUtils.uploadText(t, essay.getContent().getBytes(StandardCharsets.UTF_8));
            e.setContent(t);
        }
        return essayRepository.save(e);
    }


}
