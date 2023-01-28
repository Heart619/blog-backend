package com.example.blog2.dao;

import com.example.blog2.entity.BlogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog2.vo.DateCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author mxp
 * @email mxp@gmail.com
 * @date 2023-01-25 09:47:20
 */
@Mapper
public interface BlogDao extends BaseMapper<BlogEntity> {

    /**
     * 获取所有分类id
     * @return
     */
    List<Long> selectAllBlogTypeId();

    /**
     * 获得所有博客id和博客题目
     * @return
     */
    List<BlogEntity> selectAllBlogIdAndTitles();

    /**
     * 获取推荐博客
     * @return
     */
    List<BlogEntity> selectRecommedBlog();

    /**
     * 获取最新博客
     * @return
     */
    List<BlogEntity> selectNewBlog();

    /**
     * 根据月份统计阅读量
     * @return
     */
    List<DateCountVo> selectViewCountByMonth();

    /**
     * 根据月份统计文章数
     * @return
     */
    List<DateCountVo> selectBlogCountByMonth();

    /**
     * 根据月份统计点赞数
     * @return
     */
    List<DateCountVo> selectAppreciateCountByMonth();

    /**
     * 获得文章总数
     * @return
     */
    Long selectBlogCount();

    /**
     * 获得阅读总量
     * @return
     */
    Long selectViewCount();

    /**
     * 获得总点赞量
     * @return
     */
    Long selectAppreciateCount();

    /**
     * 查询
     * @param query
     * @return
     */
    List<BlogEntity> selectCondition(String query);

    /**
     * 更新阅读量
     * @param id
     * @param oldv
     * @param i
     * @return
     */
    int updateViews(@Param("id") Long id, @Param("oldv") int oldv, @Param("i") int i);
}
