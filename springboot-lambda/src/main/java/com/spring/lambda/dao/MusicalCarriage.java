package com.spring.lambda.dao;

public class MusicalCarriage implements Carriage,JukeBox{

    @Override
    public String rock() {
        return Carriage.super.rock();
    }
}
