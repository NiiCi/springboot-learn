package com.spring.lambda.dao;

public interface DefalutParent {
    public default  void welcome(){
        System.out.println("Parent: hi!");
    }
}
