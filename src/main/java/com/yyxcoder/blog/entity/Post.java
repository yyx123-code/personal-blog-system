package com.yyxcoder.blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;//长文本

    @Column(columnDefinition = "TEXT")
    private String summary;//摘要

    /*枚举字段*/
    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;  // 状态：草稿/已发布

    /*多篇文章对应一个作者*/
    @ManyToOne
    /*指定外键列名author_id关联到User表主键*/
    @JoinColumn(name = "author_id")
    private User author;//作者

    private Integer viewCount = 0;  // 浏览量

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

    public enum Status {
        DRAFT,   // 草稿
        PUBLISHED // 已发布
    }
}