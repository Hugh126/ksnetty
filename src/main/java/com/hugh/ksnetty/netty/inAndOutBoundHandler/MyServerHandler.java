package com.hugh.ksnetty.netty.inAndOutBoundHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author hugh
 * @Title: MyServerHandler
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/3017:02
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

        System.out.println("从客户端" + ctx.channel().remoteAddress() + "读取到Long=" + msg);

        /**
         * 回一个消息(Long) 给客户端
         */
        ctx.writeAndFlush(56789L);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端异常");
        ctx.channel().close();
    }
}
