package com.hugh.ksnetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author hugh
 *
 *
 * decode -> complate -> encode -> send
 *
 */
public class NettyServerHandle extends ChannelInboundHandlerAdapter {


    /**
     * 读取数据事件
     *
     * ChannelHandlerContext  重量级对象，包含 {pipeline}  {channel} {address}
     * 本质上是双向链表
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx = " + ctx);

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("客户端发送了消息=" + byteBuf.toString(CharsetUtil.UTF_8));

        System.out.println("客户端地址=" + ctx.channel().remoteAddress());

        /**
         * Netty人物队列使用场景:
         * 1. 用户自定义普通任务
         * 2. 用户自定义定时任务
         * 3. 非当前 Ractor 线程调用Channel各种方法( 把所有用户的SocketChannel用集合管理起来，再调用)
         *
         *
         * 假设这里需要处理一个费时的操作，为使不阻塞，演示第一种场景
         *
         * 注意：此处可以启动多个线程执行，然而，eventLoop中的task实则还是在一个线程中
         */

        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("哈喽，客户端111");
        });

        /**
         * 定时任务场景
         */
        ctx.channel().eventLoop().schedule( () -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("哈喽，客户端222 From定时任务");
        }, 5, TimeUnit.SECONDS);

        /**
         * 这里可以打个断点，debug观察eventLoop中的taskQueue
         */
        System.out.println("等待好久了，Let,s go ...");
    }


    /**
     * 数据读取完毕
     *
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        String returnInfo = "hello, 客户端";

        ctx.writeAndFlush(Unpooled.copiedBuffer(returnInfo, CharsetUtil.UTF_8));

    }


    /**
     *
     * 异常关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }


}
