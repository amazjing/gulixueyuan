# 尚硅谷-谷粒教育

**学习地址：**https://www.bilibili.com/video/BV1dQ4y1A75e?p=2&spm_id_from=333.1007.top_right_bar_window_history.content.click

**目的：**学习搭建微服务，学习了解该项目。

**项目模式：**B2C模式

![image-20220714114451650](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220714114451650.png)

**项目模块：**

![image-20220714145318507](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220714145318507.png)

**项目采用：**前后端分离开发

- 后端技术
  - SpringBoot
  - SpringCloud
  - MybatisPlus
  - SpringSecurity
  - Redis
  - easyExcel
  - JWT
  - OAuth2
- 前端技术
  - vue
  - element-ui
  - axios
  - nodejs
- 其他技术
  - 阿里云oss
  - 阿里云视频点播服务
  - 阿里云短信
  - 微信支付和登录
  - docker
  - git
  - jenkins



## 1. 创建数据库

1. 创建数据库mybatis_plus

2. 创建User表

   ```java
   DROP TABLE IF EXISTS user;
   CREATE TABLE user (    
       id BIGINT(20) NOT NULL COMMENT '主键ID',    
       name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',    
       age INT(11) NULL DEFAULT NULL COMMENT '年龄',    
       email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',    
       PRIMARY KEY (id) 
   );
   ```

3. 导入数据

   ```java
   DELETE FROM user;
   INSERT INTO user (id, name, age, email) VALUES 
   (1, 'Jone', 18, 'test1@baomidou.com'), 
   (2, 'Jack', 20, 'test2@baomidou.com'), 
   (3, 'Tom', 28, 'test3@baomidou.com'), 
   (4, 'Sandy', 21, 'test4@baomidou.com'), 
   (5, 'Billie', 24, 'test5@baomidou.com');
   ```



## 2. 初始化项目

### 2.1 创建项目

使用 Spring Initializr 快速初始化一个 Spring Boot 工程，mpdemo1010项目。



### 2.2 添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.ama</groupId>
    <artifactId>mpdemo1010</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>mpdemo1010</name>
    <description>mpdemo1010</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>
        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
            <version>8.0.26</version>
        </dependency>
        <!--lombok用来简化实体类-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```



### 2.3 配置文件

>  如果SpringBoot使用的是2.1以后的版本，mysql的驱动使用的是com.mysql.cj.jdbc.Driver，数据库地址后面需要加上时区serverTimezone=GMT%2B8

```properties
#mysql数据库连接
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=123456
```

**注意：**

1. 这里的 url 使用了 ?serverTimezone=GMT%2B8 后缀，因为Spring Boot 2.1 集成了 8.0版本的jdbc驱动，这个版本的 jdbc 驱动需要添加这个后缀，否则运行测试用例报告如下错误：

   java.sql.SQLException: The server time zone value 'ÖÐ¹ú±ê×¼Ê±¼ä' is unrecognized or represents more 

2. 这里的 driver-class-name 使用了 com.mysql.cj.jdbc.Driver ，在 jdbc 8 中 建议使用这个驱动，之前的 com.mysql.jdbc.Driver 已经被废弃，否则运行测试用例的时候会有 WARN 信息



### 2.4 创建实体类以及Mapper

```java
package com.ama.mpdemo1010.entity;

import lombok.Data;

/**
 * 2022/7/20 20:24
 *
 * @Description
 * @Author WangWenZhe
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```

```java
package com.ama.mpdemo1010.mapper;

import com.ama.mpdemo1010.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
//不使用@Repository会导致使用@Autowired时，找不到对应的bean，出现爆红错误;
//因为类是动态创建的，但是程序可以正确的执行。为了避免报错，可以在dao层的接口上添加@Repository注解。
@Repository
public interface UserMapper extends BaseMapper<User> {
}
```



### 2.5 功能测试

在test文件夹中进行测试，查看所有用户数据。

```java
package com.ama.mpdemo1010;

import com.ama.mpdemo1010.entity.User;
import com.ama.mpdemo1010.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Mpdemo1010ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    //查询User中的所有数据
    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

}
```

### 2.6 配置日志

```properties
#mybatis日志
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

![image-20220722173913653](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220722173913653.png)



## 3. MybatisPlus-insert

### 3.1 insert方法

在单元测试类中增加insert方法

```java
/**
     * 添加操作
     */
    @Test
    void insertUser() {
        User user = new User();
        user.setName("Ama");
        user.setAge(18);
        user.setEmail("Ama@qq.com");
        int result = userMapper.insert(user);
        System.out.println(result);//影响的行数
        System.out.println("insert:" + user);//id自动回填
    }
```

执行该方法后，控制台输出：

![image-20220722180004921](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220722180004921.png)

不需要给id赋值(主键)，MybatisPlus会自动生成一个全局唯一的19位的id值。



### 3.2 主键策略

1. **ID_WORKER**：MyBatis-Plus默认的主键策略是：ID_WORKER 全局唯一ID

   参考资料：分布式系统唯一**ID**生成方案汇总：https://www.cnblogs.com/haoxinyue/p/5208136.html

2. 自增策略

   - 要想主键自增需要配置如下主键策略

     - 需要在创建数据表的时候设置主键自增
     - 实体字段中配置 @TableId(type = IdType.AUTO)

     ```java
     @TableId(type = IdType.AUTO)
     private Long id;
     ```

要想影响所有实体的配置，可以设置全局主键配置

```java
#全局设置主键生成策略
mybatis-plus.global-config.db-config.id-type=auto
```

其它主键策略：分析 IdType 源码可知

```java
@Getter
public enum IdType {
    /**
     * 数据库ID自增
     */
    AUTO(0),
    /**
     * 该类型为未设置主键类型
     */
    NONE(1),
    /**
     * 用户输入ID
     * 该类型可以通过自己注册自动填充插件进行填充
     */
    INPUT(2),

    /* 以下3种类型、只有当插入对象ID 为空，才自动填充。 */
    /**
     * 全局唯一ID (idWorker)
     */
    ID_WORKER(3),
    /**
     * 全局唯一ID (UUID)
     */
    UUID(4),
    /**
     * 字符串全局唯一ID (idWorker 的字符串表示)
     */
    ID_WORKER_STR(5);

    private int key;

    IdType(int key) {
        this.key = key;
    }
```



