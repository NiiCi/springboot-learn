package com.spring.lambda.dao.impl;

import com.spring.lambda.dao.Action;
import com.spring.lambda.dao.Editor;

public class Close implements Action {
    private final Editor editor;

    public Close(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void perform() {
        editor.close();
    }
}
