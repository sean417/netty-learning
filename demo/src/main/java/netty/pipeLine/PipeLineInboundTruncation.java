package netty.pipeLine;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;


public class PipeLineInboundTruncation {

    public static void main(String[] args) {
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new InboundHandlerA());
                ch.pipeline().addLast(new InboundHandlerB());
                ch.pipeline().addLast(new InboundHandlerC());
                ch.pipeline().addLast(new InboundHandlerD());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        ByteBuf byteBuf = Unpooled.buffer();
        System.out.println("入站");
        byteBuf.writeInt(10);
        channel.writeInbound(byteBuf);

    }

    static class InboundHandlerA extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("回调：InboundHandlerA");
            super.channelRead(ctx, msg);
        }
    }

    static class InboundHandlerB extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("回调：InboundHandlerB");
            // super.channelRead(ctx, msg);
        }
    }

    static class InboundHandlerC extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("回调：InboundHandlerC");
            super.channelRead(ctx, msg);
        }
    }

    static class InboundHandlerD extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("回调：InboundHandlerD");
            super.channelRead(ctx, msg);
        }
    }
}
