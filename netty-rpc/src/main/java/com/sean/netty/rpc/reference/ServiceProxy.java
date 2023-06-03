package com.sean.netty.rpc.reference;

import com.sean.netty.rpc.protocol.RpcRequest;
import com.sean.netty.rpc.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

// 代理服务模块
public class ServiceProxy {

    public static Object createProxy(ReferenceConfig referenceConfig){
            // 构建代理实例
            return Proxy.newProxyInstance(
                    ServiceProxy.class.getClassLoader(),
                    // 需要代理的接口
                    new Class[]{referenceConfig.getServiceInterfaceClass()},
                    new ServiceProxyInvocationHandler(referenceConfig));
    }

    //具体的代理逻辑
    static class ServiceProxyInvocationHandler implements InvocationHandler{

        private ReferenceConfig referenceConfig;

        public ServiceProxyInvocationHandler(ReferenceConfig referenceConfig){
            this.referenceConfig = referenceConfig;
        }
        // 通过动态代理改写类的方法
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            NettyRpcClient nettyRpcClient = new NettyRpcClient(referenceConfig);
            // 连接服务端
            nettyRpcClient.connect();
            RpcRequest rpcRequest = new RpcRequest();
            // 配置请求
            rpcRequest.setRequestId(UUID.randomUUID().toString());
            rpcRequest.setServiceInterfaceClass(referenceConfig.getServiceInterfaceClass().getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParameterTypes(method.getParameterTypes());
            rpcRequest.setArgs(args);
            // 远程调用
            RpcResponse  rpcResponse = nettyRpcClient.remoteCall(rpcRequest);

            return rpcResponse.getResult();
        }
    }
}
