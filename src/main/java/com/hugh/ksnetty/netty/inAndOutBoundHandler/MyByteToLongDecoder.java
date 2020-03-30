package com.hugh.ksnetty.netty.inAndOutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author hugh
 * @Title: MyByteToLongDecoder
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/3016:50
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {


    /**
     * [根据接收的数据size， decode方法会被调用多次，直到没有新数据添加到out
     *  或者ByteBuf没有足够可读字节
     *
     *  并且，每次decode方法调用都会触发  下一个 handler 的 channelRead0 方法
     * ]
     *
     *
     * @param ctx
     * @param in  入站的ByteBuf
     * @param out 将解码后的数据传给下一个handle
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyByteToLongDecoder decode方法被调用");

        /**
         * long 有8个字节
         */
        if(in.readableBytes() >= 8) {
            out.add(in.readLong());
        }

    }
}
