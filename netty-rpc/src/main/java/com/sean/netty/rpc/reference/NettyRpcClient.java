package com.sean.netty.rpc.reference;

import com.sean.netty.rpc.protocol.RpcResponse;
import com.sean.netty.rpc.serialization.RpcDecoder;
import com.sean.netty.rpc.serialization.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sean.netty.rpc.protocol.RpcRequest;

import java.util.Date;

public class NettyRpcClient {

    private static final Logger logger = LogManager.getLogger(NettyRpcClient.class);
    // 请求服务的配置
    private ReferenceConfig referenceConfig;

    private ChannelFuture channelFuture;
    NettyRpcClientHandler nettyRpcClientHandler ;

    public NettyRpcClient(ReferenceConfig referenceConfig) {
        this.referenceConfig = referenceConfig;
        this.nettyRpcClientHandler =  new NettyRpcClientHandler(referenceConfig.getTimeout());
    }

    public void connect(){
        logger.info("connecting to netty rpc server");
        // 1. 初始化线程池资源
        EventLoopGroup eventLoopGroup =  new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        // 2.配置
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 序列化
                                .addLast(new RpcEncoder(RpcRequest.class))
                                // 反序列化
                                .addLast(new RpcDecoder(RpcResponse.class))
                                // 响应超时处理
                                .addLast(new NettyRpcReadTimeoutHandler(referenceConfig.getTimeout()))
                                // 核心逻辑处理
                                .addLast(nettyRpcClientHandler);
                    }
                });
        try{
            // 3.发起连接
            channelFuture = bootstrap.connect(referenceConfig.getServceHost(),referenceConfig.getServicePort()).sync();
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


        // 2. 收到服务提供方的响应
        RpcResponse rpcResponse = nettyRpcClientHandler.getRpcResponse(rpcRequest.getRequestId());
        logger.info("receive response from netty rpc server: "+rpcResponse);
        System.out.println("receive response from netty rpc server: "+rpcResponse);
        if(rpcResponse.getSuccess()){
            return rpcResponse;
        }
        throw rpcResponse.getException();

    }
}
