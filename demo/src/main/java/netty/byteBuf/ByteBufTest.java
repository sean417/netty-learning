package netty.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufTest {
    public static void main(String[] args) {
             ByteBuf byteBuf =  init();
             getByteBuf(byteBuf);
             System.out.println("Buffer不改变读索引"+ byteBuf);
             readByteBuf(byteBuf);
             System.out.println("Buffer不改变读索引"+ byteBuf);
    }

    private static ByteBuf init(){
        // 初始容量为9，最大容量为100 的缓冲区
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9,100);
        System.out.println("Buffer的初始化:"+ buffer);
        buffer.writeBytes(new byte[]{1,2,3,4});
        System.out.println("Buffer写入操作:"+ buffer);
        return buffer;

    }


    // 读字节，不改变指针
    private static void getByteBuf(ByteBuf buffer){
        for(int i=0;i<buffer.readableBytes();i++){
            System.out.println("读"+i+"个字节："+buffer.getByte(i));
        }
    }

    // 读字节，改变指针
    private static void readByteBuf(ByteBuf buffer){
        while (buffer.isReadable()){
            System.out.println("取第一个字节："+ buffer.readByte());
        }
    }
}
