package com.spring.lambda.dao;

/**
 * open save 这样的操作成为命令,我们需要一个统一的接口来概括这些不同的操作
 * 我们将这个接口成为 Action , 代表了一个操作,所有命令都要实现该接口
 */
public interface Editor {
    public void save();

    public void open();

    public void close();
}
