package com.hugh.ksnetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author hugh
 *
 *
 * decode -> complate -> encode -> send
 *
 */
public class NettyServerHandle extends ChannelInboundHandlerAdapter {


    /**
     * 读取数据事件
     *
     * ChannelHandlerContext包含 {pipeline}  {channel} {address}
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx = " + ctx);

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("客户端发送了消息=" + byteBuf.toString(CharsetUtil.UTF_8));

        System.out.println("客户端地址=" + ctx.channel().remoteAddress());

    }


    /**
     * 数据读取完毕
     *
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        String returnInfo = "hello, 客户端";

        ctx.writeAndFlush(Unpooled.copiedBuffer(returnInfo, CharsetUtil.UTF_8));

    }


    /**
     *
     * 异常关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }


}
