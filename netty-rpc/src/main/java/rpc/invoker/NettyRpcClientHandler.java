package rpc.invoker;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rpc.protocol.RpcResponse;

import java.util.Date;

public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(NettyRpcClientHandler.class);
    private static final long GET_RPC_RESPONSE_SLEEP_INTERNAL = 5;

    private RpcResponse rpcResponse;
    private long timeout;

    public NettyRpcClientHandler(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        logger.info("netty rpc client Receive the response:"+ rpcResponse);

    }

    public RpcResponse getRpcResponse(){
        long waitStartTime =  new Date().getTime();
        while (rpcResponse == null){
            try {
                long now =  new Date().getTime();
                if(now - waitStartTime>=timeout){
                    break;
                }
                Thread.sleep(GET_RPC_RESPONSE_SLEEP_INTERNAL);
            }catch (InterruptedException e){
                logger.error("wait for response interrupted",e);
            }
        }
        if(rpcResponse == null){

        }
            return rpcResponse;
        }
}
