package rpc.invoker;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rpc.protocol.RpcRequest;
import rpc.protocol.RpcResponse;
import rpc.serialization.RpcDecoder;
import rpc.serialization.RpcEncoder;

import java.util.Date;

public class NettyRpcClient {

    private static final Logger logger = LogManager.getLogger(NettyRpcClient.class);
    private String serviceHost;
    private int servicePort;
    private long timeout;
    private ChannelFuture channelFuture;
    NettyRpcClientHandler nettyRpcClientHandler ;

    public NettyRpcClient(String serviceHost, int servicePort,long timeout) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.timeout = timeout;
        this.nettyRpcClientHandler =  new NettyRpcClientHandler(timeout);
    }

    public void connect(){
        logger.info("connecting to netty rpc server");

        EventLoopGroup eventLoopGroup =  new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RpcEncoder(RpcRequest.class))
                                .addLast(new RpcDecoder(RpcResponse.class))
                                .addLast(new NettyRpcReadTimeoutHandler(timeout))
                                .addLast(nettyRpcClientHandler);
                    }
                });
        try{
            // 发起连接
            ChannelFuture channelFuture = bootstrap.connect(serviceHost,servicePort).sync();
            logger.info("successfully connected");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    // 连接成功后，发送 RPC 请求
    public RpcResponse remoteCall(RpcRequest rpcRequest) throws Throwable{

        NettyRpcRequestTimeHolder.put(rpcRequest.getRequestId(),new Date().getTime());

        // 1.拿到 channel,并往 channel 里写对象，本质就是向服务端发送请求。
        channelFuture.channel().writeAndFlush(rpcRequest).sync();
        channelFuture.channel().closeFuture().sync();


        // 2. 收到服务提供方的响应
        RpcResponse rpcResponse = nettyRpcClientHandler.getRpcResponse(rpcRequest.getRequestId());
        logger.info("receive response from netty rpc server:"+rpcResponse);
        if(rpcResponse.getSuccess()){
            return rpcResponse;
        }
        throw rpcResponse.getException();

    }
}
