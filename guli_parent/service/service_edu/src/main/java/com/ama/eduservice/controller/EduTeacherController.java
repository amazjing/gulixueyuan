package com.ama.eduservice.controller;


import com.ama.eduservice.entity.EduTeacher;
import com.ama.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author ama
 * @since 2022-07-26
 */
@RestController
@RequestMapping("/eduService/teacher")
public class EduTeacherController {

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
    @DeleteMapping("{id}")
    public boolean deleteTeacherById(@PathVariable String id) {
        //调用service层的方法，实现逻辑删除操作
        boolean flag = eduTeacherService.removeById(id);
        return flag;
    }

    //endregion
}

