package com.yyxcoder.blog.dto;

import com.yyxcoder.blog.entity.Post;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class PostRequestDTO {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String summary;

    private Post.Status status = Post.Status.DRAFT;
}
