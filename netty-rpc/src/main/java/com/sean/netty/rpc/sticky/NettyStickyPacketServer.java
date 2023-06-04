package com.sean.netty.rpc.sticky;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyStickyPacketServer {

    private static final Logger logger = LogManager.getLogger(NettyStickyPacketServer.class);

    public void start() throws Exception{

        logger.info("server is starting!!!");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new NettyStickPacketServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8998).sync();
            logger.info("netty server started， and listening!!!");
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        NettyStickyPacketServer server = new NettyStickyPacketServer();
        server.start();
    }
}
