package com.hugh.ksnetty.netty.inAndOutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author hugh
 * @Title: MyLongToByteEncoder
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/3017:04
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder encode 方法调用..., msg=" + msg );

        out.writeLong(msg);
    }
}
