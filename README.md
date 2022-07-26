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



## 4. MybatisPlus-update

### 4.1 根据Id更新操作

> 注意：update时生成的sql自动是动态sql：UPDATE user SET age=? WHERE id=?

```java
/**
     * 修改操作
     */
    @Test
    void updateUser() {
        User user = new User();
        user.setId(2L);
        user.setAge(30);
        int row = userMapper.updateById(user);
        System.out.println(row);//影响的行数
    }
```



### 4.2 自动填充

项目中经常会遇到一些数据，每次都使用相同的方式填充，例如记录的创建时间，更新时间等。

我们可以使用MyBatis Plus的自动填充功能，完成这些字段的赋值工作：



#### 4.2.1 数据库表中添加自动填充字段

在User表中添加datetime类型的新的字段 create_time、update_time



#### 4.2.2 实体上添加注解

```java
@Data
public class User {
    //IdType.ID_WORKER:MybatisPlus自带策略，生成19位值，数字类型使用这种策略，比如Long
    //IdType.ID_WORKER_STR:生成19位字符串类型的值，比如String
    //IdType.AUTO表示自动增长
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    //当创建一个新的对象时，自动填充创建时间和更新时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
```



#### 4.2.3 实现元对象处理器接口

```java
package com.ama.mpdemo1010.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 实现公共字段自动写入
 *
 * @author WangWenZhe
 * @date 2020/7/20
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 在执行插入操作之前执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 在执行更新操作之前执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
```



#### 4.2.4 总结

**实现自动填充的步骤：**

1. 在实体类上添加注解@TableField(fill = FieldFill.INSERT)和@TableField(fill = FieldFill.INSERT_UPDATE)
2. 实现元对象处理器接口；实现MetaObjectHandler接口，重写里面的insertFill和updateFill方法

以上两步实现后，可对新增的数据或者修改的数据自动添加值，无需再对一些默认数据进行赋值。



### 4.3 乐观锁

**主要解决：**丢失更新；多个人同时修改同一数据，最后提交更新的把之前的提交数据覆盖。

**主要适用场景：**当要更新一条记录的时候，希望这条记录没有被别人更新，也就是说实现线程安全的数据更新

**解决方案：**

- 悲观锁：当其中一个更新数据时，其他人不能更新数据。串行。
- 乐观锁：
  - 取出记录时，获取当前version
  - 更新时，带上这个version
  - 执行更新时， set version = newVersion where version = oldVersion
  - 如果version不对，就更新失败



#### 4.3.1 数据库中添加version字段

```mysql
ALTER TABLE `user` ADD COLUMN `version` INT
```



#### 4.3.2 实体类添加version字段

并添加 @Version 注解

```java
@Version
@TableField(fill = FieldFill.INSERT)
private Integer version;
```



#### 4.3.3 元对象处理器接口添加version的insert默认值

```java
package com.ama.mpdemo1010.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 实现公共字段自动写入
 *
 * @author WangWenZhe
 * @date 2020/7/20
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 在执行插入操作之前执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("version", 1, metaObject);
    }

    /**
     * 在执行更新操作之前执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
```



#### 4.3.4 配置MybatisPlusConfig乐观锁插件

新建MybatisPlusConfig类，将启动类上的@MapperScan注解放在MybatisPlusConfig类上。

```java
package com.ama.mpdemo1010.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 2022/7/25 23:05
 *
 * @Description 乐观锁插件
 * @Author WangWenZhe
 */
@Configuration
@MapperScan("com.ama.mpdemo1010.mapper")
public class MybatisPlusConfig {
    /**
     * 乐观锁插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
```



#### 4.3.6 测试

```java
/**
     * 测试乐观锁
     */
    @Test
    void testOptimisticLock() {
        //根据Id查询用户
        User user = userMapper.selectById(1550420141828943876L);
        //更新用户年龄
        user.setAge(31);
        //更新用户
        int row = userMapper.updateById(user);
    }
```

新添加的数据的version值为1，修改后，version值为2。



#### 4.3.5 总结

**实现乐观锁的步骤：**

