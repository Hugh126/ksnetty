package com.hugh.ksnetty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author hugh
 *
 * 与服务端区别：
 * 1. Bootstrap
 * 2. NioSocketChannel
 *
 */
public class NettyClient {

    public static void main(String[] args) {

        // 创建事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建启动对象
        Bootstrap bootstrap = new Bootstrap();

        // 启动客户端连接服务端
        try {

            // 设置相关参数
            bootstrap.group(eventLoopGroup)     // 设置线程组
                    .channel(NioSocketChannel.class)        // 客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandle());
                        }
                    });     // 配置业务handle

            System.out.println("客户端OK了。。。。");

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }


    }
}
