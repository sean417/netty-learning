package netty.zeroCopy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class WrapDemo {

    public static void main(String[] args) {
        String message= "hello";
        byte[] bytes = message.getBytes();

        ByteBuf byteBufWrap = Unpooled.wrappedBuffer(bytes);
        for(int i=0;i<byteBufWrap.readableBytes();i++){
            System.out.println("byteBufWrap 的值："+(char)byteBufWrap.getByte(i));
        }
    }
}
