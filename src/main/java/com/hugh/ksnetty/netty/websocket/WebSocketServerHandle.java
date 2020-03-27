package com.hugh.ksnetty.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author hugh
 * @Title: WebSocketServerHandle
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2714:24
 */
public class WebSocketServerHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        System.out.println("服务端收到消息 : " + msg.text());

        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端当前时间 : " + LocalDateTime.now()
                + "|" + msg.text()));
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded, channelId = " + ctx.channel().id());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved, channelId = " + ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生，关闭通道");
        ctx.channel().close();
    }


}
