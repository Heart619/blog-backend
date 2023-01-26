package com.example.blog2.service.old;

import com.example.blog2.po.Essay;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 16:41
 */

public interface EssayService {
    List<Essay> listEssay();

    void deleteEssay(Long id);

    Essay saveEssay(Essay essay);

    Essay updateEssay(Long id,Essay essay);
}
