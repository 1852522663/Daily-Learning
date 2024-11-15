package com.learning.demo.service.impl;


import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.demo.mapper.UserMapper;
import com.learning.demo.model.User;
import com.learning.demo.service.UserService;


/**
* @author yuehewei
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-07-05 19:41:27
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
