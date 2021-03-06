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

### 9.1 创建数据库

1. 新建数据库：guli_edu
2. 执行guli_edu.sql脚本



### 9.2 数据库设计规约

以下规约只针对本模块，更全面的文档参考《阿里巴巴Java开发手册》：五、MySQL数据库

1. 库名与应用名称尽量一致

2. 表名、字段名必须使用小写字母或数字，禁止出现数字开头，

3. 表名不使用复数名词

4. 表的命名最好是加上“业务名称_表的作用”。如，edu_teacher

5. 表必备三字段：id, gmt_create, gmt_modified

   **说明：**

   其中 id 必为主键，类型为 bigint unsigned、单表时自增、步长为 1。（如果使用分库分表集群部署，则id类型为verchar，非自增，业务中使用分布式id生成器）gmt_create, gmt_modified 的类型均为datetime类型，前者现在时表示主动创建，后者过去分词表示被动更新。

6. 单表行数超过 500 万行或者单表容量超过 2GB，才推荐进行分库分表。 说明：如果预计三年后的数据量根本达不到这个级别，请不要在创建表时就分库分表。

7. 表达是与否概念的字段，必须使用 is_xxx 的方式命名，数据类型是 unsigned tinyint （1 表示是，0 表示否）。

   **说明：**

   任何字段如果为非负数，必须是unsigned。

   **注意：**

   POJO 类中的任何布尔类型的变量，都不要加 is 前缀。数据库表示是与否的值，使用 tinyint 类型，坚持 is_xxx 的 命名方式是为了明确其取值含义与取值范围。

   正例：表达逻辑删除的字段名 is_deleted，1 表示删除，0 表示未删除。

8. 小数类型为decimal，禁止使用float 和double。 说明：float和double在存储的时候，存在精度损失的问题，很可能在值的比较时，得到不正确的结果。如果存储的数据范围超过decimal的范围，建议将数据拆成整数和小数分开存储。

9. 如果存储的字符串长度几乎相等，使用char定长字符串类型。

10. varchar 是可变长字符串，不预先分配存储空间，长度不要超过 5000，如果存储长度大于此值，定义字段类型为 text，独立出来一张表，用主键来对应，避免影响其它字段索引效率。

11. 唯一索引名为 **uk_字段名**；普通索引名则为 idx_字段名。

    说明：uk_ 即 unique key；idx_ 即 index 的简称

12. 不得使用外键与级联，一切外键概念必须在应用层解决。外键与级联更新适用于单机低并发，不适合分布式、高并发集群；级联更新是强阻塞，存在数据库更新风暴的风险；外键影响数据库的插入速度。



## 10. 谷粒学苑-工程结构介绍

![image-20220726205300617](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726205300617.png)

### 10.1 工程结构

![image-20220726205548926](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726205548926.png)





### 10.2 模块说明

- **guli-parent**：在线教学根目录（父工程），管理四个子模块：

  - **canal-client**：**canal**数据库表同步模块（统计同步数据）
  - **common**：公共模块父节点
    - common-util：工具类模块，所有模块都可以依赖于它
    - service-base：service服务的base包，包含service服务的公共配置类，所有service模块依赖于它
    - spring-security：认证与授权模块，需要认证授权的service服务依赖于它
  - **infrastructure**：基础服务模块父节点
    - api-gateway：api网关服务

  - **service**：**api**接口服务父节点
    - service-acl：用户权限管理api接口服务（用户管理、角色管理和权限管理等）
    - service-cms：cms api接口服务
    - service-edu：教学相关api接口服务
    - service-msm：短信api接口服务
    - service-order：订单相关api接口服务
    - service-oss：阿里云oss api接口服务
    - service-statistics：统计报表api接口服务
    - service-ucenter：会员api接口服务
    - service-vod：视频点播api接口服务



## 11. 谷粒学苑-创建父工程

(1). **使用IDEA创建guli_parent工程**

![image-20220726213005233](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726213005233.png)

(2). **修改pom.xml文件，改为2.2.1.RELEASE**

```
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/> 
    </parent>
```

(3). **在<artifactId> 节点后面添加pom类型**

