package netty.referenceCount;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PooledByteBufReferenceCounted {

    public static void main(String[] args) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println("初始化后引用计数器："+byteBuf.refCnt());

        // 增加一次引用计数
        byteBuf.retain();
        System.out.println("调用retain()后引用计数器："+byteBuf.refCnt());

        // 减少一次引用计数
        byteBuf.release();
        System.out.println("调用 release() 后引用计数器："+byteBuf.refCnt());

        // 减少一次引用计数
        byteBuf.release();
        System.out.println("调用 release() 后引用计数器："+byteBuf.refCnt());

        // 报错，byteBuf.refCnt()=0 后就不能再使用byteBuf了
        byteBuf.retain();
        System.out.println("调用retain()后引用计数器："+byteBuf.refCnt());

    }
}
