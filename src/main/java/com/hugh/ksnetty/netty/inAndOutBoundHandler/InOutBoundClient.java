package com.hugh.ksnetty.netty.inAndOutBoundHandler;

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
 * @Title: InOutBoundClient
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/3016:49
 */
public class InOutBoundClient {

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

                            pipeline.addLast(new MyLongToByteEncoder());

                            pipeline.addLast(new MyByteToLongDecoder());

                            pipeline.addLast(new MyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 7000).sync();
            System.out.println("客户端OK");

            Channel channel = channelFuture.channel();

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

}
