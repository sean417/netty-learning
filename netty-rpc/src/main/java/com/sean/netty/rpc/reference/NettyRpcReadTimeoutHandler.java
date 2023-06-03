package com.sean.netty.rpc.reference;

import com.sean.netty.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;


//处理超时响应
public class NettyRpcReadTimeoutHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(NettyRpcReadTimeoutHandler.class);

    private long timeout;

    public NettyRpcReadTimeoutHandler(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 1.类型强转成 RpcResponse 对象
        RpcResponse rpcResponse = (RpcResponse) msg;
        // 2.拿到发送时间
        long requestTime = NettyRpcRequestTimeHolder.get(rpcResponse.getRequestId());
        long now = new Date().getTime();
        if(now - requestTime >= timeout){
            rpcResponse.setTimeout(true);
            logger.error("rpc response is regarded as timeout: "+ rpcResponse);
        }else {
            rpcResponse.setTimeout(false);
        }

        NettyRpcRequestTimeHolder.remove(rpcResponse.getRequestId());
        super.channelRead(ctx, msg);
    }
}
