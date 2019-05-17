package com.spring.lambda.dao;

import lombok.extern.log4j.Log4j2;

@FunctionalInterface
public interface UserLambdaDao {
    public boolean actionPerformed(String username);

    public static void test(){
        System.out.println("test");
    }
}
