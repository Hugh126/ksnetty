package com.hugh.ksnetty.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author hugh
 * @Title: NettyGCClientHandle
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2622:25
 */
public class NettyGCClientHandle extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        /**
         * 本地窗口显示
         */
        System.out.println(msg.trim());
    }
}
