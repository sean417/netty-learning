package com.sean.netty.rpc.stickySolution;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyStickyPacketClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(NettyStickyPacketClientHandler.class);
    private static final String LINE_SEPERATOR = System.lineSeparator();

    private byte[] pingBytes;

    public NettyStickyPacketClientHandler() {
        pingBytes = ("hello, i am netty client, I request you!!!!!!!!"+LINE_SEPERATOR).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = null;
        for(int i=0;i<500;i++) {
            byteBuf = Unpooled.buffer(pingBytes.length);
            byteBuf.writeBytes(pingBytes);
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String response = (String) msg;
        logger.info("Netty client received response: "+ response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
