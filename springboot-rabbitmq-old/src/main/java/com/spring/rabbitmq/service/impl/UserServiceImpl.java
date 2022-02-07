package com.spring.rabbitmq.service.impl;

import com.spring.rabbitmq.service.UserService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    // Spring AMQP提供的‘template’扮演者关键的角色。定义主要操作的接口是AmqpTemplate。
    @Autowired
    private AmqpTemplate amqpTemplate;
}
