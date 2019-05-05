package com.spring.shiro.dao;

import com.spring.shiro.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    public String selectPasswordByUsername(String username);
}
