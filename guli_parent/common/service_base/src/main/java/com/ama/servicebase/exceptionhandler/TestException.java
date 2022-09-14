package com.ama.servicebase.exceptionhandler;

/**
 * 2022/9/14 22:03
 * 测试异常
 *
 * @Description
 * @Author WangWenZhe
 */
public class TestException {
    public static void main(String[] args) {
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            throw new GuliException(20001, "执行了自定义异常处理...");
        }
    }
}
