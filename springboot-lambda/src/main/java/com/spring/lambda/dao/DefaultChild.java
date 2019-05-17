package com.spring.lambda.dao;

public interface DefaultChild extends DefalutParent {
    @Override
    default void welcome() {
        System.out.println("Child: hi!");
    }
}
