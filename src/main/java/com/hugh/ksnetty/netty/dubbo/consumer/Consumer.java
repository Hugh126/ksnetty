package com.hugh.ksnetty.netty.dubbo.consumer;

import com.hugh.ksnetty.netty.dubbo.commoninterface.HelloService;
import com.hugh.ksnetty.netty.dubbo.netty.NettyClient;

/**
 * @author hugh
 * @Title: Consumer
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/6/921:25
 */
public class Consumer {

    /**
     * 类名#方法名#参数
     */
    public final static String PROTOCOL_HEADER = "HelloService#hello#";

    public static void main(String[] args) {

        NettyClient consumer = new NettyClient("localhost", 7777);

        HelloService helloService = (HelloService)consumer.getBean(HelloService.class, PROTOCOL_HEADER);

        /**
         * 调用RPC
         */
        String res = helloService.hello("你好RPC");
        System.out.println("调用的结果 res= " + res);

    }
}
