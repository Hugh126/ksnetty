package com.hugh.ksnetty.netty.dubbo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hugh
 * @Title: NettyClient
 * @ProjectName ksnetty
 * @Description: 客户端常规启动
 * @date 2020/6/921:18
 */
public class NettyClient {

    private String host;
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandle clientHandle = null;


    /**
     * 基础不牢，晕乎乎
     *
     * @param serivceClass
     * @param providerName
     * @return
     */
    public Object getBean(final Class<?> serivceClass, final String providerName) {

        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serivceClass}, (proxy, method, args) -> {
                    if (clientHandle == null) {
                        initClient();
                    }
                    clientHandle.setParam(providerName + args[0]);

                    // 线程池 submit 查看API
                    return executor.submit(clientHandle).get();

                });
    }

    public void setPara(String params) {
        clientHandle.setParam(params);
    }


    private void initClient() {
        clientHandle = new NettyClientHandle();

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new StringDecoder());
                                pipeline.addLast(new StringEncoder());
                                pipeline.addLast(clientHandle);
                            }
                        }
                );

        try {
            bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
