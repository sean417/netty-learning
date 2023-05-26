package netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {
    public static void main(String[] args) {
        // 第一步，定义一个EventLoopGroup
        EventLoopGroup parent = new NioEventLoopGroup();   //Acceptor线程组
        try{
            Bootstrap bootstrap= new Bootstrap();
            // 第二步，对客户端做各种配置
            bootstrap.group(parent)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new NettyClintHandler());
                        }
                    });
            //第三步，向服务端连接
            ChannelFuture channelFuture= bootstrap.connect("127.0.0.1",50099).sync();
            channelFuture.channel().closeFuture().sync();


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
