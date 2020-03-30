package com.hugh.ksnetty.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hugh
 * <p>
 * 1、ServerSocketChannel 得到 SocketChannel =>  ServerSocket
 * 2、SocketChannel 注册到 Selector，返回SelectionKey会与 Selector 关联
 * 3、Selector监听select方法，返回事件发生的通道
 * 4、遍历SelectionKey，反向通过channel()获取SocketChannel
 * 5、通过Channel，处理事件
 *
 *
 *
 * 总结：
 * 1、 - ServerSocketChannel用于服务端监听新客户端的Socket连接
 *     - SocketChannel 网络IO通道，具体负责读写操作
 *
 *
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {

        // 得到 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到 Selector
        Selector selector = Selector.open();

        // bind and listen
        serverSocketChannel.socket().bind(new InetSocketAddress(6999));

        // config non-block
        serverSocketChannel.configureBlocking(false);

        // register selector，监听连接事件发生
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 操作selector => SelectionKey => component
            if (selector.select(1000L) == 0) {
                System.out.println("没有事件发生");
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();

                if (selectionKey.isAcceptable()) {

                    // 通过服务端 ServerSocketChannel 拿到当前监听到有事件发生的客户端 SocketChannel
                    // 这个客户端 SocketChannel 也需要注册到 Selector, 监听读事件，关联一个buffer
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    System.out.println("有客户端连接 socketChannel=" + socketChannel.hashCode());

                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) {
                    System.out.println("有读写事件发生");
                    // 通过 selectedKey反向获取 SocketChannel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    // 通过 selectedKey 获取 component 对应的 buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("socket component = " + channel.hashCode());
                    System.out.println("接受到消息" + new String(byteBuffer.array()));
                }

                // 防止重复操作，去掉已经操作 SelectionKey
                selectionKeyIterator.remove();
            }
        }

    }
}
