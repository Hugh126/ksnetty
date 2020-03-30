package com.hugh.ksnetty.netty.inAndOutBoundHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author hugh
 * @Title: MyClientHandler
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/3022:52
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从服务端" + ctx.channel().remoteAddress() + "收到数据" + msg);
    }

    /**
     * 通道激活  触发 向服务端发送数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("MyClientHandler--channelActive  发送数据");

        /**
         *  如果发送和接收的数据不一致， 编解码混乱
         *  此处 会导致 字符 被分成两次 发送，并解析成 两个Long
         */
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));

        ctx.writeAndFlush(123456L);
    }
}
