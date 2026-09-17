//package com.yyxcoder.blog.config.interceptor;
//
//import com.yyxcoder.blog.util.SessionUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.util.UrlPathHelper;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class LoginInterceptor implements HandlerInterceptor {
//
//    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
//    private final SessionUtil sessionUtil;
//    private final UrlPathHelper urlPathHelper = new UrlPathHelper();
//
//    // 公开路径（不需要登录）
//    private static final List<String> PUBLIC_PATHS = Arrays.asList(
//            "/",
//            "/index",
//            "/index.html",
//            "/user/login",
//            "/user/login/**",
//            "/user/register",
//            "/user/register/**",
//            "/post/view/**",
//            "/error",
//            "/favicon.ico"
//    );
//
//    // 需要排除的路径前缀（如静态资源）
//    private static final List<String> EXCLUDE_PREFIXES = Arrays.asList(
//            "/css/",
//            "/js/",
//            "/images/",
//            "/fonts/",
//            "/static/",
//            "/webjars/",
//            "/swagger-ui/",
//            "/v3/api-docs",
//            "/api-docs",
//            "/h2-console"
//    );
//
//    // 需要排除的路径后缀（如静态文件）
//    private static final List<String> EXCLUDE_SUFFIXES = Arrays.asList(
//            ".css",
//            ".js",
//            ".jpg",
//            ".jpeg",
//            ".png",
//            ".gif",
//            ".ico",
//            ".svg",
//            ".woff",
//            ".woff2",
//            ".ttf",
//            ".eot"
//    );
//
//    public LoginInterceptor(SessionUtil sessionUtil) {
//        this.sessionUtil = sessionUtil;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) throws Exception {
//
//        String uri = request.getRequestURI();
//        String contextPath = request.getContextPath();
//        String path = uri.substring(contextPath.length());
//
//        log.debug("拦截请求: {}, 方法: {}, 完整路径: {}", path, request.getMethod(), uri);
//
//        // 1. 检查是否为公开路径
//        if (isPublicPath(path)) {
//            log.debug("公开路径，放行: {}", path);
//            return true;
//        }
//
//        // 2. 检查是否为静态资源
//        if (isStaticResource(path)) {
//            log.debug("静态资源，放行: {}", path);
//            return true;
//        }
//
//        // 3. 检查用户是否登录
//        boolean isLoggedIn = sessionUtil.isLoggedIn();
//        log.debug("登录状态检查: {}, 路径: {}", isLoggedIn, path);
//
//        if (!isLoggedIn) {
//            log.warn("用户未登录，拦截路径: {}", path);
//
//            // 对于AJAX请求，返回JSON错误
//            if (isAjaxRequest(request)) {
//                response.setContentType("application/json;charset=UTF-8");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("{\"code\":401,\"message\":\"请先登录\"}");
//                return false;
//            }
//
//            // 对于普通请求，重定向到登录页
//            String redirectUrl = "/user/login?redirect=" +
//                    urlPathHelper.getOriginatingRequestUri(request);
//            response.sendRedirect(contextPath + redirectUrl);
//            return false;
//        }
//
//        // 已登录，放行
//        log.debug("用户已登录，放行: {}", path);
//        return true;
//    }
//
//    /**
//     * 检查是否为公开路径
//     */
//    private boolean isPublicPath(String path) {
//        // 精确匹配
//        if (PUBLIC_PATHS.contains(path)) {
//            return true;
//        }
//
//        // 前缀匹配（支持通配符）
//        for (String publicPath : PUBLIC_PATHS) {
//            if (publicPath.endsWith("/**")) {
//                String prefix = publicPath.substring(0, publicPath.length() - 3);
//                if (path.startsWith(prefix)) {
//                    return true;
//                }
//            }
//        }
//
//        // 特殊处理：文章详情页（如 /post/123）
//        if (path.startsWith("/post/") && path.length() > "/post/".length()) {
//            String postIdPart = path.substring("/post/".length());
//            // 检查是否全是数字（简单验证）
//            if (postIdPart.matches("\\d+")) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 检查是否为静态资源
//     */
//    private boolean isStaticResource(String path) {
//        // 检查前缀
//        for (String prefix : EXCLUDE_PREFIXES) {
//            if (path.startsWith(prefix)) {
//                return true;
//            }
//        }
//
//        // 检查后缀
//        for (String suffix : EXCLUDE_SUFFIXES) {
//            if (path.endsWith(suffix)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 判断是否为AJAX请求
//     */
//    private boolean isAjaxRequest(HttpServletRequest request) {
//        String requestedWith = request.getHeader("X-Requested-With");
//        String contentType = request.getContentType();
//
//        return "XMLHttpRequest".equals(requestedWith) ||
//                (contentType != null && contentType.contains("application/json"));
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response,
//                                Object handler,
//                                Exception ex) throws Exception {
//        if (ex != null) {
//            log.error("请求处理异常: {}", request.getRequestURI(), ex);
//        }
//    }
//}