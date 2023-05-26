package netty.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class SliceDemo {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9,100);
        buffer.writeBytes(new byte[]{1,2,3,4});
        // 获取可读部分的切片
        ByteBuf sliceA = buffer.slice();
        // 设定切片的起始位置和切片长度
        ByteBuf sliceB = buffer.slice(0,2);
        System.out.println(sliceA.getByte(0));
        System.out.println(buffer.getByte(0));
        // 修改切片后，源 byteBuf 也会随之改变
        sliceA.setByte(0,9);
        System.out.println(sliceA.getByte(0));
        System.out.println(buffer.getByte(0));
    }
}
