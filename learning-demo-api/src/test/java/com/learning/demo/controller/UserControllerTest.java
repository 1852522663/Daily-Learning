package com.learning.demo.controller;

import com.learning.demo.LearningDemoApplication;
import com.learning.demo.model.ResponseResult;
import com.learning.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-07-06
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearningDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {


    @Autowired
    private UserController userController;

    @Test
    void add() {
        User user = new User();
        user.setName("王武");
        user.setSex("男");
        user.setPwd("1233");
        user.setEmail("3413@qq.com");
        ResponseResult add = userController.add(user);
        System.out.println(add);
    }

    @Test
    void list() {
        System.out.println(userController.list(new User()).getData());
    }

    @Test
    void listPage() {
        System.out.println(userController.listPage(0, 3, new User()).getData());
    }

    @Test
    void update() {
        User user = new User();
        user.setId(157);
        user.setName("王武1");
        user.setSex("男");
        user.setPwd("1233");
        user.setEmail("3413@qq.com");
        System.out.println(userController.update(user));
    }

    @Test
    void delete() {
        System.out.println(userController.delete(156));
    }

    @Test
    void deleteById() {
        System.out.println(userController.deleteById(156));
    }
}