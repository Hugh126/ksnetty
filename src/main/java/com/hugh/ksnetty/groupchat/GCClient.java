package com.hugh.ksnetty.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author hugh
 *
 * 连接服务端
 * 处理读、写消息
 */
public class GCClient {

    // 如何客户端是多通道，需要的
    private Selector selector;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 7777;
    private SocketChannel socketChannel;
    // 定义一个客户端名称
    private String username;

    public GCClient() throws IOException {
        this.selector = Selector.open();
        socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
        this.socketChannel.configureBlocking(false);

        this.socketChannel.register(selector, SelectionKey.OP_READ);
        this.username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok ...");
    }

    // 发送消息
    public void sendMsg(String info) {
        info = username + "说:" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取消息
    public void readMsg() {
        try {
//            int select = selector.select(1000);
            int select = selector.select();
            if(select == 0) {
//                System.out.println("暂时没有可用通道...");
            }else {

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if(key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        // 由于客户端消息输入是按行输入的
                        System.out.println(msg.trim());
                    }

                    // 不要重复操作
                    iterator.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GCClient client = new GCClient();

        // 循环读取消息
        new Thread(() -> {
            for(;;){
                client.readMsg();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 控制台输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            client.sendMsg(s);
        }
    }

}
