package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioServer {

    private static ByteBuffer readBuffer;
    private static Selector selector;

    public static void main(String[] args)  throws Exception{
          // 服务端的初始化
          init();
          listen();
    }

    private static void init(){
        // 读取请求数据的 Buffer
        readBuffer = ByteBuffer.allocate(128);
        ServerSocketChannel serverSocketChannel;

        try{
            // 1.打开服务端
            serverSocketChannel = ServerSocketChannel.open();
            // 配置为非阻塞IO
            serverSocketChannel.configureBlocking(false);
            // 设置端口，
            serverSocketChannel.socket().bind(new InetSocketAddress(9000),100);
            // 打开 多路复用选择器
            selector = Selector.open();
            // 2.把 serverSocketChannel 注册到 selector 上。 监听 serverSocketChannel 的连接请求事件，与各个客户端建立连接请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void listen(){
        while (true){
            try{
                // 3. selector查看是否有注册在 selector 上的 Channel 有网络事件发生。这个方法是阻塞的。
                selector.select();
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
                // 4. 轮询 SelectionKey 集合里的 SelectionKey，一个 SelectionKey 代表一个网络事件。
                while (keysIterator.hasNext()){
                    SelectionKey key = (SelectionKey) keysIterator.next();
                    keysIterator.remove();
                    handleKey(key);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void handleKey(SelectionKey key){
        SocketChannel channel = null;
        // 5. 判断网络事件的类型并做出相应的处理
        try {
            // 如果是客户端要求连接的请求
            if(key.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                // 通过 TCP 三次握手，建立和获取获取客户端和服务器的连接SocketChannel
                channel = serverSocketChannel.accept();
                channel.configureBlocking(false);
                // 注册网络读事件
                channel.register(selector,SelectionKey.OP_READ);
                //如果是可以网络读的事件
            }else if(key.isReadable()){
                channel = (SocketChannel) key.channel();
                readBuffer.clear();// postion 变为0，limit = capacity,也就是复位操作，准备把数据写入Buffer了
                int count = channel.read(readBuffer); // 通过 Socket 来读取数据并把数据写入 Buffer 中。

                if(count > 0){
                    readBuffer.flip();// 开始从Buffer读数据。
                    String request = StandardCharsets.UTF_8.decode(readBuffer).toString();
                    System.out.println("Server receive the request: "+request);
                    channel.register(selector, SelectionKey.OP_WRITE);
                    String response = "Server accept request";
                    channel.write(ByteBuffer.wrap(response.getBytes()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
