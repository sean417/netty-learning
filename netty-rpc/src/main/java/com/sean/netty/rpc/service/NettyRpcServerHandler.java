package rpc.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rpc.protocol.RpcRequest;
import rpc.protocol.RpcResponse;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(NettyRpcServerHandler.class);

    private static ConcurrentHashMap<String, String> serviceClasses = new ConcurrentHashMap<>();

    static {
        serviceClasses.put("TestService","TestServiceImpl");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.接收请求对象
        RpcRequest rpcRequest = (RpcRequest) msg;
        logger.info("Netty rpc server receives the request:"+rpcRequest);

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try{

            // 2.我们需要根据 rpcRequest 要调用的接口找到实现类
            String serviceClass = serviceClasses.get(rpcRequest.getServiceInterfaceClass());
            // 3. 把实现类通过反射实例化
            Class clazz =  Class.forName(serviceClass);
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
