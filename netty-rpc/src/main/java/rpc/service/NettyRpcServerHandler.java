package rpc.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rpc.protocol.RpcRequest;
import rpc.protocol.RpcResponse;

import java.lang.reflect.Method;

public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(NettyRpcServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //1.接收请求对象
        RpcRequest rpcRequest = (RpcRequest) msg;
        logger.info("Netty rpc server receives the request:"+rpcRequest);

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try{

            // 2.我们需要根据 rpcRequest 要调用的类名进行反射，并实例化

            Class clazz = Class.forName(rpcRequest.getClassName());
            Object instance = clazz.newInstance();
            // 3.找到对应方法
            Method method = clazz.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            // 4.调用这个方法
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
