package com.spring.lambda.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * 包含操作序列的宏对象,可按顺序执行操作
 */
public class Macro {
    private final List<Action> actionList;

    public Macro() {
        this.actionList = new ArrayList<>();
    }

    public void record(Action action){
        actionList.add(action);
    }

    public void run(){
        actionList.forEach(Action::perform);
    }
}
