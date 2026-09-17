//// config/SecurityConfig.java
//package com.yyxcoder.blog.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        // 允许公开访问的路径
//                        .requestMatchers("/", "/index", "/home").permitAll()
//                        .requestMatchers("/api/posts/public/**").permitAll()
//                        .requestMatchers("/api/posts/search").permitAll()
//                        .requestMatchers("/api/posts/hot").permitAll()
//                        .requestMatchers("/api/posts/{id}").permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()  // 允许认证相关接口
//                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll()  // 如果使用 H2 数据库
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/api/auth/login")  // 使用你自己的登录接口
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/auth/logout")
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable());  // 开发阶段禁用 CSRF，生产环境要开启
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}