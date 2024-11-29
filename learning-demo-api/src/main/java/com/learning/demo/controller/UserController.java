package com.learning.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learning.demo.aop.LogExecutionTime;
import com.learning.demo.aop.LogOutput;
import com.learning.demo.event.EventPublisher;
import com.learning.demo.model.HttpStatusEnum;
import com.learning.demo.model.ResponseResult;
import com.learning.demo.model.User;
import com.learning.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-07-05
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @LogExecutionTime
    @RequestMapping("add")
    public ResponseResult add(User user) {
        user.setStatus(1);
        boolean save = userService.add(user);
        if (!save) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),save);
        }
        return ResponseResult.success();
    }

    @LogExecutionTime
    @LogOutput(value = "查询列表")
    @GetMapping("list")
    public ResponseResult list(User user) {
        return ResponseResult.success(userService.list(user));
    }

    @LogExecutionTime
    @GetMapping("listPage")
    public ResponseResult listPage(Integer pageNo, Integer pageSize, User user) {
        IPage<User> pageInfo = userService.listPage(pageNo, pageSize, user);
        return ResponseResult.success(pageInfo);
    }

    @LogExecutionTime
    @RequestMapping("update")
    public ResponseResult update(User user) {
        boolean update = userService.update(user);
        if (!update) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),update);
        }
        return ResponseResult.success();
    }

    @LogExecutionTime
    @RequestMapping("delete")
    public ResponseResult delete(Integer id) {
        boolean update = userService.delete(id);
        if (!update) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),update);
        }
        return ResponseResult.success();
    }

    @LogExecutionTime
    @RequestMapping("deleteById")
    public ResponseResult deleteById(Integer id) {
        boolean removeById = userService.deleteById(id);
        if (!removeById) {
            return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(),HttpStatusEnum.BAD_REQUEST.getMessage(),removeById);
        }
        return ResponseResult.success();
    }

}
