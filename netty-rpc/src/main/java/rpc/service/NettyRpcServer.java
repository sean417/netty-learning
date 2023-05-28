package rpc.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.Logger;
import rpc.protocol.RpcRequest;
import rpc.protocol.RpcResponse;
import rpc.serialization.RpcDecoder;
import rpc.serialization.RpcEncoder;

import org.apache.logging.log4j.LogManager;


public class NettyRpcServer {

    private static final Logger logger = LogManager.getLogger(NettyRpcServer.class);
    private int port;
    public NettyRpcServer(int port){
        this.port=port;
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
                                    .addLast(new NettyRpcServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //server 启动并监听指定的端口号
            ChannelFuture channelFuture =  serverBootstrap.bind(port).sync();
            logger.info("netty rpc server started successfully");
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

    public static void main(String[] args) throws Exception{
        NettyRpcServer nettyRpcServer =  new NettyRpcServer(8998);
        nettyRpcServer.start();
    }

}

