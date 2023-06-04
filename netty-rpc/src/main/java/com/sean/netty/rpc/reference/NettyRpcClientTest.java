package com.sean.netty.rpc.reference;

import com.sean.netty.rpc.service.TestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyRpcClientTest {
    private static final Logger logger = LogManager.getLogger(NettyRpcClientTest.class);

    public static void main(String[] args) {
        // 定义要调用的服务
        ReferenceConfig referenceConfig = new ReferenceConfig(TestService.class);
        // 利用动态代理封装接口
        TestService testService = (TestService) ServiceProxy.createProxy(referenceConfig);
        // 调用接口的方法
        String resulet = testService.sayHello("sean");

        logger.info("call complete :"+ resulet);

    }
}
