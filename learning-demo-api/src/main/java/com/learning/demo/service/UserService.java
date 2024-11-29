package com.learning.demo.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.demo.model.ResponseResult;
import com.learning.demo.model.User;

import java.util.List;

/**
* @author yuehewei
* @description 针对表【user】的数据库操作Service
* @createDate 2023-07-05 19:41:27
*/

public interface UserService extends IService<User> {
    public boolean add(User user);
    public List<User> list(User user);
    public IPage<User> listPage(Integer pageNo, Integer pageSize, User user);
    public boolean update(User user);
    public boolean delete(Integer id);
    public boolean deleteById(Integer id);
}

