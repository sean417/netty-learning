package com.sean.netty.rpc.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sean.netty.rpc.protocol.RpcRequest;
import com.sean.netty.rpc.protocol.RpcResponse;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(NettyRpcServerHandler.class);


    private ConcurrentHashMap<String, ServiceConfig> serviceConfigsMap =
            new ConcurrentHashMap<String,ServiceConfig>();

    public NettyRpcServerHandler(List<ServiceConfig> serviceConfigs) {
        for(ServiceConfig serviceConfig:serviceConfigs){
            String serviceInterfaceClass = serviceConfig.getServiceInterfaceClass().getName();
            serviceConfigsMap.put(serviceInterfaceClass,serviceConfig);
        }
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.接收请求对象
        RpcRequest rpcRequest = (RpcRequest) msg;
        logger.info("Netty rpc server receives the request:"+rpcRequest);
        System.out.println("Netty rpc server receives the request:"+rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try{
            // 2.我们需要根据 rpcRequest 要调用的接口找到实现类
            ServiceConfig serviceConfig = serviceConfigsMap.get(rpcRequest.getServiceInterfaceClass());
            // 3. 把实现类通过反射实例化
            Class clazz =  serviceConfig.getServiceClass();
            Object instance = clazz.newInstance();
            // 4.找到对应方法，并调用
            Method method = clazz.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            Object result = method.invoke(instance,rpcRequest.getArgs());

            // 5.把 rpc 调用结果放入响应中去
            rpcResponse.setResult(result);
            rpcResponse.setSuccess(RpcResponse.SUCCESS);
        }catch (Exception e){
            logger.error("netty rpc server failed to response the request",e);
            rpcResponse.setSuccess(RpcResponse.FAILURE);
            rpcResponse.setException(e);
        }
        ctx.write(rpcResponse);
        ctx.flush();
    }
}
