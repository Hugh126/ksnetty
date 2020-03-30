package com.hugh.ksnetty.nio.component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author hugh
 * <p>
 * 实现功能：
 * 拷贝文件
 * <p>
 * <p>
 * #一次性读取#
 * ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
 * component.read(byteBuffer);
 * <p>
 * #下面为边度编写实现#
 * <p>
 * <p>
 * 可以支持在内存中读写文件 MappedByteBuffer
 */
public class BasicFileChannel02 {


    private static String fileName = "testFileChannel01.txt";


    public static void main(String[] args) throws FileNotFoundException {

        // 创建文件输入流
        File file = new File(fileName);
        FileInputStream inputStream = new FileInputStream(file);

        // 创建管道
        FileChannel channel = inputStream.getChannel();

        System.out.println("file size=" + file.length());

        // 创建另一个文件的输出流
        String outFileName = "testFile_COPY2.txt";
        File file2 = new File(outFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);

        // 获取channel
        FileChannel channel2 = fileOutputStream.getChannel();

        // 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        while (true) {
            try {
                // 读取管道中数据，读取完成后会放到buffer
                int read = channel.read(byteBuffer);

                if (read == -1) {
                    break;
                }

                // 读写转换
                byteBuffer.flip();

                channel2.write(byteBuffer);

                // 清除buffer标志位
                byteBuffer.clear();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * 关闭输入 输出流
         */
        try {
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
