package com.spring.easypoi.dao;

import com.spring.easypoi.bean.User;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * MysqlMapper 用于批量插入
 */
public interface UserMapper extends Mapper<User>,IdListMapper<User,Integer>,MySqlMapper<User> {

}
