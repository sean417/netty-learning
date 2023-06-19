package com.sean.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LogManager.getLogger(NettyHttpServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
          logger.info(msg);
          String method = msg.getMethod().name();
          String uri = msg.getUri();

          logger.info("receives http request:{} uri :{}",method,uri);
          // 创建响应
          FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
          // 构造响应的html内容
          String html = "<html><body>hello, netty http server is coming</body></html>";
          // 创建 byteBuf
          ByteBuf byteBuf = Unpooled.copiedBuffer(html, CharsetUtil.UTF_8);
          // 创建相应头
          response.headers().set("content-type","text/html;charset=UTF-8");
          // 把响应内容写入 response
          response.content().writeBytes(byteBuf);
          // 释放对 byteBuf 的引用
          byteBuf.release();
          // 把 response 发送回去
          ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
