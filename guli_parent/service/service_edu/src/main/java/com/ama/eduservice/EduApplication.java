package com.ama.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 2022/7/27 0:13
 *
 * @Description
 * @Author WangWenZhe
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ama"})
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
