# BlogSystem - 个人博客系统

基于 **Spring Boot 3 + Spring Data JPA + MySQL + Thymeleaf** 构建的个人博客系统，包含用户注册登录、文章管理、RESTful API 等基础功能模块。

---

## 功能特性

### 用户模块
- **注册** - 用户名/邮箱/密码注册，自动检查用户名和邮箱唯一性
- **登录/登出** - 基于 Session 的登录态管理
- **密码安全** - 采用 BCrypt 算法加密存储

### 博客模块
- **首页** - 展示文章列表（支持登录/未登录两种状态下的导航）
- **文章详情** - 文章内容、作者信息、评论展示
- **文章 CRUD** - 完整的 RESTful API（创建、查询、编辑、删除）
- **文章状态** - 支持草稿（DRAFT）和已发布（PUBLISHED）两种状态
- **分页查询** - 已发布文章支持分页、排序
- **模糊搜索** - 按标题和内容关键词搜索文章
- **热门排行** - 按浏览量展示 Top 5 热门文章
- **访问统计** - 文章浏览计数自动累加

### 技术亮点
- 分层架构设计（Controller -> Service -> Repository -> Entity）
- DTO 模式隔离实体和 API 数据
- 自定义 Session 工具类管理登录态
- JPA @Query 自定义查询
- 基于 Thymeleaf 的服务端模板渲染

---

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.5.10 | 应用框架 |
| Spring Data JPA | - | ORM / 数据持久化 |
| MySQL | 8.0 | 数据库 |
| Thymeleaf | - | 服务端模板引擎 |
| Lombok | - | 简化实体类代码 |
| BCrypt (spring-security-crypto) | 6.5.7 | 密码加密 |
| Hibernate Validator | - | 参数校验 |
| Maven | 3.9+ | 项目构建 |
| JDK | 17+ | 运行环境 |

> 注意：项目使用 spring-security-crypto 仅用于密码加密，不使用 Spring Security 的认证拦截功能，登录态由自定义 SessionUtil 管理。

---

## 快速开始

### 前置条件
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 1. 创建数据库

`sql
CREATE DATABASE IF NOT EXISTS blog_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
`

### 2. 配置数据库连接

编辑 src/main/resources/application.properties，修改数据库连接信息

### 3. 启动项目

`ash
# 方式一：Maven 直接启动
mvn spring-boot:run

# 方式二：编译后运行 JAR
mvn package -DskipTests
java -jar target/blog-system-0.0.1-SNAPSHOT.jar
`

### 4. 访问

首页: http://localhost:8080/
注册: http://localhost:8080/user/register
登录: http://localhost:8080/user/login
关于: http://localhost:8080/about
文章详情: http://localhost:8080/post/{id}

---

## 项目结构

`
pom.xml - Maven 依赖配置
src/main/java/com/yyxcoder/blog/
  BlogSystemApplication.java - 启动类
  TestController.java - 健康检查接口
  config/
    SecurityConfig.java - [预留] Spring Security 配置
    interceptor/LoginInterceptor.java - [预留] 登录拦截器
    exception/GlobalExceptionHandler.java - [预留] 全局异常处理
  controller/
    BlogController.java - 页面路由（首页/详情/关于）
    PostController.java - 文章 REST API
    UserController.java - 用户注册/登录页面
  dto/ - 数据传输对象
  entity/ - JPA 实体（User/Post/Comment）
  repository/ - 数据访问层
  service/ - 业务逻辑层（UserService/PostService）
  util/ - 工具类（PasswordUtil/SessionUtil）
src/main/resources/
  application.properties - 应用配置
  templates/ - Thymeleaf 模板
    index.html - 首页
    auth/login.html - 登录页
    auth/register.html - 注册页
    blog/about.html - 关于页
    blog/post/detail.html - 文章详情
    error/404.html - 404 页面
test/ - 单元测试
`

---

## 架构设计

分层架构: Controller -> DTO -> Service -> Repository -> Entity

请求流程示例（用户注册）:
1. 浏览器 GET /user/register -> UserController.showRegisterPage() -> 渲染 auth/register.html
2. 浏览器 POST /user/register -> UserController.register(UserRegisterDTO) -> UserService.register()
   -> 检查用户名/邮箱唯一性 -> PasswordUtil.encode() BCrypt 加密 -> UserRepository.save() -> 返回 UserResponseDTO

请求流程示例（文章 REST API）:
1. 客户端 POST /api/posts (JSON) -> PostController.createPost(PostRequestDTO)
   -> SessionUtil.getCurrentUserId() 检查登录 -> UserRepository.findById() 获取用户
   -> PostService.createPost() -> PostRepository.save() -> 返回 PostResponseDTO

---

## REST API

| 方法 | 路径 | 说明 | 需登录 |
|------|------|------|--------|
| GET | /api/posts/public | 分页获取已发布文章 | 否 |
| GET | /api/posts/hot | 热门文章 Top5 | 否 |
| GET | /api/posts/search?keyword= | 搜索文章 | 否 |
| GET | /api/posts/{id} | 文章详情 | 否 |
| POST | /api/posts | 创建文章 | 是 |
| PUT | /api/posts/{id} | 更新文章 | 是 |
| DELETE | /api/posts/{id} | 删除文章 | 是 |
| GET | /api/posts/my | 我的文章列表 | 是 |

---

## 实体关系

User (1) --- (N) Post --- 用户拥有多篇文章
Post (1) --- (N) Comment -- 文章有多条评论
User (1) --- (N) Comment -- 用户可发表多条评论

users 表: id, username(unique), password(加密), email, avatar
posts 表: id, title, content, status(DRAFT/PUBLISHED), view_count, author_id(FK)
comments 表: id, content, nickname, post_id(FK), user_id(FK)

JPA 配置 ddl-auto=update，首次启动自动建表。

---

## 待完善方向

### 基础完善
- 首页接入真实数据库数据（当前使用 Mock 数据）
- 启用 LoginInterceptor 实现页面级权限控制
- 启用 GlobalExceptionHandler 统一异常处理
- 完善文章详情页的前端展示

### 功能增强
- 文章编辑/管理后台页面
- Markdown 文章编辑与渲染
- 标签和分类系统
- 评论完整 CRUD 与审核
- 用户头像上传

### 技术深度
- 整合 Spring Security 实现角色权限
- 接入 Redis 缓存热门文章 / Session
- 前后端分离（JWT + Vue/React）
- 单元测试覆盖（JUnit + Mockito）
- Swagger/OpenAPI 接口文档

### 部署运维
- Docker + docker-compose 容器化部署
- 配置文件环境分离（dev/prod）
- CI/CD 自动化构建

---

## License
MIT
