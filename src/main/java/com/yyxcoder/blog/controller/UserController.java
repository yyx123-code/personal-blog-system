package com.yyxcoder.blog.controller;

import com.yyxcoder.blog.dto.UserLoginDTO;
import com.yyxcoder.blog.dto.UserRegisterDTO;
import com.yyxcoder.blog.dto.UserResponseDTO;
import com.yyxcoder.blog.service.UserService;
import com.yyxcoder.blog.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SessionUtil sessionUtil;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*SpringMVC中处理HTTP请求的注解
     *@GetMapping:获取资源，处理HTTP GET（查询、获取）
     *@PostMapping：创建资源，处理HTTP POST（提交、创建）
     * */
    /*显示注册页面*/
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "auth/register";
    }
    /*将DTO对象添加到SpringMVC控制器的model中，让前端表单绑定这个对象*/

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

            return "auth/register";  // 返回注册页面显示成功消息

        } catch (Exception e) {
            // 注册失败 - 保留用户输入的数据
            model.addAttribute("userRegisterDTO", registerDTO); // 重要：保留表单数据
            System.out.println("注册失败: " + e.getMessage());
            model.addAttribute("error", "注册失败: " + e.getMessage());
            return "auth/register";  // 返回注册页面显示错误
        }
    }

    /*显示登录页面*/
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "auth/login";  // 返回 login.html
    }

    /*处理登录请求\*/
    @PostMapping("/login")
    public String login(
            @ModelAttribute UserLoginDTO loginDTO,
            HttpServletRequest request,
            Model model) {
        try {
            //验证登录
            UserResponseDTO user = userService.login(loginDTO);

            //设置Session
            sessionUtil.setUserSession(user);

            //重定向到首页，显示欢迎信息
            HttpSession session = request.getSession();
            session.setAttribute("welcomeMessage", "欢迎回来，" + user.getUsername() + "！");
            return "redirect:/";  // 重定向到首页

        } catch (Exception e) {
            model.addAttribute("userLoginDTO", loginDTO);//保留表单数据
            model.addAttribute("error", e.getMessage());

            return "auth/login";
        }
    }

    /*用户登出*/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        sessionUtil.logout();
        request.getSession().invalidate();
        return "redirect:/";
    }
}