```xml
<artifactId> 节点后面添加 pom类型
```

(4). **在pom.xml中添加依赖的版本**

删除pom.xml中的<dependencies>内容。

```xml
<!--以下内容删除-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

**添加** <properties>确定依赖的版本

```xml
    <properties>
        <java.version>1.8</java.version>
        <guli.version>0.0.1-SNAPSHOT</guli.version>
        <mybatis-plus.version>3.0.5</mybatis-plus.version>
        <velocity.version>2.0</velocity.version>
        <swagger.version>2.7.0</swagger.version>
        <aliyun.oss.version>2.8.3</aliyun.oss.version>
        <jodatime.version>2.10.1</jodatime.version>
        <poi.version>3.17</poi.version>
        <commons-fileupload.version>1.3.1</commons-fileupload.version>
        <commons-io.version>2.6</commons-io.version>
        <httpclient.version>4.5.1</httpclient.version>
        <jwt.version>0.7.0</jwt.version>
        <aliyun-java-sdk-core.version>4.3.3</aliyun-java-sdk-core.version>
        <aliyun-sdk-oss.version>3.1.0</aliyun-sdk-oss.version>
        <aliyun-java-sdk-vod.version>2.15.2</aliyun-java-sdk-vod.version>
        <aliyun-java-vod-upload.version>1.4.11</aliyun-java-vod-upload.version>
        <aliyun-sdk-vod-upload.version>1.4.11</aliyun-sdk-vod-upload.version>
        <fastjson.version>1.2.28</fastjson.version>
        <gson.version>2.8.2</gson.version>
        <json.version>20170516</json.version>
        <commons-dbutils.version>1.7</commons-dbutils.version>
        <canal.client.version>1.1.0</canal.client.version>
        <docker.image.prefix>zx</docker.image.prefix>
        <cloud-alibaba.version>0.2.2.RELEASE</cloud-alibaba.version>
    </properties>
