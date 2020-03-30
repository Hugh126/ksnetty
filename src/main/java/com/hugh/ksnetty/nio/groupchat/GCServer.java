package com.hugh.ksnetty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author hugh
 *
 * 服务端启动并监听
 * 处理事件
 * 消息转发
 */
public class GCServer {

    private Selector selector;
    // 专门负责监听的
    private ServerSocketChannel listenChannel;
    private static final int PORT = 7777;

    // init
    public GCServer() {
        try {
            this.selector = Selector.open();
            this.listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // non-block
            listenChannel.configureBlocking(false);
            // register to selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务端启动完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听，并处理 连接、读写事件
    private void listen() {
        System.out.println("服务端开始监听...");
        // 循环 监听是否有事件发生
        try {
            while (true){
                int select = selector.select(2000L);
                if(select == 0) {
                    // 提示无意义
//                    System.out.println("等待中...");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    // 处理连接事件
                    if(key.isAcceptable()) {
                        // 获取通道
                        SocketChannel socketChannel = listenChannel.accept();
                        // 非阻塞
                        socketChannel.configureBlocking(false);

                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println(socketChannel.getRemoteAddress() + "上线了");
                    }

                    // 处理读事件
                    if(key.isReadable()) {
                        readData(key);
                    }

                    iterator.remove();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        // 从key中使用attachment取出buffer为空，暂时也不知道为啥
        // 换种写法
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try {
            int read = channel.read(byteBuffer);
            if(read > 0) {
                String msg = new String(byteBuffer.array()).trim();
                System.out.println(msg);
                sendMsgToOtherClient(msg, channel);
            }

        } catch (IOException | NullPointerException e) {
//            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                key.cancel();
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void sendMsgToOtherClient(String msg, SocketChannel selfChannel) throws IOException {
        System.out.println("服务器转发消息中...");
        for(SelectionKey selectionKey : selector.keys()) {
            Channel targetChannel = selectionKey.channel();
            if(targetChannel instanceof  SocketChannel && targetChannel != selfChannel) {
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg转存到buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                // 写入channel
                dest.write(byteBuffer);
            }
        }
    }


    public static void main(String[] args) {
        GCServer gcServer = new GCServer();
        gcServer.listen();
    }

}
