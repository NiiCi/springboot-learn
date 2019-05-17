package com.spring.lambda.dao;

public interface Carriage {
    public default String rock() {
        return "... from side to side";
    }
}