```

(5). **配置<dependencyManagement>锁定依赖的版本**

```java
<dependencyManagement>
        <dependencies>
            <!--Spring Cloud-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--mybatis-plus 持久层-->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>

            <!-- velocity 模板引擎, Mybatis Plus 代码生成器需要 -->
            <dependency>
                <groupId>org.apache.velocity</groupId>
                <artifactId>velocity-engine-core</artifactId>
                <version>${velocity.version}</version>
            </dependency>

            <!--swagger-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <!--swagger ui-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>${swagger.version}</version>
            </dependency>

            <!--aliyunOSS-->
            <dependency>
                <groupId>com.aliyun.oss</groupId>
                <artifactId>aliyun-sdk-oss</artifactId>
                <version>${aliyun.oss.version}</version>
            </dependency>

            <!--日期时间工具-->
            <dependency>
                <groupId>joda-time</groupId>
                <artifactId>joda-time</artifactId>
                <version>${jodatime.version}</version>
            </dependency>

            <!--xls-->
            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>poi</artifactId>
                <version>${poi.version}</version>
            </dependency>
            <!--xlsx-->
            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>poi-ooxml</artifactId>
                <version>${poi.version}</version>
            </dependency>

            <!--文件上传-->
            <dependency>
                <groupId>commons-fileupload</groupId>
                <artifactId>commons-fileupload</artifactId>
                <version>${commons-fileupload.version}</version>
            </dependency>

            <!--commons-io-->
            <dependency>
                <groupId>commons-io</groupId>
                <artifactId>commons-io</artifactId>
                <version>${commons-io.version}</version>
            </dependency>

            <!--httpclient-->
            <dependency>
                <groupId>org.apache.httpcomponents</groupId>
                <artifactId>httpclient</artifactId>
                <version>${httpclient.version}</version>
            </dependency>

            <dependency>
                <groupId>com.google.code.gson</groupId>
                <artifactId>gson</artifactId>
                <version>${gson.version}</version>
            </dependency>

            <!-- JWT -->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt</artifactId>
                <version>${jwt.version}</version>
            </dependency>

            <!--aliyun-->
            <dependency>
                <groupId>com.aliyun</groupId>
                <artifactId>aliyun-java-sdk-core</artifactId>
                <version>${aliyun-java-sdk-core.version}</version>
            </dependency>
            <dependency>
                <groupId>com.aliyun.oss</groupId>
                <artifactId>aliyun-sdk-oss</artifactId>
                <version>${aliyun-sdk-oss.version}</version>
            </dependency>
            <dependency>
                <groupId>com.aliyun</groupId>
                <artifactId>aliyun-java-sdk-vod</artifactId>
                <version>${aliyun-java-sdk-vod.version}</version>
            </dependency>
            <dependency>
                <groupId>com.aliyun</groupId>
                <artifactId>aliyun-java-vod-upload</artifactId>
                <version>${aliyun-java-vod-upload.version}</version>
            </dependency>
            <dependency>
                <groupId>com.aliyun</groupId>
                <artifactId>aliyun-sdk-vod-upload</artifactId>
                <version>${aliyun-sdk-vod-upload.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>${fastjson.version}</version>
            </dependency>
            <dependency>
                <groupId>org.json</groupId>
                <artifactId>json</artifactId>
                <version>${json.version}</version>
            </dependency>

            <dependency>
                <groupId>commons-dbutils</groupId>
                <artifactId>commons-dbutils</artifactId>
                <version>${commons-dbutils.version}</version>
            </dependency>

            <dependency>
                <groupId>com.alibaba.otter</groupId>
                <artifactId>canal.client</artifactId>
                <version>${canal.client.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

删除guli_parent项目中的src文件夹。

**注意：**

1. **IDEA导入maven可能失败：**

   删除<dependencyManagement>标签，重新刷新即可下载。

2. **下载aliyun-java-sdk-vod与aliyun-java-vod-upload失败：**

   在官网https://help.aliyun.com/document_detail/106648.html下载所需要的版本

   ![image-20220726230020143](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726230020143.png)

   下载后名称为VODUploadDemo-java-1.4.11.zip，进行解压，将lib文件里面的aliyun-java-vod-upload-1.4.11.jar文件，复制到自己的maven文件夹下的bin文件见内。然后在bin文件夹内输入cmd，输入命令`mvn install:install-file -DgroupId=com.aliyun -DartifactId=aliyun-sdk-vod-upload -Dversion=1.4.11 -Dpackaging=jar -Dfile=aliyun-java-vod-upload-1.4.11.jar`，下载aliyun-sdk-vod-upload的maven包，然后再输入`mvn install:install-file  -DgroupId=com.aliyun  -DartifactId=aliyun-java-vod-upload -Dversion=1.4.11 -Dpackaging=jar -DlocalRepositoryPath=D:\Java\Maven\apache-maven-3.8.3\maven-repo(自己的maven仓库) -Dfile=aliyun-java-vod-upload-1.4.11.jar`，下载aliyun-java-vod-upload的maven包。

## 12.谷粒学苑-创建子模块

![image-20220726231216349](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220726231216349.png)

(7). **修改service的pom文件**

添加<packaging>

```java
<packaging>pom</packaging>
```

增加<dependencies>内容，注释掉的暂时用不到，不注释启动会报错。

```xml
<dependencies>
        <!--<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>-->
        <!--hystrix依赖，主要是用 @HystrixCommand -->
        <!--<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>-->

        <!--服务注册-->
        <!--<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>-->
        <!--服务调用-->
        <!--<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>-->

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>

        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- velocity 模板引擎, Mybatis Plus 代码生成器需要 -->
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
        </dependency>

        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
        </dependency>

        <!--lombok用来简化实体类：需要安装lombok插件-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <!--xls-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
        </dependency>

        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
        </dependency>

        <!--httpclient-->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
        </dependency>
        <!--commons-io-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
        </dependency>
        <!--gson-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
```

删除service项目中的src文件夹。



## 13. 谷粒学苑-创建service的子模块

### 13.1 创建子模块

在父工程**service**模块下面创建子模块**service_edu**，还是通过maven创建，与创建service项目时相同。



### 13.2 讲师管理模块

#### 13.2.1 创建配置文件

在service下面service_edu模块中resources目录下创建文件application.properties创建配置文件

```properties
#服务端口
server.port=8001
#服务名
spring.application.name=service_edu
# 环境设置：dev、test、prod
spring.profiles.active=dev
#mysql数据库连接
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/guli?serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=123456

#mybatis日志
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```



#### 13.2.2 创建MybatisPlus代码生成器

在test/java目录下创建包com.ama.eduservice，创建代码生成器：CodeGenerator.java

```java
package eduservice;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * 2022/7/26 23:46
 *
 * @Description 代码生成器
 * @Author WangWenZhe
 */
public class CodeGenerator {
    @Test
    public void run() {
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);
        //设置输出路径
        gc.setOutputDir("D:\\Java\\JavaProject\\github\\gulixueyuan\\guli_parent\\service\\service_edu" + "/src/main/java");
        gc.setAuthor("WangWenZhe");
        gc.setOpen(false); //生成后是否打开资源管理器
        gc.setFileOverride(false); //重新生成时文件是否覆盖
        gc.setServiceName("%sService"); //去掉Service接口的首字母I
        gc.setIdType(IdType.ID_WORKER); //主键策略
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
        gc.setSwagger2(true);//开启Swagger2模式

        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/guli_edu?serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("eduservice"); //模块名
        pc.setParent("com.ama");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("edu_teacher");
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setTablePrefix(pc.getModuleName() + "_"); //生成实体时去掉表前缀

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain =true)setter链式操作

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符

        mpg.setStrategy(strategy);

        // 6、执行
        mpg.execute();
    }
}
```



#### 13.2.3 编写讲师Controller层代码

```java
//region 服务
    /**
     * 讲师服务
     */
    @Autowired
    private EduTeacherService eduTeacherService;
    //endregion

    //region 查询讲师表所有数据

    /**
     * 查询讲师表所有数据
     */
    @GetMapping("findAll")
    public List<EduTeacher> findAllTeacherList() {
        //调用service层的方法，实现查询所有的操作
        List<EduTeacher> list = eduTeacherService.list(null);
        return list;
    }
    //endregion
```



#### 13.2.4 创建启动类

```java
package com.ama.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 2022/7/27 0:13
 *
 * @Description
 * @Author WangWenZhe
 */
@SpringBootApplication
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
```



#### 13.2.5 配置类

```java
package com.ama.eduservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 2022/7/27 0:16
 *
 * @Description
 * @Author WangWenZhe
 */
@Configuration
@MapperScan("com.ama.eduservice.mapper")
public class EduConfig {
}
```



#### 13.2.7 配置统一返回的json时间格式

默认情况下json时间格式带有时区，并且是世界标准时间，和我们的时间差了八个小时

在application.properties中设置

```properties
#返回json的全局时间格式
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
```



#### 13.2.8 讲师逻辑删除功能

在EduConfig类中配置逻辑删除插件

```java
    /**
     * 逻辑删除插件
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
```

修改实体类，添加注解@TableLogic

```java
@ApiModelProperty(value = "逻辑删除 1(true) 已删除，0(false) 未删除")
@TableLogic
private Integer isDeleted;
```

在Contoller层编写删除方法

```java
@DeleteMapping("{id}")
    public boolean deleteTeacherById(@PathVariable String id) {
        //调用service层的方法，实现逻辑删除操作
        boolean flag = eduTeacherService.removeById(id);
        return flag;
    }
```



## 14. Swagger2

前后端分离开发模式中，api文档是最好的沟通方式。

Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。

1. 及时性 (接口变更后，能够及时准确地通知相关前后端开发人员) 
2. 规范性 (并且保证接口的规范性，如接口的地址，请求方式，参数及响应格式和错误信息) 

3. 一致性 (接口信息一致，不会出现因开发人员拿到的文档版本不一致，而出现分歧) 

4. 可测性 (直接在接口文档上进行测试，以方便理解业务)



### 14.1 创建common模块

选中guli_parent项目，创建一个与service同级的项目，common。

![image-20220727120252031](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220727120252031.png)

![image-20220727120357118](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220727120357118.png)

修改pom文件，增加依赖。

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <scope>provided</scope>
        </dependency>
        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <scope>provided</scope>
        </dependency>

        <!--lombok用来简化实体类：需要安装lombok插件-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>

        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <scope>provided</scope>
        </dependency>

        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- spring2.X集成redis所需common-pool2
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.6.0</version>
        </dependency>-->
    </dependencies>
```

然后再加上<packaging>pom</packaging>，删除common项目中的src文件夹。



### 14.2 创建service_base模块

在common模块下创建子模块service_base，并创建配置类SwaggerConfig。

```java
package com.ama.servicebase.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 2022/7/27 12:17
 *
 * @Description Swagger配置类
 * @Author WangWenZhe
 */
@Configuration
@EnableSwagger2//Swagger注解
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }

    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .title("网站-课程中心API文档")
                .description("本文档描述了课程中心微服务接口定义")
                .version("1.0")
                .contact(new Contact("Helen", "http://atguigu.com",
                        "55317332@qq.com"))
                .build();
    }
}
```

在service项目中修改pom文件，引入service_base的依赖。

```xml
        <dependency>
            <groupId>com.ama</groupId>
            <artifactId>service_base</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```

修改service_edu的启动类，增加注解@ComponentScan，项目启动时，使其能够扫描到bean。

```java
@ComponentScan(basePackages = {"com.ama"})
```



### 14.3 启动测试

启动service_edu项目，访问地址http://localhost:8001/swagger-ui.html

![image-20220727201847088](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220727201847088.png)



### 14.4 定义接口说明和参数说明

定义在类上：@Api

定义在方法上：@ApiOperation

定义在参数上：@ApiParam

```java
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduService/teacher")
public class EduTeacherController {
```

```java
/**
     * 查询讲师表所有数据
     */
    @ApiOperation(value = "查询讲师表所有数据")
    @RequestMapping("findAll")
    public List<EduTeacher> findAllTeacherList() {
        //调用service层的方法，实现查询所有的操作
        List<EduTeacher> list = eduTeacherService.list(null);
        return list;
    }
    //endregion

