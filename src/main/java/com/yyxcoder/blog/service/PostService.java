package com.yyxcoder.blog.service;

import com.yyxcoder.blog.entity.Post;
import com.yyxcoder.blog.entity.User;
import com.yyxcoder.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor//为所有final字段生成构造函数
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Transactional//开启事务，方法内的所有数据库操作要么全部成功，要么全部回滚
    public Post createPost(Post post, User author){
        post.setAuthor(author);//设置文章作者
        if(post.getSummary() == null || post.getSummary().isEmpty()){//摘要为空
            String content = post.getContent();
            String summary = content.length() > 200
                    ? content.substring(0,200)+"..."//截取两百个字+省略号
                    : content;//否则完整内容作为摘要
            post.setSummary(summary);
        }
        return postRepository.save(post);//保存文章到数据库
    }

    @Transactional
    public Post updatePost(Long id,Post updatedPost,User currentUser){//更新文章
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("文章不存在"));
        if(!existingPost.getAuthor().getId().equals(currentUser.getId())){
            throw new RuntimeException("无权限修改此文章");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setSummary(updatedPost.getSummary());
        existingPost.setStatus(updatedPost.getStatus());

        return postRepository.save(existingPost);
    }

    public Post getPostById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()->new RuntimeException("文章不存在"));

        post.setViewCount(post.getViewCount()+1);
        postRepository.save(post);

        return post;
    }

    //删除文章
    @Transactional
    public void deletePost(Long id,User currentUser){
        Post post = postRepository.findById(id)
                .orElseThrow(()->new RuntimeException("文章不存在"));
        if(!post.getAuthor().getId().equals(currentUser.getId())){
            throw new RuntimeException("无权删除此文章");
        }
        postRepository.delete(post);
    }

    //分页获取已发布文章
    public Page<Post> getPublishedPosts(Pageable pageable){
        return postRepository.findByStatusOrderByCreateTimeDesc(
                Post.Status.PUBLISHED,pageable);
    }

    //获取用户文章
    public Page<Post> getUserPosts(Long id, Pageable pageable){
        return postRepository.findByAuthorIdOrderByCreateTimeDesc(id,pageable);
    }

    //搜索文章
    public Page<Post> searchPosts(String keyword, Pageable pageable){
        return postRepository.search(keyword,pageable);
    }

    //获取热门文章
    public List<Post> getHotPosts(){
        return postRepository.findTop5ByStatusOrderByViewCountDesc(
                Post.Status.PUBLISHED
        );
    }

    //获取用户文章（不分页）
    public List<Post> getUserPosts(Long id){
        return postRepository.findByAuthorIdOrderByCreateTimeDesc(
                id,Pageable.unpaged()).getContent();
    }
}


