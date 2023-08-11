package com.sean.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyWebSocketServer {
    private static final Logger logger = LogManager.getLogger(NettyWebSocketServer.class);

    private static final int DEFAULT_PORT = 8998;

    private int port;

    public NettyWebSocketServer(int port){ this.port = port;}

    public void start() throws Exception{
        logger.info("netty websocket server is starting......");

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // websocket pipeline
                                    // 客户端的字节数组收到后，先用 HttpServerCodes 来解码
                                    // 并转换为一个 HttpRequest 对象
                                    .addLast(new HttpServerCodec())
                                    // 太大的数据流会把数据流分为块来发送或接收。
                                    .addLast(new ChunkedWriteHandler())
                                    // 把分离的数据段聚合成一个完整的数据段，并定义聚合数据对象的大小。
                                    .addLast(new HttpObjectAggregator(1024*32))
                                    // 基于前面已经处理好的请求数据对象，这里基于Websocket 协议在做一次处理
                                    // 基于 http 协议传输过来的数据， 封装内容， 可以是按 websocket 协议封装的 http 请求里的数据
                                    // 必须在这拿出出 http 请求里的数据， 按照 websocket 协议来进行解析处理， 把数据提取出来作为 webSocket 的数据
                                    //
                                    .addLast(new WebSocketServerProtocolHandler("/websocket"))
                                    // 业务逻辑处理
                                    .addLast("netty-websocket-server-handler", new NettyWebSocketServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("netty websocket server is started, listened["+port+"].");
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        NettyWebSocketServer nettyWebSocketServer = new NettyWebSocketServer(DEFAULT_PORT);
        nettyWebSocketServer.start();
    }


}
