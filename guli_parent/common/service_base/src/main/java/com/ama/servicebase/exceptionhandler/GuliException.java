package com.ama.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2022/9/14 21:57
 * 自定义异常
 *
 * @Description
 * @Author WangWenZhe
 */
@Data
@AllArgsConstructor//生成有参数构造方法
@NoArgsConstructor//生成无参数构造方法
public class GuliException extends RuntimeException {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String msg;

}
