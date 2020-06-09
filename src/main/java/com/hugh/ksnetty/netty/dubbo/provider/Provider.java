package com.hugh.ksnetty.netty.dubbo.provider;

import com.hugh.ksnetty.netty.dubbo.netty.NettyServer;

/**
 * @author hugh
 * @Title: Provider
 * @ProjectName ksnetty
 * @Description: 服务提供者启动NettyServer
 * @date 2020/6/921:24
 */
public class Provider {

    public static void main(String[] args) {
        NettyServer.startServer("localhost", 7777);
    }
}
