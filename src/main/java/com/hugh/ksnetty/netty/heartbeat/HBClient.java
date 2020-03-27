package com.hugh.ksnetty.netty.heartbeat;

import com.hugh.ksnetty.netty.groupchat.NettyGCClient;

/**
 * @author hugh
 * @Title: HBClient
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/3/2710:32
 */
public class HBClient {

    /**
     * 心跳检测，用一个普通client代替， 保证 port一致即可
     * @param args
     */
    public static void main(String[] args) {
        new NettyGCClient().run();
    }
}
