package com.hugh.ksnetty.netty.dubbo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author hugh
 * @Title: NettyClientHandle
 * @ProjectName ksnetty
 * @Description:
 *
 * 被代理对象调用, 发送数据给服务器(call)-> wait -> 等待被唤醒(channelRead)
 *
 * 注意 synchronized 的理解
 *
 * @date 2020/6/921:22
 */
public class NettyClientHandle extends ChannelInboundHandlerAdapter implements Callable {

    private String param;
    private String result;
    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" channelActive被调用");
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead被调用");
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端异常了...");
        ctx.close();
    }


    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call1-111");
        context.writeAndFlush(param);

        //等待channelRead获取到服务器的结果
        wait();
        System.out.println("call1-222");
        return result;
    }

    public void setParam(String param) {
        System.out.println("传入参数 = " + param);
        this.param = param;
    }
}
