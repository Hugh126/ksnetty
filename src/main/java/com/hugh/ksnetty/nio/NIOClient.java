package com.hugh.ksnetty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author hugh
 */
public class NIOClient {


    public static void main(String[] args) throws IOException {
        // 得到一个 SocketChannel
        SocketChannel socketChannel = SocketChannel.open();

        // 非阻塞
        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6999);

        boolean status = socketChannel.connect(inetSocketAddress);
        System.out.println("socket连接及时返回状态=" + status);

        while (!socketChannel.finishConnect()) {
            System.out.println("连接未完成，等会儿...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("连接到服务端=" + socketChannel.isConnected());

        String str = "hello nio server.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);

        // 让客户的IO阻塞
        System.in.read();

    }
}
