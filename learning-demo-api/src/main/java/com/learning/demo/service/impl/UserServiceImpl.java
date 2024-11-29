package com.learning.demo.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.demo.event.EventPublisher;
import com.learning.demo.mapper.UserMapper;
import com.learning.demo.model.HttpStatusEnum;
import com.learning.demo.model.ResponseResult;
import com.learning.demo.model.User;
import com.learning.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author yuehewei
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-07-05 19:41:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private EventPublisher eventPublisher;

    /**
     * 添加用户，默认用户状态为 1（激活状态）
     *
     * @param user 用户信息
     * @return 操作结果，true 表示成功，false 表示失败
     */
    @Override
    public boolean add(User user) {
        // 设置默认用户状态为激活状态
        user.setStatus(1);
        // 调用 MyBatis Plus 提供的 save 方法，保存用户
        return save(user);
    }

    /**
     * 获取用户列表，支持根据用户名和状态进行筛选
     *
     * @param user 查询条件封装类，包含用户名和状态
     * @return 用户列表
     */
    @Override
    public List<User> list(User user) {
        // 发布查询事件，示例中只是记录日志
        eventPublisher.publish("查询列表被监听");
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (user.getName() != null && !user.getName().isEmpty()) {
            queryWrapper.like("name", user.getName());
        }
        if (user.getStatus() != null) {
            queryWrapper.eq("status", user.getStatus());
        } else {
            // 默认状态为 1（激活状态）
            queryWrapper.eq("status", 1);
        }

        // 调用 MyBatis Plus 提供的 list 方法，返回符合条件的用户列表
        return list(queryWrapper);
    }

    /**
     * 分页查询用户列表，支持根据用户名和状态进行筛选
     *
     * @param pageNo 当前页码
     * @param pageSize 每页大小
     * @param user 查询条件封装类，包含用户名和状态
     * @return 分页查询结果
     */
    @Override
    public IPage<User> listPage(Integer pageNo, Integer pageSize, User user) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (user.getName() != null && !user.getName().isEmpty()) {
            queryWrapper.like("name", user.getName());
        }
        if (user.getStatus() != null) {
            queryWrapper.eq("status", user.getStatus());
        } else {
            // 默认状态为 1（激活状态）
            queryWrapper.eq("status", 1);
        }

        // 设置分页参数
        Page<User> page = new Page<>(pageNo, pageSize);
        // 调用 MyBatis Plus 提供的分页查询方法，返回分页信息
        return page(page, queryWrapper);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 操作结果，true 表示成功，false 表示失败
     */
    @Override
    public boolean update(User user) {
        // 调用 MyBatis Plus 提供的 updateById 方法，根据用户 ID 更新信息
        return updateById(user);
    }

    /**
     * 软删除用户，将用户的状态设为 0（禁用状态）
     *
     * @param id 用户ID
     * @return 操作结果，true 表示成功，false 表示失败
     */
    @Override
    public boolean delete(Integer id) {
        // 构建更新条件，设定要更新的字段为状态
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);

        // 创建用户对象，修改其状态为禁用状态
        User user = new User();
        user.setStatus(0);

        // 调用 MyBatis Plus 提供的 update 方法，根据条件进行软删除
        return update(user, updateWrapper);
    }

    /**
     * 根据 ID 删除用户，执行真正的删除操作
     *
     * @param id 用户ID
     * @return 操作结果，true 表示成功，false 表示失败
     */
    @Override
    public boolean deleteById(Integer id) {
        // 调用 MyBatis Plus 提供的 removeById 方法，根据用户 ID 执行删除操作
        return removeById(id);
    }
}
