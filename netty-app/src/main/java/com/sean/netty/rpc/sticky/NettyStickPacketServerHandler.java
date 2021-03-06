package com.sean.netty.rpc.sticky;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class NettyStickPacketServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(NettyStickPacketServerHandler.class);

    private static final String LINE_SEPERATOR = System.lineSeparator();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        String message = new String(bytes,"UTF-8");
        //message = message.substring(0,message.length() - LINE_SEPERATOR.length());

        logger.info("netty server received message:"+ message);

        // 构造响应，并加入换行符。
        String response = "hello, server is here! now is : "+ new Date().getTime()
                +LINE_SEPERATOR;

        ByteBuf responseByteBuf = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseByteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }



}
