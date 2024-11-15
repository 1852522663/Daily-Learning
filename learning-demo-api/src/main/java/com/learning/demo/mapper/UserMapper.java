package com.learning.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.demo.model.User;


/**
* @author yuehewei
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-07-05 19:41:27
* @Entity demo.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


}
