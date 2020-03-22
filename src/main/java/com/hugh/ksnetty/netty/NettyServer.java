package com.hugh.ksnetty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author hugh
 *
 * 示意流程
 * 1、创建两个线程组
 * 2、创建启动对象并配置参数， 设置业务handle
 * 3、绑定端口并同步，监听关闭
 *
 *
 * [[模型解读]]
 *
 * Netty 抽象出两组线程池，BossGroup 专门负责接收客户端连接，WorkerGroup 专门负责网络读写操作。
 * 1,NioEventLoop 表示一个不断循环执行处理任务的线程，每个 NioEventLoop 都有一个 selector，用于监听绑定在其上的 socket 网络通道。
 * 2, NioEventLoop 内部采用串行化设计，从消息的读取->解码->处理->编码->发送，始终由 IO 线程 NioEventLoop 负责
 *
 * NioEventLoopGroup 下包含多个 NioEventLoop
 * 	>每个 NioEventLoop 中包含有一个 Selector，一个 taskQueue
 * 	>每个 NioEventLoop 的 Selector 上可以注册监听多个 NioChannel
 * 	>每个 NioChannel 只会绑定在唯一的 NioEventLoop 上
 * 	>每个 NioChannel 都绑定有一个自己的 ChannelPipeline
 *
 */
public class NettyServer {

    public static void main(String[] args) {

        // 1、处理连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 2、处理业务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 服务端启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try{

            // 参数配置
            serverBootstrap.group(bossGroup, workerGroup)   // 设置两个线程组
                    .channel(NioServerSocketChannel.class)  // 通道实现类型
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //  保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 设置workGroup 的EventLoop 对应管道的 处理器(可以是 Netty提供，或自定义的)

                        // pipeline 与 channel 是相互包含的！
                        // 给pipeline设置handle
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            System.out.println("每个用户的socketChannel 不一样的 = " + socketChannel.hashCode());

                            socketChannel.pipeline().addLast(new NettyServerHandle());
                        }
                    }); // 设置workGroup 的EventLoop 对应管道的 处理器(可以是 Netty提供，或自定义的)


            System.out.println("服务器已经准备好了。。。。。。");

            // 绑定一个端口并且同步
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();

            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // shutdownGracefully
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
