package netty.pipeLine;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;


public class PipeLineOutboundOrder {

    public static void main(String[] args) {
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new OutboundHandlerA());
                ch.pipeline().addLast(new OutboundHandlerB());
                ch.pipeline().addLast(new OutboundHandlerC());
                ch.pipeline().addLast(new OutboundHandlerD());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(10);
        channel.writeOutbound(byteBuf);
    }

    static class OutboundHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("回调：OutboundHandlerA");

            super.write(ctx, msg, promise);
        }
    }

    static class OutboundHandlerB extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("回调：OutboundHandlerB");

            super.write(ctx, msg, promise);
        }
    }

    static class OutboundHandlerC extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("回调：OutboundHandlerC");

            super.write(ctx, msg, promise);
        }
    }

    static class OutboundHandlerD extends ChannelOutboundHandlerAdapter{
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("回调：OutboundHandlerD");

            super.write(ctx, msg, promise);
        }
    }
}
