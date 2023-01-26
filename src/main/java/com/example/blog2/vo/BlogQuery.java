package com.example.blog2.vo;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/15 16:16
 */
public class BlogQuery {
    private String title;
    private Long typeId;

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "BlogQuery{" +
                "title='" + title + '\'' +
                ", typeId=" + typeId +
                '}';
    }
}
