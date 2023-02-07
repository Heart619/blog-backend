package com.example.blog2.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.blog2.config.OSSConfig;
import com.example.blog2.constant.ConstantImg;
import com.example.blog2.exception.UserStatusException;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.utils.OSSUtils;
import com.example.blog2.utils.PageUtils;
import com.example.blog2.utils.Query;
import com.example.blog2.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.PicturesDao;
import com.example.blog2.entity.PicturesEntity;
import com.example.blog2.service.PicturesService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author mxp
 */

@Slf4j
@Service
public class PicturesServiceImpl extends ServiceImpl<PicturesDao, PicturesEntity> implements PicturesService {

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private OSSConfig ossConfig;

    @Override
    public PageUtils queryPage(Map<String, Object> params, boolean visAll) {
        QueryWrapper<PicturesEntity> queryWrapper;
        if (visAll) {
            queryWrapper = new QueryWrapper<PicturesEntity>().ge("type", 2);
        } else {
            queryWrapper = new QueryWrapper<PicturesEntity>().eq("type", 2);
        }
        return query(params, queryWrapper);
    }

    private PageUtils query(Map<String, Object> params, Wrapper<PicturesEntity> queryWrapper) {
        IPage<PicturesEntity> page = this.page(
                new Query<PicturesEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        if (!TokenUtil.checkUserType()) {
            throw new UserStatusException();
        }

        byte[] bytes = file.getBytes();
        String key = ossConfig.getImgSrc() + UUID.randomUUID() + file.getOriginalFilename();
        ossUtils.upload(key, bytes);
        PicturesEntity picture = new PicturesEntity();
        picture.setImage(key);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (ConstantImg.IMG_COOKIE_NAME.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }

        if (cookie == null) {
            long id = IdWorker.getId();
            ResponseCookie c = ResponseCookie.from(ConstantImg.IMG_COOKIE_NAME, String.valueOf(id)) // key & value
                    .httpOnly(true)		// 禁止js读取
                    .secure(false)		// 在http下也传输
                    .path("/")			// path
                    .maxAge(60 * 60 * 24)	// 有效期
//                    .sameSite("None")	// 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外
                    .build();
            HttpServletResponse response = requestAttributes.getResponse();
            response.setHeader(HttpHeaders.SET_COOKIE, c.toString());
            picture.setBelong(id);

        } else {
            picture.setBelong(Long.parseLong(cookie.getValue()));
        }
        picture.setType(-1);
        save(picture);
        log.info("IP：{}，上传图片", IPInterceptor.IP_INFO.get());
        return ossConfig.getAddress() + "/" + key;
    }

    @Override
    public void updateOldPicturesByLongId(Long lid, Long blogId, Integer type) {
        baseMapper.updateOldPicturesByLongId(lid, blogId, type);
    }

    @Override
    public List<PicturesEntity> getWallImg() {
        log.info("IP：{}，读取所有照片", IPInterceptor.IP_INFO.get());
        return list(new QueryWrapper<PicturesEntity>().eq("type", 2));
    }

    @Override
    public void delPic(PicturesEntity pictures) {
        if (!TokenUtil.checkUserType()) {
            throw new UserStatusException();
        }

        ossUtils.del(pictures.getImage());
        removeById(pictures.getId());
        log.info("IP：{}，删除照片[{}]", IPInterceptor.IP_INFO.get(), pictures.getType());
    }

    @Override
    public void updateShowStatus(PicturesEntity pictures) {
        baseMapper.updatePicTypeById(pictures);
    }

}
