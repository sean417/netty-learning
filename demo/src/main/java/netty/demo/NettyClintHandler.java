package netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClintHandler extends ChannelInboundHandlerAdapter {
        // 第一步，定义要发送的内容
        private ByteBuf requestBuffer;

        public NettyClintHandler(){
            byte[] requestBytes = "客户端发送请求".getBytes();
            requestBuffer = Unpooled.buffer(requestBytes.length);
            requestBuffer.writeBytes(requestBytes);
        }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 第二步，向服务端发送消息
            ctx.writeAndFlush(requestBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 第三步，读取服务端的响应
        ByteBuf responseBuffer = (ByteBuf) msg;
        byte[] responseBytes = new byte[responseBuffer.readableBytes()];
        responseBuffer.readBytes(responseBytes);

        String response = new String(responseBytes,"UTF-8");
        System.out.println("收到服务端的响应："+response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
