package com.hugh.ksnetty.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author hugh
 * @Title: NettyGCClient
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2616:08
 */
public class NettyGCClient {

    private final static int port = 7000;
    private final static String host = "localhost";

    public void run() {

        NioEventLoopGroup workGroups = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workGroups)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("client-encoder", new StringEncoder());
                            pipeline.addLast("client-decoder", new StringDecoder());
                            // 业务handle
                            pipeline.addLast(new NettyGCClientHandle());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            /**
             *  客户端发送信息窗口
             *  scanner -> msg ->channel
             */
            Channel channel = channelFuture.channel();
            System.out.println("----" + channel.localAddress() + "-----");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\n");
            }

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroups.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyGCClient().run();
    }
}