1. 在表中增加int类型的version字段
2. 在实体类中添加version字段，并在该字段上添加@Version和@TableField(fill = FieldFill.INSERT)注解。
3. 在元对象处理器接口（继承的MetaObjectHandler类）添加version的insert默认值。
4. 新建MybatisPlusConfig类，并将@MapperScan注解从启动类放到该类中，再添加@Configuration注解。



## 5. MybatisPlus-select

### 5.1 通过id查询

```java
@Test
    void testOptimisticLock() {
        //根据Id查询用户
        User user = userMapper.selectById(1550420141828943876L);
        //更新用户年龄
        user.setAge(31);
        //更新用户
        int row = userMapper.updateById(user);
    }
```



### 5.2 通过多个id批量查询

```java
@Test
    void selectByIds() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));
        System.out.println(users);
    }
```



### 5.3 通过Map封装查询条件

```java
@Test
    void selectByMap() {
        //根据条件查询
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "world");
        map.put("age", 31);
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
    }
```

> 注意：map中的key对应的是数据库中的列名。例如数据库user_id，实体类是userId，这时map的key需要填写user_id



### 5.4 分页

MyBatis Plus自带分页插件，只要简单的配置即可实现分页功能

#### 5.4.1 分页配置类

在配置MybatisPlusConfig类中，新增分页插件

```java
/**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
```



#### 5.4.2 测试分页

```java
@Test
    void selectPage() {
        //创建分页对象,并传入页码和每页显示条数
        Page<User> page = new Page<>(1, 3);
        //分页查询
        IPage<User> users = userMapper.selectPage(page, null);
        System.out.println("当前页码===" + users.getCurrent());//当前页码
        System.out.println("总页数===" + users.getPages());//总页数
        System.out.println("每页显示条数===" + users.getSize());//每页显示条数
        System.out.println("当前页的数据===" + users.getRecords());//当前页的数据
        System.out.println("总记录数===" + users.getTotal());//总记录数
        System.out.println("是否有下一页===" + page.hasNext());//是否有下一页
        System.out.println("是否有上一页===" + page.hasPrevious());//是否有上一页
        System.out.println(users);
    }
```



## 6. MybatisPlus-delete

### 6.1 根据id删除

```java
/**
     * 物理删除 根据id删除
     */
    @Test
    void deleteById() {
        int row = userMapper.deleteById(1550420141828943876L);
        System.out.println(row);
    }
```



### 6.2 根据多个id批量删除

```java
/**
     * 物理删除 根据id批量删除
     */
    @Test
    void deleteByIds() {
        int row = userMapper.deleteBatchIds(Arrays.asList(1550420141828943875L, 1550420141828943874L));
        System.out.println(row);
    }
```



### 6.3 根据简单条件删除

```java
/**
     * 根据条件删除
     */
    @Test
    void deleteByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Billie");
        map.put("age", 24);
        int row = userMapper.deleteByMap(map);
        System.out.println(row);
    }
```



### 6.4 逻辑删除

- 物理删除：真实删除，将对应数据从数据库中删除，之后查询不到此条被删除数据
- 逻辑删除：假删除，将对应数据中代表是否被删除字段状态修改为“被删除状态”，之后在数据库中仍旧能看到此条数据记录



#### 6.4.1 在表中添加delete字段

```mysql
ALTER TABLE `user` ADD COLUMN `deleted` boolean
```



#### 6.4.2 实体类添加deleted字段

并加上@TableLogic注解和@TableField(fill = FieldFill.INSERT)注解

```java
@TableLogic
@TableField(fill = FieldFill.INSERT)
private Integer deleted;
```



#### 6.4.3 元对象处理器接口添加deleted的insert默认值

```java
package com.ama.mpdemo1010.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 实现公共字段自动写入
 *
 * @author WangWenZhe
 * @date 2020/7/20
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 在执行插入操作之前执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("version", 1, metaObject);
        this.setFieldValByName("deleted", 0, metaObject);
    }

    /**
     * 在执行更新操作之前执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
```



#### 6.4.4 **application.properties** 加入配置

此为默认值，如果你的默认值和mp默认的一样,该配置可无

```properties
#删除时默认字段的值
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```



#### 6.4.5 在MybatisPlusConfig中注册Bean

```java
/**
  * 逻辑删除插件
  */
@Bean
public ISqlInjector sqlInjector() {
    return new LogicSqlInjector();
}
```



