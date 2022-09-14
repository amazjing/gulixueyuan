package com.ama.servicebase.exceptionhandler;

import com.ama.commonutis.config.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 2022/9/13 22:31
 * 统一处理异常
 *
 * @Description
 * @Author WangWenZhe
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 指定出现什么异常执行这个方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody//为了返回数据
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("执行了全局异常处理...");
    }

    /**
     * 特定异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody//为了返回数据
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理...");
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
