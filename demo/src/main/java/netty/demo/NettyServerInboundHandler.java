package netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

// 这个类很像 reactor 模式里的processor线程，负责读区请求然后返回响应
public class NettyServerInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 第一步，获取客户端请求的内容
        ByteBuf buffer= (ByteBuf) msg;
        byte[] requestBytes = new byte[buffer.readableBytes()];
        buffer.readBytes(requestBytes);

        String request = new String(requestBytes,"UTF-8");
        System.out.println("收到请求::"+request);
        //第二步，向客户端返回信息
        String response = "收到请求后返回响应";
        ByteBuf responseBuffer = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseBuffer);

    }

    @Override
    //
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 真正的发送
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    // 只要channel打通了，就会执行
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server is Active......");
    }
}