    //region 逻辑删除

    /**
     * 逻辑删除
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public boolean deleteTeacherById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        //调用service层的方法，实现逻辑删除操作
        boolean flag = eduTeacherService.removeById(id);
        return flag;
    }

    //endregion
```



### 14.5 总结

**步骤总结：**

1. 在父工程guli_parent下创建一个子模块，common项目。并修改pom文件，引入依赖。同时删除common项目下的src文件。
2. 在common项目下，再创建一个子模块，service_base项目。同时在service_base项目内新建SwaggerConfig类。
3. 在service项目内，修改pom文件，引入service_base依赖。
4. 修改service_edu项目的启动类，增加注解@ComponentScan，使其启动时能够扫描到bean。



## 15. 统一返回数据格式

项目中我们会将响应封装成json返回，一般我们会将所有接口的数据格式统一， 使前端(iOS Android,Web)对数据的操作更一致、轻松。

一般情况下，统一返回数据格式没有固定的格式，只要能描述清楚返回的数据状态以及要返回的具体数据就可以。但是一般会包含状态码、返回消息、数据这几部分内容

例如，我们的系统要求返回的基本数据格式如下：

```json
{
    "success":true,
    "code":20000,
    "message":"成功",
    "data":{
        "items":[
            {
                "id":"1",
                "name":"刘德华",
                "intro":"毕业于师范大学数学系，热爱教育事业，执教数学思维6年有余"
            }
        ]
    }
}
```

**分页：**

```json
{
    "success":true,
    "code":20000,
    "message":"成功",
    "data":{
        "total":17,
        "rows":[
            {
                "id":"1",
                "name":"刘德华",
                "intro":"毕业于师范大学数学系，热爱教育事业，执教数学思维6年有余"
            }
        ]
    }
}
```

**没有返回数据：**

```json
{
    "success":true,
    "code":20000,
    "message":"成功",
    "data":{

    }
}
```

**失败：**

```json
{
    "success":false,
    "code":20001,
    "message":"失败",
    "data":{

    }
}
```

**因此，我们定义统一结果：**

```json
{
    "success":"布尔",//响应是否成功
    "code":"数字",//响应码
    "message":"字符串",//返回消息
    "data":"HashMap"//返回数据，放在键值对中
}
```



### 15.1 创建common_utils模块

#### 15.1.1 创建子模块

在common模块下创建子模块common_utils

![image-20220727195220346](http://typora-imagelist.oss-cn-qingdao.aliyuncs.com/image-20220727195220346.png)



#### 15.1.2 创建接口定义返回码

```java
package com.ama.commonutis.config;

public interface ResultCode {
    public static final Integer SUCCESS = 20000;
    public static final Integer ERROR = 20001;
}
```



#### 15.1.3 定义返回数据格式

```java
package com.ama.commonutis.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 2022/7/27 20:00
 *
 * @Description 返回数据格式
 * @Author WangWenZhe
 */
@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    private R() {
    }

    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
```



### 15.2 统一返回结果使用

#### 15.2.1 引入依赖

在service模块中添加依赖

```xml
        <dependency>
            <groupId>com.ama</groupId>
            <artifactId>common_utils</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```



#### 15.2.2 修改Controller中的返回结果

```java
//region 查询讲师表所有数据

