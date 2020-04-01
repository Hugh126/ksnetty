package com.hugh.ksnetty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author hugh
 * @Title: ProtocolMsgEncoder
 */
public class ProtocolMsgEncoder extends MessageToByteEncoder<ProtocolMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage msg, ByteBuf out) throws Exception {
        System.out.println("\r\n\r\nProtocolMsgEncoder--encode--方法被调用");

        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