#### 6.4.6 总结

**总结：**

1. 在表中新增tinyint类型的deleted字段。
2. 在实体类增加deleted字段，并添加@TableLogic注解和@TableField(fill = FieldFill.INSERT)注解。
3. 在元对象处理器接口添加deleted的insert默认值。
4. 在properties文件中加入deleted字段的默认值。
5. 在MybatisPlusConfig文件中，注册Bean，一个逻辑删除的插件。

**注意：**

当使用MybatisPlus自带的查询时，会默认将deleted=0的条件加上。如果想实现deleted=1需要自己写查询语句。



## 7. 性能分析

性能分析拦截器，用于输出每条 SQL 语句及其执行时间

SQL 性能执行分析,开发环境使用，超过指定时间，停止运行。有助于发现问题



### 7.1 配置插件

在MybatisPlusConfig文件中，增加SQL执行性能分析插件

**参数说明：**

- maxTime： SQL 执行最大时长，超过自动停止运行，有助于发现问题。
- format： SQL是否格式化，默认false。

```java
/**
     * SQL执行性能分析插件
     * 开发环境使用，线上不推荐使用。
     * maxTime：指的是sql最大执行时长
     * 三种环境：
     * dev:开发环境;
     * test:测试环境;
     * prod:生产环境;
     */
    @Bean
    @Profile({"dev", "test"})//dev和test环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(200);//ms,超过此处设置的ms则sql不执行
        performanceInterceptor.setFormat(true);//输出sql格式化
        return performanceInterceptor;
    }
```



### 7.2 配置dev环境

在配置文件设置环境

```properties
#环境设置
spring.profiles.active=dev
```



### 7.3 测试输出

#### 7.3.1 正常测试

新增一条数据

```java
@Test
    void insertUser() {
        User user = new User();
        user.setName("world");
        user.setAge(28);
        user.setEmail("world@qq.com");
        int result = userMapper.insert(user);
        System.out.println(result);//影响的行数
        System.out.println("insert:" + user);//id自动回填
    }
```

控制台输出

![image-20220726185340633](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726185340633.png)



#### 7.3.2 超时测试

将MaxTime参数设置为10后，SQL执行超时出现错误。

![image-20220726185816391](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726185816391.png)



## 8. wapper

### 8.1 wapper的介绍

![image-20220726191445136](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726191445136.png)

**说明：**

Wrapper ： 条件构造抽象类，最顶端父类

AbstractWrapper ： 用于查询条件封装，生成 sql 的 where 条件

QueryWrapper ： Entity 对象封装操作类，不是用lambda语法

 UpdateWrapper ： Update 条件封装，用于Entity对象更新操作

AbstractLambdaWrapper ： Lambda 语法使用 Wrapper统一处理解析 lambda 获取 column。

LambdaQueryWrapper ：看名称也能明白就是用于Lambda语法使用的查询Wrapper

LambdaUpdateWrapper ： Lambda 更新封装Wrapper



### 8.2 AbstractWrapper

#### 8.2.1 QueryWrapper常用的参数

![wrapper](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/wrapper.png)

**注意：**以下条件构造器的方法入参中的column均表示数据库字段



#### 8.2.2 指定要查询的列

```java
/**
     * 使用wrapper指定要查询的列
     * 结果：查询的数据中除了除了id和name之外，其他的数据都是null
     */
    @Test
    void selectByColumn() {
        //创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //添加条件
        queryWrapper.select("name", "age");
        //查询
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }
```



#### 8.2.3 set、setSql

最终的sql会合并 user.setAge()，以及 userUpdateWrapper.set() 和 setSql() 中的字段

```java
/**
     * 使用wrapper中的set、setSql
     */
    @Test
    void selectBySet() {
        //修改值
        User user = new User();
        user.setAge(99);
        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "t")
                .set("name", "老李头")//除了可以查询还可以使用set设置修改的字段
                .setSql("email = '123@qq.com'");//可以有子查询
        int result = userMapper.update(user, userUpdateWrapper);
        System.out.println(result);
    }
```

执行的Sql为

![image-20220726195522139](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726195522139.png)



## 9. 谷粒学苑-数据库设计

