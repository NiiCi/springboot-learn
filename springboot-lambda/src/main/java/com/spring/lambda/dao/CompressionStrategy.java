package com.spring.lambda.dao;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 压缩数据的策略接口
 */
public interface CompressionStrategy {
    /**
     * 定义的压缩方法
     * @return
     */
    public OutputStream compress(OutputStream data) throws IOException;
}
