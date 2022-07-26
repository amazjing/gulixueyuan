package com.ama.mpdemo1010;

import com.ama.mpdemo1010.entity.User;
import com.ama.mpdemo1010.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
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

    /**
     * 添加操作
     */
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

    /**
     * 修改操作
     */
    @Test
    void updateUser() {
        User user = new User();
        user.setId(2L);
        user.setAge(31);
        int row = userMapper.updateById(user);
        System.out.println(row);//影响的行数
    }

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

    /**
     * 多个id的查询
     */
    @Test
    void selectByIds() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));
        System.out.println(users);
    }

    /**
     * 根据条件查询
     */
    @Test
    void selectByMap() {
        //根据条件查询
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "world");
        map.put("age", 31);
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
    }

    /**
     * 分页查询
     */
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

    /**
     * 物理删除 根据id删除
     */
    @Test
    void deleteById() {
        int row = userMapper.deleteById(1550420141828943877L);
        System.out.println(row);
    }

    /**
     * 物理删除 根据id批量删除
     */
    @Test
    void deleteByIds() {
        int row = userMapper.deleteBatchIds(Arrays.asList(1550420141828943875L, 1550420141828943874L));
        System.out.println(row);
    }

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

    /**
     * 使用wrapper查询
     */
    @Test
    void selectByWrapper() {
        //创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //添加条件
        queryWrapper.eq("name", "world");
        //查询
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }

    /**
     * 使用wrapper查询age大于等于24的用户
     */
    @Test
    void selectByAge() {
        //创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //添加条件
        queryWrapper.ge("age", 24);
        //查询
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }

    /**
     * 使用wrapper查询age在24和31之间的用户
     */
    @Test
    void selectByAgeBetween() {
        //创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //添加条件
        queryWrapper.between("age", 24, 31);
        //查询
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }

    /**
     * 使用wrapper中的last查询age最大的用户
     */
    @Test
    void selectByAgeLast() {
        //创建查询对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //添加条件
        queryWrapper.last("and age = (select max(age) from user)");
        //查询
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }

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

}
