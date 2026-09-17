package com.yyxcoder.blog.entity;//位置声明

import jakarta.persistence.*;   //JPA注解(Java持久化API)
import lombok.Data;             //Lombok库(自动生成getter/setter)
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime; //时间API

/*JPA注解*/
@Entity               //数据库表对应的实体类
@Table(name = "users")//指定对应的数据库表名是"users"(user是MySQL关键字不能作为表名)

/*Lombok提供的注解,自动生成以下方法
 *1.getter
 *2.setter
 *3.toString()
 *4.equals()和hashCode()
 * */
@Data
public class User {
    /*主键*/
    @Id
    /*主键生成策略-自增*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*长整型(BIGINT)*/
    private Long id;

    /*定义数据库列的属性*/
    //unique唯一  nullable非空  length长度限制
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)//非空
    private String password;

    @Column(unique = true)//唯一
    private String email;

    /*无注解，默认设置avatar VARCHAR(255) NULL*/
    private String avatar;  // 头像URL

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
/*
    /*创建时间-TIMESTAMP* /
    private LocalDateTime createTime;
    /*数据插入数据库前的回调函数* /
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();//创建时间
    }
*/

/*生成的MySQL表:
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    avatar VARCHAR(255),
    create_time TIMESTAMP,

    #MySQL会自动为UNIQUE约束创建唯一索引:uk = unique key
    UNIQUE INDEX uk_username (username),
    UNIQUE INDEX uk_email (email)
);
//有索引时，以下查询非常快
SELECT * FROM users WHERE username = 'john_doe';
SELECT * FROM users WHERE email = 'john@example.com';
//如果没有索引，这些查询会进行全表扫描
 */