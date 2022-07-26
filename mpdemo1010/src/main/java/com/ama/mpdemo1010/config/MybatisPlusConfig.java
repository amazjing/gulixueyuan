package com.ama.mpdemo1010.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 逻辑删除插件
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

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
}
