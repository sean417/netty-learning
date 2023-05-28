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

        RpcRequest rpcRequest = (RpcRequest) msg;
        logger.info("Netty rpc server receives the request:"+rpcRequest);

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try{

            // 我们需要根据 rpcRequest 要调用的类名进行反射，并实例化
            // 得到要访问的方法，再根据 rpcRequest 提供的方法参数去调用这个方法
            Class clazz = Class.forName(rpcRequest.getClassName());
            Object instance = clazz.newInstance();
            Method method = clazz.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            Object result = method.invoke(instance,rpcRequest.getArgs());

            // 把 rpc 调用结果放入响应中去
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
