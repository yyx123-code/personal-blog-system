package com.yyxcoder.blog.dto;

import com.yyxcoder.blog.entity.Post;
import com.yyxcoder.blog.entity.User;
import lombok.Data;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private Post.Status status;
    private Long authorId;
    private String authorName;
    private Integer viewCount;

    public static PostResponseDTO fromEntity(Post post){
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setSummary(post.getSummary());
        dto.setStatus(post.getStatus());
        dto.setViewCount(post.getViewCount());

        User author = post.getAuthor();
        if(author != null){
            dto.setAuthorId(author.getId());
            dto.setAuthorName(author.getUsername());
        }
        return dto;
    }
}
