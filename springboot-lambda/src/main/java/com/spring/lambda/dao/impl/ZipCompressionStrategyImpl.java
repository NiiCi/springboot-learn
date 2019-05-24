package com.spring.lambda.dao.impl;

import com.spring.lambda.dao.CompressionStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip 算法 压缩数据实现
 */
public class ZipCompressionStrategyImpl implements CompressionStrategy {
    @Override
    public OutputStream compress(OutputStream data) throws IOException {
        // zip 算法实现
        return new ZipOutputStream(data);
    }
}
