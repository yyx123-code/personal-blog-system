package com.yyxcoder.blog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "🎉 Spring Boot启动成功！";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello Blog System!";
    }
}