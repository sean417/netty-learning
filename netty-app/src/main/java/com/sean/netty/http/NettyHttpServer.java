package com.sean.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NettyHttpServer {
    private static final Logger logger = LogManager.getLogger(NettyHttpServer.class);

    private static final int DEFAULT_PORT = 8999;

    private int port;

    public NettyHttpServer(int port){
        this.port = port;
    }

    public void start() throws Exception{

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup,workEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 解码 resquest 的 handler
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder())
                                    // http对象聚合组件
                                    .addLast("http-aggregator",new HttpObjectAggregator(65536))
                                    // 编码 response 的 handler
                                    .addLast("http-encoder",new HttpResponseEncoder())
                                    // chunked handler
                                    .addLast("http-chunked",new ChunkedWriteHandler())
                                    // 真正处理请求和发送响应的 handler
                                    .addLast("netty-http-server-handler", new NettyHttpServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port);
            logger.info("starting......");
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossEventLoopGroup.shutdownGracefully();
            workEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        NettyHttpServer nettyHttpServer = new NettyHttpServer(DEFAULT_PORT);
        nettyHttpServer.start();
    }
}
