package com.hugh.ksnetty.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author hugh
 *
 * 实现目的：
 * 1. 通过websocket编程，实现服务端、客户端长连接
 * 2、相互感知，双工通信
 *
 *
 */
public class WebSocketServer {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * 基于http协议, 来个编码解码器
                             */
                            pipeline.addLast(new HttpServerCodec());

                            /**
                             * 块的方式读写
                             */
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * http协议中数据是分段传输，需要聚合
                             * 参数：单次最大字节, 此处与chrome一致
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * 1、websocket的数据以帧（frame）的形式传输，参考WebSocketFrame的6个子类
                             * 2、WebSocketServerProtocolHandler 核心功能是将HTTP协议升级为WS协议，保持长连接
                             * 3、参数对应浏览器请求url (ws://localhost:7000/hello)
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义业务handle
                            pipeline.addLast(new WebSocketServerHandle());

                        }
                    });

            System.out.println("websocket Server is  OK.");

            ChannelFuture channelFuture = serverBootstrap.bind(7777).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
