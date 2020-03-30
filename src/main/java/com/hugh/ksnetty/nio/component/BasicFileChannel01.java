package com.hugh.ksnetty.nio.component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author hugh
 * <p>
 * 主要方法 :
 * read  从通道读取数据，并放入buffer
 * write 把buffer数据写到channel
 * transferTo 把数据从当前通道复制到目标通道
 * transferFrom
 * <p>
 * 实现功能：
 * 写入文件A，然后从文件A读取内容
 */
public class BasicFileChannel01 {

    private static String fileName = "testFileChannel01.txt";
    private static String str = "I MISS U.";

    public static void outputToFile() throws FileNotFoundException {

        // 创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        // 创建FileChannel
        FileChannel channel = fileOutputStream.getChannel();

        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // buffer中写入数据
        byteBuffer.put(str.getBytes());

        // 对buffer数据读取到channel，需要读写转换
        byteBuffer.flip();

        // buffer数据读取，写入channel
        try {
            channel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {

//        outputToFile();

        inputFromFile();
    }

    private static void inputFromFile() throws FileNotFoundException {

        // 创建文件输入流
        File file = new File(fileName);
        FileInputStream inputStream = new FileInputStream(file);

        FileChannel channel = inputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将通道（输入流）数据读入buffer
        try {
            channel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将buffer数据转为String
        System.out.println(new String(byteBuffer.array()));

        // 关闭输入流
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
