package com.spring.lambda.dao.impl;

import com.spring.lambda.dao.CompressionStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gzip 算法压缩数据实现
 */
public class GZipCompressionStrategyImpl implements CompressionStrategy {
    @Override
    public OutputStream compress(OutputStream data) throws IOException {
        // gzip 算法实现
        return new GZIPOutputStream(data);
    }
}
