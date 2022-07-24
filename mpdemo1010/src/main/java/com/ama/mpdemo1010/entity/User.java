package com.ama.mpdemo1010.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 2022/7/20 20:24
 *
 * @Description
 * @Author WangWenZhe
 */
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
}
