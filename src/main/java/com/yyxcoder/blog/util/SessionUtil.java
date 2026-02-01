package com.yyxcoder.blog.util;

import com.yyxcoder.blog.dto.UserResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component//该类注册为Spring Bean 可通过@Autowired注入使用
public class SessionUtil {
    //用户登录后设置Session
    public void setUserSession(UserResponseDTO user) {
        HttpSession session = getCurrentSession();
        session.setAttribute("currentUser", user);//存储完整的用户对象
        session.setAttribute("userId", user.getId());//存id
        session.setAttribute("username", user.getUsername());//存储用户名

        //设置session过期时间（30分钟）
        session.setMaxInactiveInterval(30 * 60);
    }

    //获取当前登录用户
    public UserResponseDTO getCurrentUser() {
        HttpSession session = getCurrentSession();
        return (UserResponseDTO) session.getAttribute("currentUser");
    }

    //获取当前用户ID
    public Long getCurrentUserId() {
        HttpSession session = getCurrentSession();
        return (Long) session.getAttribute("userId");
    }

    //检查用户是否登录
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    //用户登出
    public void logout() {
        HttpSession session = getCurrentSession();
        session.invalidate();//失效
    }

    private HttpSession getCurrentSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getSession(true);
    }
    /*
     Spring 在处理每个 HTTP 请求时，会把当前请求的信息（HttpServletRequest）绑定到当前线程
     在请求处理的任何地方（控制器、Service、工具类），只要在同一个线程中，都能获取到当前请求
     currentRequestAttributes()：返回当前线程绑定的请求属性
     */
}