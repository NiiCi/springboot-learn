package com.spring.lambda.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 压缩类
 */
public class Compressor {
    /**
     * 引用接口  在调用构造函数时,传递接口
     * zip 和 gzip 都各自实现了 CompressionStrategy 接口,实现了 策略模式
     */
    private final CompressionStrategy compressionStrategy;

    public Compressor(CompressionStrategy compressionStrategy) {
        this.compressionStrategy = compressionStrategy;
    }

    public void compressor(Path inFile, File outFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(outFile);
        Files.copy(inFile, compressionStrategy.compress(outputStream));
    }
}
