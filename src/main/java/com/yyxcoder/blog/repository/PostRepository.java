package com.yyxcoder.blog.repository;

import com.yyxcoder.blog.entity.Post;
import org.springframework.data.domain.Page;//分页相关类
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;//JPA仓库接口
import org.springframework.data.jpa.repository.Query;//自定义查询注解
import org.springframework.stereotype.Repository;
import java.util.List;//list集合类

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /*Page<Post>返回数据列表和分页(总页数、当前页)
    * findByStatus：根据status字段查询
    * OrderByCreateTimeDesc：按 createTime 字段降序（Desc）排序
    * status：文章状态（如：PUBLISHED 已发布、DRAFT 草稿）
    * pageable：分页参数（页码、每页大小、排序等）
    * */
    Page<Post> findByStatusOrderByCreateTimeDesc(Post.Status status, Pageable pageable);

    Page<Post> findByAuthorIdOrderByCreateTimeDesc(Long authorId, Pageable pageable);

    /*自定义查询搜索文章（使用JPQL查询语言）
    * @Query 注解:当方法命名规则不能满足需求时，可以自定义查询语句
    * p.status = 'PUBLISHED'：只查询已发布的文章
    * (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)：模糊查询
    * %:keyword%：% 表示任意字符，:keyword 表示参数占位符
    * LIKE '%关键字%'：查询包含关键字的记录
    * */
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND " +
            "(p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> search(String keyword, Pageable pageable);

    /*findTop5By：查询前5条记录
    * Status：根据 status 字段过滤
    * OrderByViewCountDesc：按 viewCount（浏览量）降序排列
    * */
    List<Post> findTop5ByStatusOrderByViewCountDesc(Post.Status status);
}