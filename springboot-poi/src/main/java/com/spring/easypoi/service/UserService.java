package com.spring.easypoi.service;

import com.spring.easypoi.bean.User;

import java.util.List;

public interface UserService {
    public List<User>  selectAllUser();

    public int saveUserList(List<User> userList);
}
