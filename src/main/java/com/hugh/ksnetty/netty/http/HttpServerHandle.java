package com.hugh.ksnetty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author hugh
 * @Title: HttpServerHandle
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2213:39
 */
public class HttpServerHandle extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest) {

            System.out.println("客户端地址=" + ctx.channel().remoteAddress());

            /**
             * 每一个客户端对应的 pipeline he handle 是对应的
             */
            System.out.println("[pipeline = ]" + ctx.pipeline().hashCode() + " [handle=]" + this.hashCode());

            /**
             * 屏蔽图标导致的二次请求
             */

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())) {
                System.out.println("ioc 请求, pass");
                return;
            }


            /**
             *
             * 遵循 http 协议
             * 构造一个HttpResponse 返回给浏览器
             *
             *
             */
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 这里是Server总部", CharsetUtil.UTF_8);

            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            ctx.writeAndFlush(response);
        }
    }


}
