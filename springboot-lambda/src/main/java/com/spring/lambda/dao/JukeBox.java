package com.spring.lambda.dao;

public interface JukeBox {
    public default String rock(){
        return "... all over the world!";
    }
}
