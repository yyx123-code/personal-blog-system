package com.yyxcoder.blog.controller;

import com.yyxcoder.blog.dto.UserRegisterDTO;
import com.yyxcoder.blog.dto.UserResponseDTO;
import com.yyxcoder.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*显示注册页面*/
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "register";
    }

    /*处理注册请求*/
    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterDTO registerDTO, Model model) {
        try {
            System.out.println("收到注册请求");

            // 调用Service进行注册
            UserResponseDTO user = userService.register(registerDTO);

            // 注册成功 - 同时保留用户输入的数据（用于显示）
            model.addAttribute("userRegisterDTO", registerDTO); // 重要：重新添加DTO
            model.addAttribute("success", "注册成功！用户ID: " + user.getId());

            return "register";  // 返回注册页面显示成功消息

        } catch (Exception e) {
            // 注册失败 - 保留用户输入的数据
            model.addAttribute("userRegisterDTO", registerDTO); // 重要：保留表单数据
            System.out.println("注册失败: " + e.getMessage());
            model.addAttribute("error", "注册失败: " + e.getMessage());
            return "register";  // 返回注册页面显示错误
        }
    }

    // 在UserController中添加以下方法：

    /*显示登录页面*/
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // 返回 login.html
    }

    /*处理登录请求（简易版，后续完善）*/
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        try {
            // 这里先简单验证，后续需要连接数据库
            System.out.println("登录尝试 - 用户名: " + username);

            // 模拟登录成功
            model.addAttribute("message", "登录成功！欢迎 " + username);
            return "redirect:/";  // 重定向到首页

        } catch (Exception e) {
            model.addAttribute("error", "登录失败: " + e.getMessage());
            return "login";
        }
    }
}