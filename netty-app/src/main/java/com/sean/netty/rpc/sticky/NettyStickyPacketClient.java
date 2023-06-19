package com.sean.netty.rpc.sticky;

import com.sean.netty.rpc.reference.NettyRpcReadTimeoutHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyStickyPacketClient {

    private static final Logger logger = LogManager.getLogger(NettyStickyPacketClient.class);
    private static final int DEFAULT_PORT = 8998;
    private static final String DEFAULT_HOST = "127.0.0.1";

    private String host;
    private int port;

    public NettyStickyPacketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception{
        logger.info("netty client is connecting!!!");
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
            try{
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE,true)
                        .handler(new ChannelInitializer<SocketChannel>(){
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(new NettyStickyPacketClientHandler());
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
                logger.info("server connected!!!");
                channelFuture.channel().closeFuture().sync();
            }finally {
                eventLoopGroup.shutdownGracefully();
            }
    }



    public static void main(String[] args) throws Exception{
            NettyStickyPacketClient client = new NettyStickyPacketClient(DEFAULT_HOST,DEFAULT_PORT);
            client.connect();
    }
}
