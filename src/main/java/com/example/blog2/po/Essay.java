package com.example.blog2.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/12 16:36
 */

@Entity
@Table(name = "t_essay")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Essay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long praise;
    private String title;
    private String content;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPraise() {
        return praise;
    }

    public void setPraise(Long praise) {
        this.praise = praise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "id=" + id +
                ", praise=" + praise +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", color='" + color + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
