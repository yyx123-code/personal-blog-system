package com.yyxcoder.blog.service;

import com.yyxcoder.blog.dto.UserLoginDTO;
import com.yyxcoder.blog.dto.UserRegisterDTO;
import com.yyxcoder.blog.dto.UserResponseDTO;
import com.yyxcoder.blog.entity.User;
import com.yyxcoder.blog.repository.UserRepository;
import com.yyxcoder.blog.util.PasswordUtil;
import com.yyxcoder.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j//使用log代替System.out.println
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordUtil passwordUtil;

    public UserService(PasswordUtil passwordUtil) {
        this.passwordUtil = passwordUtil;
    }

    /*用户注册*/
    public UserResponseDTO register(UserRegisterDTO registerDTO){
        log.info("开始注册：username={},email={}",
                registerDTO.getUsername(),registerDTO.getEmail());

        //检查用户名and邮箱是否存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        //创建用户实体
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordUtil.encode(registerDTO.getPassword()));//加密密码

        //保存到数据库
        User savedUser = userRepository.save(user);
        log.info("注册成功：id={},username={}",
                savedUser.getId(),savedUser.getUsername());
      //转换为响应DTO
        return convertToResponseDTO(savedUser);
    }
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreateTime(user.getCreateTime());
        return dto;

    }

    /*检查用户名是否存在*/
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /*用户登录*/
    public UserResponseDTO login(UserLoginDTO loginDTO){
        log.info("用户尝试登录：username={}", loginDTO.getUsername());

        //根据用户名查找用户
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(()->{
                    log.warn("登录失败：用户不存在 - {}",
                            loginDTO.getUsername());
                    return new RuntimeException("用户名或密码错误");
                });

        //验证密码
        if(!passwordUtil.matches(loginDTO.getPassword(),user.getPassword())){
            log.warn("登录失败：密码错误-{}",
                    loginDTO.getUsername());
            throw new RuntimeException("用户名或密码错误");
        }

        log.info("用户登录成功：id={},user={}",user.getId(),user.getUsername());
        return convertToResponseDTO(user);
    }
}
