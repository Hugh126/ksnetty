package com.hugh.ksnetty.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程池支持的SocketServer
 * <p>
 * 1、启动线程池
 * 2、创建SocketServer
 * 3、创建Thread, accept, loop -> input -> handle
 * <p>
 * 1）Try Exception 2）close Resource
 * <p>
 * <p>
 * 总结：
 * 1、会阻塞两次，一次server的accept，一次read客户端输入
 * <p>
 * <p>
 * 技巧：
 * 可以同telnet连接，很直观很方便
 * telnet 127.0.0.1 7000
 * 进入后
 * Ctrl + ]
 * send  hello,bio
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        Executor executor = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(7000);

        System.out.println("服务器已经启动了");

        while (true) {

            System.out.println("等待连接……");

            // 这里会blocking!
            Socket socket = serverSocket.accept();

            System.out.println("连接到一个客户端");

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    handle(socket);
                }
            });

        }
    }

    private static void handle(Socket socket) {
        try {
            // 打印当前线程信息
            System.out.print("当前线程ID=" + Thread.currentThread().getId());
            System.out.println("当前线程名称=" + Thread.currentThread().getName());

            // 通过socket获取输入流
            System.out.println("reading……");
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];

            // 循环读取客户端发生数据
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    String acceptStr = new String(bytes, 0, read);

                    System.out.println("收到客户端请求：" + acceptStr);

                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getId() + "线程连接断开");
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
