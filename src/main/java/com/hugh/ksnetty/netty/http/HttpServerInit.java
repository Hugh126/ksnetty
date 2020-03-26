package com.hugh.ksnetty.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author hugh
 * @Title: HttpServerInit
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2213:42
 */
public class HttpServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // 向管道加入处理器
        ChannelPipeline pipeline = ch.pipeline();


        // 1. 新增解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        // 2. 新增handle
        pipeline.addLast("MyHandle", new HttpServerHandle());

        /**
         * 这里打断点，观察  ChannelPipeline 的双向链表结构，以及与 ChannelHandler 的包含关系
         */
        System.out.println("服务端已OK");

    }
}
