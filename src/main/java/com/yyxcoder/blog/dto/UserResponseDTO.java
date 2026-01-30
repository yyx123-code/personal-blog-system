package com.yyxcoder.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private long id;
    private String username;
    private String email;
    private LocalDateTime createTime;
}
