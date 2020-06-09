package com.hugh.ksnetty.netty.dubbo.provider;

import com.hugh.ksnetty.netty.dubbo.commoninterface.HelloService;

/**
 * @author hugh
 * @Title: HelloServiceImpl
 * @ProjectName ksnetty
 * @Description: TODO
 * @date 2020/6/921:16
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        System.out.println("Provider实现公共接口功能，当前客户端输入为   " + msg);

        return "[已经通过Provider认证]" + msg;
    }
}
