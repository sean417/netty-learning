package netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.handler.HandlerLifeCycle;

public class NettyServer {

    public static void main(String[] args) {
        // 第一步，分别创建两个处理网络的EventLoopGroup。
        // 主线程池负责客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();   //Acceptor 线程组
        // 处理客户端与服务端之间的读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();    //Processor 或 Handler 线程组

        try{
            // 第二步，初始化服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap(); //相当于Netty服务器
            // 第三步，给服务器做一系列的配置。
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//监听端口的ServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() { //处理每个连接的 SocketChannel,SocketChannel代表每一个连接
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new HandlerLifeCycle())
                                    // 解码：从 byteBuffer 到 String。
                                    .addLast(new StringDecoder())
                                    // 编码：从 String 到 byteBuffer
                                    .addLast(new StringEncoder())
                                    .addLast(new NettyServerInboundHandler()); //针对网络请求的处理逻辑
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,256)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            System.out.println("Server 启动了");
            // 第四步，绑定端口。
            ChannelFuture channelFuture =  serverBootstrap.bind(50099).sync(); //监听指定端口
            // 第五步，等待服务器关闭
            channelFuture.channel().closeFuture().sync();// 同步等待关闭启动服务器的结果
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
