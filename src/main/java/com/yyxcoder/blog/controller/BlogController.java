package com.yyxcoder.blog.controller;

import com.yyxcoder.blog.dto.UserResponseDTO;
import com.yyxcoder.blog.service.UserService;
import com.yyxcoder.blog.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BlogController {

    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private UserService userService;

    // 首页 - 显示文章列表
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        System.out.println("=== 访问首页 ===");

        // 创建虚拟文章列表
        List<Map<String, Object>> posts = new ArrayList<>();

        if (sessionUtil.isLoggedIn()) {
            UserResponseDTO user = sessionUtil.getCurrentUser();
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userId", user.getId());
        }

        //获取欢迎消息
        HttpSession session = (HttpSession) request.getSession();
        if (session != null && session.getAttribute("welcomeMessage") != null) {
            model.addAttribute("welcomeMessage", session.getAttribute("welcomeMessage"));
            session.removeAttribute("welcomeMessage");  // 一次性显示
        }

        // 文章1
        Map<String, Object> post1 = new HashMap<>();
        post1.put("id", 1);
        post1.put("title", "Spring Boot入门教程");
        post1.put("summary", "学习Spring Boot的基础知识");
        post1.put("createTime", LocalDateTime.now());
        post1.put("viewCount", 100);
        posts.add(post1);

        // 文章2
        Map<String, Object> post2 = new HashMap<>();
        post2.put("id", 2);
        post2.put("title", "从嵌入式C到Java");
        post2.put("summary", "分享转码心得");
        post2.put("createTime", LocalDateTime.now().minusDays(1));
        post2.put("viewCount", 50);
        posts.add(post2);

        // 把数据传给页面
        model.addAttribute("posts", posts);
        model.addAttribute("message", "首页加载成功！");

        return "index";  // 对应 templates/index.html
    }

    // 文章详情页
    @GetMapping("/post/{id}")
    public String postDetail(@PathVariable Integer id, Model model) {
        System.out.println("=== 访问文章详情，ID: " + id + " ===");

        // 创建虚拟文章数据
        Map<String, Object> post = new HashMap<>();
        post.put("id", id);
        post.put("title", "文章标题 - ID:" + id);
        post.put("content", "# 这是第" + id + "篇文章\n\n欢迎阅读！\n\n## 章节1\n内容...");
        post.put("summary", "文章摘要");
        post.put("createTime", LocalDateTime.now());
        post.put("viewCount", 88);
        post.put("status", "PUBLISHED");

        // 虚拟作者
        Map<String, Object> author = new HashMap<>();
        author.put("username", "rm_coder");
        post.put("author", author);

        // 虚拟评论
        List<Map<String, Object>> comments = new ArrayList<>();
        Map<String, Object> comment1 = new HashMap<>();
        comment1.put("nickname", "读者A");
        comment1.put("content", "好文章！");
        comment1.put("createTime", LocalDateTime.now().minusHours(2));
        comments.add(comment1);

        // 传递数据
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("message", "文章详情页加载成功！");

        return "post/detail";  // 对应 templates/post/detail.html
    }

    // 关于页面
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("message", "关于页面");
        return "about";
    }
}