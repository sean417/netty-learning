package com.sean.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.soap.Text;

public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LogManager.getLogger(NettyWebSocketServerHandler.class);

    private static ChannelGroup webSocketClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // websocket 网页客户端发送过来的数据。
        String request = msg.text();
        logger.info("netty server receives request; "+ request+ ".");

        TextWebSocketFrame response = new TextWebSocketFrame("hello, I'm WebSocketServer!");
        ctx.writeAndFlush(response);
    }


    // 如果一个 webSocket 客户端与这个 webSocket 服务端建立连接后，这个方法就会触发，把连接放入集合
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        webSocketClients.add(ctx.channel());
    }

    // 如果一个 webSocket 客户端与这个 webSocket 服务端断开连接后，这个方法就会触发，把连接从集合删除

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("websocket connection is closed, channel id:"+ctx.channel().id().asLongText()+"["+
                ctx.channel().id().asShortText()+"]");

        super.handlerRemoved(ctx);

    }
}
