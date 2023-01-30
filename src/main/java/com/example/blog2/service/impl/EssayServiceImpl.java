package com.example.blog2.service.impl;

import com.example.blog2.config.OSSConfig;
import com.example.blog2.constant.ConstantImg;
import com.example.blog2.entity.PicturesEntity;
import com.example.blog2.entity.UserEntity;
import com.example.blog2.exception.UserStatusException;
import com.example.blog2.interceptor.IPInterceptor;
import com.example.blog2.service.UserService;
import com.example.blog2.service.PicturesService;
import com.example.blog2.utils.*;
import com.example.blog2.vo.EssayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.blog2.dao.EssayDao;
import com.example.blog2.entity.EssayEntity;
import com.example.blog2.service.EssayService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * @author mxp
 */

@Slf4j
@Service
public class EssayServiceImpl extends ServiceImpl<EssayDao, EssayEntity> implements EssayService {

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private OSSConfig ossConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private PicturesService picturesService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<EssayEntity> queryWrapper = new QueryWrapper<>();

        String userId = (String) params.get("userId");
        if (!StringUtils.isEmpty(userId)) {
            queryWrapper.eq("author", userId);
        }

        String search = (String) params.get("search");
        if (!StringUtils.isEmpty(search)) {
            queryWrapper.like("title", search);
        }

        queryWrapper.orderByDesc("create_time");
        IPage<EssayEntity> page = this.page(
                new Query<EssayEntity>().getPage(params),
                queryWrapper
        );

        Map<Long, UserEntity> map = userService.getUserAvatarAndNickName().stream().collect(Collectors.toMap(UserEntity::getId, x -> x));

        page.getRecords().forEach(x -> {
            x.setContent(MarkdownUtils.markdownToHtmlExtensions(new String(ossUtils.load(x.getContent()), StandardCharsets.UTF_8)));
            UserEntity user = map.get(x.getAuthor());
            if (user != null) {
                x.setNickName(user.getNickname());
                x.setAvatar(user.getAvatar());
            } else {
                x.setNickName("用户已注销");
                x.setAvatar(DefaultImgUtils.getDefaultAvatarImg());
            }
        });

        log.info("IP：{}， 随笔信息查询", IPInterceptor.IP_INFO.get());
        return new PageUtils(page);
    }

    @Override
    public void saveEssay(EssayEntity essay) {
        EssayEntity old = null;
        if (essay.getId() != null) {
            old = getById(essay.getId());
        }


        if (old != null) {
            if (!TokenUtil.checkCurUserStatus(essay.getAuthor())) {
                throw new UserStatusException();
            }

            String k = old.getContent();
            ossUtils.del(k);
            String[] split = k.split("/");
            if (split.length > 2) {
                k = ossConfig.getEssay() + split[2] + "/" + UUID.randomUUID();
            }
            essay.setContent(ossUtils.upload(k, essay.getContent().getBytes(StandardCharsets.UTF_8)));
            this.updateById(essay);
            log.info("IP：{}，用户[{}], 更新随笔[{}]", IPInterceptor.IP_INFO.get(), essay.getAuthor(), essay.getTitle());
        } else {
            if (!TokenUtil.checkUserType()) {
                throw new UserStatusException();
            }

            String k = ossConfig.getEssay() + LocalDate.now() + "/" + UUID.randomUUID();
            ossUtils.upload(k, essay.getContent().getBytes(StandardCharsets.UTF_8));
            essay.setCreateTime(new Date());
            essay.setContent(k);
            this.save(essay);
            log.info("IP：{}，用户[{}], 新增随笔[{}]", IPInterceptor.IP_INFO.get(), essay.getAuthor(), essay.getTitle());
        }

        CompletableFuture.runAsync(() -> {
            updatePictureBelongEssay(essay.getId());
        }, executor);

    }

    /**
     * 更新图片所属随笔
     * @param essay
     */
    private void updatePictureBelongEssay(Long essay) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Cookie c = null;
            for (Cookie cookie : cookies) {
                if (ConstantImg.IMG_COOKIE_NAME.equals(cookie.getName())) {
                    c = cookie;
                    break;
                }
            }
            if (c != null) {
                long id = Long.parseLong(c.getValue());
                picturesService.updateOldPicturesByLongId(id, essay, 0);
            }
        }
    }


    @Override
    public void delEssayById(Long id) {
        EssayEntity essay = getById(id);
        if (!TokenUtil.checkCurUserStatus(essay.getAuthor())) {
            throw new UserStatusException();
        }

        removeById(id);

        CompletableFuture.runAsync(() -> {
            // 删除随笔内容
            ossUtils.del(essay.getContent());
        }, executor);

        CompletableFuture.runAsync(() -> {
            // 删除随笔所包含的图片（如果有）
            List<PicturesEntity> entities = picturesService.list(new QueryWrapper<PicturesEntity>().eq("type", 0).eq("belong", id));
            if (!CollectionUtils.isEmpty(entities)) {
                List<Long> longs = entities.stream().map(x -> {
                    ossUtils.del(x.getImage());
                    return x.getId();
                }).collect(Collectors.toList());
                picturesService.removeByIds(longs);
            }
        }, executor);

        log.info("IP：{}，用户[{}], 新增随笔[{}]", IPInterceptor.IP_INFO.get(), essay.getAuthor(), essay.getTitle());
    }

    @Override
    public EssayVo getDefaultInfo(Long id) {
        EssayEntity essay = getById(id);
        EssayVo vo = new EssayVo();
        BeanUtils.copyProperties(essay, vo);
        vo.setContent(new String(ossUtils.load(vo.getContent()), StandardCharsets.UTF_8));
        return vo;
    }

}
