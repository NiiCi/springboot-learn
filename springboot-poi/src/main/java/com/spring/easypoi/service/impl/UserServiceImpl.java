package com.spring.easypoi.service.impl;

import com.spring.easypoi.bean.User;
import com.spring.easypoi.dao.UserMapper;
import com.spring.easypoi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAll();
    }

    @Override
    public int saveUserList(List<User> userList) {
        return userMapper.insertList(userList);
    }
}
