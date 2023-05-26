package NIO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioClient {
    public static void main(String[] args) throws Exception{
        // 启动十个线程，模拟十个客户端。
        for(int i=0;i<10;i++){
            new Worker().start();
        }
    }

    static class Worker extends Thread{

        @Override
        public void run() {
            SocketChannel channel = null;
            Selector selector = null;
            try{
                // 1.创建一个 SocketChannel,用来与服务端连接，并实现网络读写操作。
                channel = SocketChannel.open();
                channel.configureBlocking(false);
                // 指定网络地址，通过三次握手实现 TCP 连接
                channel.connect(new InetSocketAddress("localhost",9000));
                // 创建一个 selector 对象， 并把 SocketChannel 注册到 selector 上，并监听 请求连接事件 OP_CONNECT
                selector = Selector.open();
                channel.register(selector, SelectionKey.OP_CONNECT);
                // 2. 遍历网络事件
                while (true){
                    // 查找收到的网络事件
                    selector.select();
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()){
                        SelectionKey key = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        // 3. 判断是否可以连接
                        if(key.isConnectable()){
                            // 如果连接成功了
                            if(channel.finishConnect()){
                                // 监听网络读事件
                                key.interestOps(SelectionKey.OP_READ);
                                // 向服务端发送数据
                                channel.write(ByteBuffer.wrap("hello,I'm client".getBytes()));
                            }else {
                                key.cancel();
                            }
                            // 4. 如果有数据需要读取
                        }else if(key.isReadable()){
                            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                            // 把数据写入 buffer
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            // 把数据读出来。
                            String response = StandardCharsets.UTF_8.decode(byteBuffer).toString();
                            System.out.println("["+Thread.currentThread().getName()+"] receive response:"+response);
                            Thread.sleep(5000);
                            // 向服务端发送数据
                            channel.write(ByteBuffer.wrap("hello".getBytes()));
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(channel != null){
                    try{
                        channel.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
