package com.spring.lambda.dao;

@FunctionalInterface
public interface UserLambdaDao {
    public boolean actionPerformed(String username);
}
