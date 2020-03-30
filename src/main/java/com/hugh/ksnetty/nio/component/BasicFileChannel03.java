package com.hugh.ksnetty.nio.component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author hugh
 * <p>
 * transferFrom
 * <p>
 * 文件拷贝简化版本
 */
public class BasicFileChannel03 {

    private static String fileName = "testFileChannel01.txt";
    private static String outFile3 = "testFile_COPY3.txt";

    public static void main(String[] args) throws IOException {

        try (
            FileInputStream fileInputStream = new FileInputStream(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile3);
        ) {
            FileChannel channel1 = fileInputStream.getChannel();
            FileChannel channel2 = fileOutputStream.getChannel();
            channel2.transferFrom(channel1, 0, channel1.size());
        }


    }
}
