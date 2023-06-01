package com.sean.netty.rpc.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> targetClass;
    public RpcEncoder(Class<?> targetClass){
        this.targetClass = targetClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
            if(targetClass.isInstance(o)){
                // 序列化
                byte[] bytes = HessianSerialization.serialize(o);
                // 先写入 4 个字节的 长度
                byteBuf.writeInt(bytes.length);
                // 写入 byte 数组
                byteBuf.writeBytes(bytes);
            }
    }
}
