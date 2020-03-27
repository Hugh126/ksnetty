package com.hugh.ksnetty.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author hugh
 * @Title: HBEventTriggerHandle
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2710:49
 */
public class HBEventTriggerHandle extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("--MSG--" + msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            String type = null;
            switch (stateEvent.state()) {
                case READER_IDLE:
                    type = "读空闲";
                    break;
                case WRITER_IDLE:
                    type = "写空闲";
                    break;
                case ALL_IDLE:
                    type = "读写空闲";
                    break;
            }

            System.out.println("--发送事件--" + type);

            /**
             * 服务端可针对type做出相应处理..............
             */

            if(IdleState.ALL_IDLE.equals(stateEvent.state())) {
                System.out.println("发生读写空闲，关闭连接...");
                ctx.channel().close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("--客户端已断开--");
        ctx.channel().close();
    }
}
