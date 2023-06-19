package com.sean.netty.rpc.service;

import com.sean.netty.rpc.protocol.RpcRequest;
import com.sean.netty.rpc.protocol.RpcResponse;
import com.sean.netty.rpc.serialization.RpcDecoder;
import com.sean.netty.rpc.serialization.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class NettyRpcServer {

    private static final Logger logger = LogManager.getLogger(NettyRpcServer.class);
    private static final int DEFAULT_SERVICE_PORT = 8998;

    // 多个服务的容器
    private List<ServiceConfig> serviceConfigs =  new CopyOnWriteArrayList<ServiceConfig>();
    private int port;

    public NettyRpcServer(int port) {
        this.port = port;
    }

    public void start() throws Exception{
        logger.info("netty rpc server starting......");

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // 反序列化 RpcRequest 对象。
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    // 序列化
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    // 接收请求，发送响应
                                    .addLast(new NettyRpcServerHandler(serviceConfigs));
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            // server 启动并监听指定的端口号
            ChannelFuture channelFuture =  serverBootstrap.bind(port).sync();
            logger.info("netty rpc server started successfully" );
            System.out.println("netty rpc server started successfully");;
            // 进入阻塞状态，同步一直等到 server 关闭。
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }

    public void addServiceConfig(ServiceConfig serviceConfig){
        serviceConfigs.add(serviceConfig);
    }
    public static void main(String[] args) throws Exception{
        // 1.实例化服务配置
        ServiceConfig serviceConfig = new ServiceConfig("TestService",TestService.class,TestServiceImpl.class);
        // 2.创建服务
        NettyRpcServer nettyRpcServer =  new NettyRpcServer(DEFAULT_SERVICE_PORT);
        nettyRpcServer.addServiceConfig(serviceConfig);

        // 3.服务类型
        nettyRpcServer.start();
    }

}

