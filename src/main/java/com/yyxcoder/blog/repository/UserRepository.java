package com.yyxcoder.blog.repository;

import com.yyxcoder.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/*数据访问层组件*/
@Repository

/*JpaRepository：Spring Data JPA 提供的基础仓库接口
 *User:操作User实体类
 *Long：User实体的主键(ID)是Long类型
 * */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

/*Optional<User>:返回一个 Optional 包装的 User 对象
 *如果找到用户：返回 Optional.of(user)
 *如果没找到：返回 Optional.empty()
 *避免返回 null，减少 NullPointerException
 * */

/*findByUsername:方法命名规则,自动生成对应的SQL查询
 *existsByUsername：方法命名规则,检查是否存在
 * */