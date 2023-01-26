package com.example.blog2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog2.entity.PicturesEntity;
import com.example.blog2.utils.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author mxp
 * @date 2023-01-25 09:47:20
 */
public interface PicturesService extends IService<PicturesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 上传博客内容中的图片
     * @param file
     * @return
     * @throws IOException
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 修改图片所属博客
     * @param lid
     * @param blogId
     * @param type 1 - blog   0-essay
     */
    void updateOldPicturesByLongId(Long lid, Long blogId, Integer type);

    /**
     * 获取图片墙所有照片
     * @return
     */
    List<PicturesEntity> getWallImg();

    /**
     * 删除图片
     * @param pictures
     */
    void delPic(PicturesEntity pictures);
}

