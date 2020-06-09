package com.hugh.ksnetty.netty.dubbo.commoninterface;

/**
 * @author hugh
 * @Title: HelloService
 * @ProjectName ksnetty
 * @Description:
 * 接口，在服务方定义实现
 * 注册到注册中心，
 * 客户端从注册中心获取到
 * 客户端消费接口
 * @date 2020/6/921:13
 */
public interface HelloService {

    String hello(String msg);

}
