package com.hugh.ksnetty.netty.dubbo.netty;

import com.hugh.ksnetty.netty.dubbo.consumer.Consumer;
import com.hugh.ksnetty.netty.dubbo.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author hugh
 * @Title: NettyServerHandle
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/6/921:19
 */
public class NettyServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("ServerHandler msg=" + msg);

        if(msg.toString().startsWith(Consumer.PROTOCOL_HEADER)) {
            System.out.println("Provider调用公共接口实现，并回传结果");
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常了...");
        ctx.close();
    }
}
