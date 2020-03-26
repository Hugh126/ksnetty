package com.hugh.ksnetty.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hugh
 *
 * 1、接受客户端消息
 * 2、进行转发
 *
 *
 * [策略]
 * 使用 ChannelGroup 维护ClientChannel 组
 */
public class NettyGCServerHandle extends SimpleChannelInboundHandler<String> {

    // 全局事件执行器 、 单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 遍历并排除自己
        channelGroup.forEach(ch -> {
            if(channel != ch) {
                ch.writeAndFlush(formatChannelMsg((InetSocketAddress) channel.remoteAddress(), msg, true));
            }else {
//                ch.writeAndFlush(formatChannelMsg() + "[自己已在聊天室发送一条消息]" + msg + "\n");
            }
        });

    }

    /**
     * 建立连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 会遍历所有包含的channel,并发送通知
        channelGroup.writeAndFlush(formatChannelMsg((InetSocketAddress) channel.remoteAddress(), "加入群聊!\n", false) );
        channelGroup.add(channel);
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(formatChannelMsg((InetSocketAddress) channel.remoteAddress(),"离开了!\n", false));
        // 会自己remove ，do nothing
//        System.out.println("--------当前在线人数---------" + channelGroup.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println( "-------" + ctx.channel().remoteAddress() + "上线了----");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("-------" + ctx.channel().remoteAddress() + "离线了-------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }

    private String formatChannelMsg(InetSocketAddress address, String msg, boolean isChat) {
        StringBuilder sb = new StringBuilder();
        sb.append('[')
                .append(dateTimeFormatter.format(LocalDateTime.now()))
                .append(']')
                .append(address.getPort());
        if(isChat) {
            sb.append(" : ");
        }
        sb.append(msg);
        return sb.toString();
    }
}
