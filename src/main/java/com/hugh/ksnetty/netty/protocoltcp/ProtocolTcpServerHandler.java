package com.hugh.ksnetty.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @author hugh
 */
public class ProtocolTcpServerHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    private int count = 0;
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg) throws Exception {
        System.out.println("服务端收到客户端消息---msg=" + new String(msg.getContent(), CharsetUtil.UTF_8));

        System.out.println("服务端接收到消息数量" + (++count));

        /**
         * 回复消息给客户端
         */
        String resp = "疫情何时散，且看今朝" + UUID.randomUUID().toString();
        byte[] content = resp.getBytes(CharsetUtil.UTF_8);
        System.out.println("发送给客户端消息长度=" + content.length);
        ctx.writeAndFlush(new ProtocolMessage(content.length, content));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端异常");
        ctx.channel().close();
    }


}
