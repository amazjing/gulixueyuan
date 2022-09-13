package com.ama.servicebase.exceptionhandler;

import com.ama.commonutis.config.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 2022/9/13 22:31
 * 统一处理异常
 * @Description
 * @Author WangWenZhe
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //指定出现什么异常

    @ExceptionHandler(Exception.class)
    @ResponseBody//为了返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理...");
    }

}
