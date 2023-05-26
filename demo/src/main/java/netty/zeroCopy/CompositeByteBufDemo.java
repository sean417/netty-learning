package netty.zeroCopy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

public class CompositeByteBufDemo {
    public static void main(String[] args) {
        ByteBuf byteBufA = Unpooled.buffer();
        byteBufA.writeBytes("a".getBytes());
        for(int i=0;i<byteBufA.readableBytes();i++){
            System.out.println("byteBufA 的值："+(char)byteBufA.getByte(i));
        }

        ByteBuf byteBufB = Unpooled.buffer();
        byteBufB.writeBytes("b".getBytes());
        for(int i=0;i<byteBufB.readableBytes();i++){
            System.out.println("byteBufB 的值："+(char)byteBufB.getByte(i));
        }
        // 创建 compositeByteBuf
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        // 拼接两个 byteBuf
        compositeByteBuf.addComponents(true,byteBufA,byteBufB);

        printBuf(compositeByteBuf);
    }


    static void printBuf(ByteBuf byteBuf){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i< byteBuf.writerIndex();i++) {
            stringBuilder.append((char)byteBuf.getByte(i));
        }
        System.out.println("compositeByteBuf 的值为："+stringBuilder);
    }
}
