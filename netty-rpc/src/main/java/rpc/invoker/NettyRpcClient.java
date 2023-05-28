package rpc.invoker;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rpc.protocol.RpcRequest;
import rpc.protocol.RpcResponse;
import rpc.serialization.RpcDecoder;
import rpc.serialization.RpcEncoder;

public class NettyRpcClient {

    private static final Logger logger = LogManager.getLogger(NettyRpcClient.class);

    private String ServiceHost;
    private int servicePort;

    public NettyRpcClient(String serviceHost, int servicePort) {
        ServiceHost = serviceHost;
        this.servicePort = servicePort;
    }

    public void connect(){
        logger.info("connecting to netty rpc server");

        EventLoopGroup eventLoopGroup =  new NioEventLoopGroup();
        Bootstrap bootstrap =  new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RpcEncoder(RpcRequest.class))
                                .addLast(new RpcDecoder(RpcRequest.class))
                                .addLast(new NettyRpcReadTimeoutHandler())
                                .addLast(new NettyRpcClientHandler());
                    }
                });
        try{
            // 发起连接
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",8889);
            logger.info("successfully connected");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
