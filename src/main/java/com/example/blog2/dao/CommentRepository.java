package com.example.blog2.dao;

import com.example.blog2.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/20 16:02
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    @Query("select function('date_format',c.createTime, '%Y-%m') AS MONTH,count(c) as comment from Comment c group by MONTH order by MONTH desc")
    List<String> CommentCountByMonth();

    @Query("select c from Comment c where c.parentComment.id = ?1")
    List<Comment> findChildrenComment(Long rootId);
}