    /**
     * 查询讲师表所有数据
     */
    @ApiOperation(value = "查询讲师表所有数据")
    @GetMapping("findAll")
    public R findAllTeacherList() {
        //调用service层的方法，实现查询所有的操作
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
    }
    //endregion

    //region 逻辑删除

    /**
     * 逻辑删除
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacherById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        //调用service层的方法，实现逻辑删除操作
        boolean flag = eduTeacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //endregion
```



## 16. 讲师分页功能

### 16.1 配置分页插件

在service_edu模块中的EduConfig配置分页插件

```java
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
```



### 16.2 编写分页接口

```java
/**
     * 分页查询所有讲师
     *
     * @param current 当前页
     * @param limit   每页显示的条数
     */
    @ApiOperation(value = "分页查询所有讲师")
    @GetMapping("pageTeacherList/{current}/{limit}")
    public R pageTeacherList(@PathVariable long current, @PathVariable long limit) {
        //创建page对象
        Page<EduTeacher> pageTeacherList = new Page<>(current, limit);
        //调用service层的方法，实现分页查询所有的操作
        //调用方法的时候，底层封装，把分页所有数据封装到pageTeacherList对象里面
        eduTeacherService.page(pageTeacherList, null);
        long total = pageTeacherList.getTotal();
        List<EduTeacher> records = pageTeacherList.getRecords();
        //第一种方式：返回的是一个map集合
//        Map map = new HashMap<>();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);
        //第二种方式：返回的是一个pojo对象
        return R.ok().data("total", total).data("rows", records);
    }
```



### 16.3 多条件组合分页查询

#### 16.3.1 创建讲师查询的实体类

```java
package com.ama.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2022/7/27 21:07
 *
 * @Description 讲师多条件查询
 * @Author WangWenZhe
 */
@Data
public class TeacherQuery {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
}
```



#### 16.3.2 编写多条件组合分页查询接口

```java
    //region 多条件组合分页查询

	/**
     * 多条件组合分页查询
     *
     * @param current 当前页
     * @param limit   每页显示的条数
     */
    @ApiOperation(value = "多条件组合分页查询")
    @PostMapping("pageTeacherListByCondition/{current}/{limit}")
    public R pageTeacherListByCondition(@PathVariable long current, @PathVariable long limit,
                                        @RequestBody(required = false) TeacherQuery teacherQuery) {
        //创建page对象
        Page<EduTeacher> pageTeacherList = new Page<>(current, limit);
        //构造条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空，拼接条件
        if (!StringUtils.isEmpty(name)) {
            //拼接条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            //拼接条件
            wrapper.like("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            //拼接条件
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(begin)) {
            //拼接条件
            wrapper.le("gmt_modified", end);
        }

        //调用service层的方法，实现多条件组合分页查询所有的操作
        eduTeacherService.page(pageTeacherList, wrapper);
        long total = pageTeacherList.getTotal();//总记录数
        List<EduTeacher> records = pageTeacherList.getRecords();//数据list集合
        return R.ok().data("total", total).data("rows", records);
    }
    //endregion
```



## 17. 
