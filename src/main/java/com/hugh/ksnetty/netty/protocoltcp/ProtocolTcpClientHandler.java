package com.hugh.ksnetty.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author hugh
 */
public class ProtocolTcpClientHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    private static final String sendMsg = "唧唧复唧唧，何日上班去";
    int count = 0 ;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("---向服务端发送数据---");
        for(int i= 0 ;i< 5 ;i++) {
//            String tempMsg = sendMsg + i;
            byte[] content = sendMsg.getBytes(CharsetUtil.UTF_8);
            int len = content.length;
            ctx.writeAndFlush(new ProtocolMessage(len, content));
        }
        System.out.println("数据发送完成");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg) throws Exception {
        System.out.println("---收到服务端反馈消息---长度=" + msg.getLength());
        System.out.println(new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("收到服务端反馈消息数量=" + (++count));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
