package com.hugh.ksnetty.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author hugh
 * @Title: NettyHttpServer
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2213:38
 */
public class NettyHttpServer {

    public static void main(String[] args) {

        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInit());

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();

            channelFuture.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
