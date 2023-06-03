package com.sean.netty.rpc.reference;

import com.sean.netty.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(NettyRpcClientHandler.class);
    private static final long GET_RPC_RESPONSE_SLEEP_INTERNAL = 5;

    // 响应结果的封装
    private ConcurrentHashMap<String,RpcResponse> rpcResponses = new ConcurrentHashMap<>();
    private long timeout;

    public NettyRpcClientHandler(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        // 如果超时
        if(rpcResponse.getTimeout()){
            logger.error("rpc client is receiving response timeout:"+ rpcResponse);
        }else {
            // 放入 响应集合等待获取
            rpcResponses.put(rpcResponse.getRequestId(),rpcResponse);
            logger.info("rpc client is receiving the response:"+ rpcResponse);
        }
    }

    // 获取响应集合中的响应
    public RpcResponse getRpcResponse(String requestId) throws NettyRpcReadTimeoutException{
        // 响应超时的异常处理
        long waitStartTime =  new Date().getTime();
        while (rpcResponses.get(requestId) == null){
            try {
                long now =  new Date().getTime();
                if(now - waitStartTime >= timeout){
                    break;
                }
                Thread.sleep(GET_RPC_RESPONSE_SLEEP_INTERNAL);
            }catch (InterruptedException e){
                logger.error("wait for response interrupted",e);
            }
        }
        RpcResponse rpcResponse = rpcResponses.get(requestId);
        if(rpcResponse == null){
                logger.error("get rpc response timeout.");
                throw new NettyRpcReadTimeoutException("get rpc response timeout.");
        }else {
            // 清理响应集合里的响应
            rpcResponses.remove(requestId);
        }
            return rpcResponse;
        }
}
