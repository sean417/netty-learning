package rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rpc.protocol.RpcRequest;
import rpc.protocol.RpcResponse;

import java.lang.reflect.Method;

public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        RpcRequest rpcRequest = (RpcRequest) msg;
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try{

            System.out.println("netty rpc server receives the request:"+rpcRequest);

            // 获取 需要反序列化的class
            Class clazz = Class.forName(rpcRequest.getClassName());
            Object instance = clazz.newInstance();
            Method method = clazz.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            Object result = method.invoke(instance,rpcRequest.getArgs());

            // 把 rpc 调用结果放入响应中去
            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setResult(result);
            rpcResponse.setSuccess(true);
        }finally {

        }
        super.channelRead(ctx, msg);
    }
}
