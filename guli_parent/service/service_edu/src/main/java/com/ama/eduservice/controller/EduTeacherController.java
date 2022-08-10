package com.ama.eduservice.controller;


import com.ama.commonutis.config.R;
import com.ama.eduservice.entity.EduTeacher;
import com.ama.eduservice.entity.vo.TeacherQuery;
import com.ama.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author ama
 * @since 2022-07-26
 */
@Api(description = "讲师管理")
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
    @ApiOperation(value = "查询讲师表所有数据")
    @GetMapping("findAll")
    public R findAllTeacherList() {
        //调用service层的方法，实现查询所有的操作
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
    }
    //endregion

    //region 分页查询讲师的方法

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

    //endregion

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


    //region 添加讲师接口方法

    /**
     * 添加讲师
     *
     * @param eduTeacher
     * @return
     */
    @ApiOperation(value = "新增讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.save(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //endregion

    //region 根据讲师id进行查询

    /**
     * 根据讲师id进行查询
     *
     * @param id 讲师id
     * @return
     */
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("getTeacherById/{id}")
    public R updateTeacher(@PathVariable String id) {
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    //endregion

    //region 根据讲师id进行修改

    /**
     * 根据讲师id进行修改
     *
     * @param eduTeacher
     * @return
     */
    @ApiOperation(value = "根据讲师id进行修改")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

