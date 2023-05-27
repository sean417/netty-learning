package com.sean.rpc.serialization;

import com.sean.rpc.protocol.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private static final int MESSAGE_BYTES_LENGTH = 4;

    private static final int MESSAGE_LENGTH_NORMAL_LENGTH = 0;

    private Class<?> targetClass;

    public RpcDecoder(Class<?> targetClass){
        this.targetClass = targetClass;
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //校验整体数据长度
        if(byteBuf.readableBytes()< MESSAGE_BYTES_LENGTH){
            return;
        }
        // 对于 byteBuf 当前可以读的 readerIndex 做一个 mark 标记
        // 这样以后可以找到发起 read 之前的 readerIndex 的位置
        byteBuf.markReaderIndex();

        // 读取 4 个字节的 int 类型用来表示消息的 byte 长度。
        int messageLength = byteBuf.readInt();
        // 如果长度小于正常对象长度，就关闭 channel。
        if(messageLength < MESSAGE_LENGTH_NORMAL_LENGTH){
            channelHandlerContext.close();
        }

        // 此时如果可读字节数小于对象的字节长度
        // 本质是检查是否是拆包问题
        if(byteBuf.readableBytes() < messageLength){
            // 复位 readerIndex,下次再从对象体开始的地方读。
            byteBuf.resetReaderIndex();
            return;
        }
        // 反序列化
        byte[] bytes = new byte[messageLength];
        byteBuf.readBytes(bytes);
        Object object = HessianSerialization.deserialize(bytes, RpcRequest.class);


    }
}
