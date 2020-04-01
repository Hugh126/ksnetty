package com.hugh.ksnetty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author hugh
 * @Title: ProtocolMsgDecoder
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/4/115:48
 */
public class ProtocolMsgDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("\r\n\r\nProtocolMsgDecoder--decode--方法被调用");

        // 从ByteBuf中读到Msg对象
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        ProtocolMessage message = new ProtocolMessage(length, content);

        // Msg对象放到out中， 传递给下个handler处理
        out.add(message);
    }
}
