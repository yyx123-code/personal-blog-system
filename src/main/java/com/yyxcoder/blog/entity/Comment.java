package com.yyxcoder.blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")        //对应数据库中的comments表
@Data
public class Comment {
    @Id                          //主键(PRIMARY KEY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Long id;

    @Column(columnDefinition = "TEXT")//MySQL的TEXT类型
    private String content;

    @ManyToOne                    //多对一关系(一文章多评论)
    @JoinColumn(name = "post_id") //外键字段名
    private Post post;  //属于哪篇文章

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 评论者

    private String nickname;  // 允许匿名评论

    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}