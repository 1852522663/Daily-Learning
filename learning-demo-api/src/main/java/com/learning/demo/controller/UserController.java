package com.learning.demo.controller;

import com.learning.demo.model.HttpStatusEnum;
import com.learning.demo.model.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.demo.model.ResponseResult;
import com.learning.demo.model.User;
import com.learning.demo.service.UserService;


/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-07-05
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("add")
    public ResponseResult add(User user) {
        user.setStatus(1);
        boolean save = userService.save(user);
        if (!save) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),save);
        }
        return ResponseResult.success();
    }

    @GetMapping("list")
    public ResponseResult list(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(user.getName())) {
            queryWrapper.like("name", user.getName());
        }
        if (user.getStatus() != null) {
            queryWrapper.eq("status", user.getStatus());
        } else {
            queryWrapper.eq("status", 1);
        }
        return ResponseResult.success(userService.list(queryWrapper));
    }

    @GetMapping("listPage")
    public ResponseResult listPage(Integer pageNo, Integer pageSize, User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(user.getName())) {
            queryWrapper.like("name", user.getName());
        }
        if (user.getStatus() != null) {
            queryWrapper.eq("status", user.getStatus());
        } else {
            queryWrapper.eq("status", 1);
        }
        Page page = new Page<>(pageNo, pageSize);
        IPage<User> pageInfo = userService.page(page, queryWrapper);
        return ResponseResult.success(pageInfo);
    }

    @RequestMapping("update")
    public ResponseResult update(User user) {
        boolean update = userService.updateById(user);
        if (!update) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),update);
        }
        return ResponseResult.success();
    }

    @RequestMapping("delete")
    public ResponseResult delete(Integer id) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        User user = new User();
        user.setStatus(0);
        boolean update = userService.update(user, updateWrapper);
        if (!update) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),update);
        }
        return ResponseResult.success();
    }

    @RequestMapping("deleteById")
    public ResponseResult deleteById(Integer id) {
        boolean removeById = userService.removeById(id);
        if (!removeById) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),removeById);
        }
        return ResponseResult.success();
    }

}
