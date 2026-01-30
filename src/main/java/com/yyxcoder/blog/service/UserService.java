package com.yyxcoder.blog.service;

import com.yyxcoder.blog.dto.UserRegisterDTO;
import com.yyxcoder.blog.dto.UserResponseDTO;
import com.yyxcoder.blog.entity.User;
import com.yyxcoder.blog.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*用户注册*/
    public UserResponseDTO register(UserRegisterDTO registerDTO){
        System.out.println("=== 开始用户注册 ===");
        System.out.println("用户名: " + registerDTO.getUsername());
        System.out.println("邮箱: " + registerDTO.getEmail());

        //1. 检查用户名是否已存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        //2. 检查邮箱是否已存在
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        //3. 创建用户实体
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword()); // 暂时不加密
        user.setEmail(registerDTO.getEmail());
        user.setCreateTime(LocalDateTime.now());

        //4. 保存到数据库
        User savedUser = userRepository.save(user);
        System.out.println("用户保存成功，ID: " + savedUser.getId());

        // 5. 转换为响应DTO
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setUsername(savedUser.getUsername());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setCreateTime(savedUser.getCreateTime());

        return responseDTO;
    }

    /*检查用户名是否存在*/
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
