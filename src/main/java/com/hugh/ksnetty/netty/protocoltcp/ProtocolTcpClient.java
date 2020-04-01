package com.hugh.ksnetty.netty.protocoltcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * @author hugh
 */
public class ProtocolTcpClient {

    public static void main(String[] args) {

        NioEventLoopGroup workGroups = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workGroups)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            /**
                             *  口袋要放在前面， 业务handler放后面， 反了不行
                             */

                            pipeline.addLast(new ProtocolMsgEncoder());
                            pipeline.addLast(new ProtocolMsgDecoder());

                            pipeline.addLast(new ProtocolTcpClientHandler());

                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 7000).sync();
            System.out.println("客户端OK");
            channelFuture.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroups.shutdownGracefully();
        }

    }

}
