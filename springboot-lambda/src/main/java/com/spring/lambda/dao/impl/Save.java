package com.spring.lambda.dao.impl;

import com.spring.lambda.dao.Action;
import com.spring.lambda.dao.Editor;

/**
 * Action 接口的实现类
 * 这些类 要做的是调用 Editor 接口中的一个方法
 */
public class Save implements Action {
    private final Editor editor;

    public Save(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void perform() {
        editor.save();
    }
}
