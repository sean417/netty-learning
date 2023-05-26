package NIO;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    public static void main(String[] args) throws Exception{
            //1. 作为服务端，首先创建一个ServerSocket。
            ServerSocket serverSocket = new ServerSocket(9000);
            //2. 只要有客户端连接进来就分配一个线程来处理这个连接。
            while (true) {
                Socket socket = serverSocket.accept();
                // 启动新的工作线程
                new Worker(socket).start();
            }
    }
   // 工作线程
    static class Worker extends Thread {
        Socket socket;
        public Worker(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run()  {
            try {
                // 定义1kb的char[]数组。
                char[] buf = new char[1024];
                // 3.获取输入流。
                OutputStream out = socket.getOutputStream();
                InputStreamReader in = new InputStreamReader(socket.getInputStream());
                //读数据，无数据就阻塞
                int len = in.read(buf);
                // 4.循环读取客户端的数据
                while (len != -1) {
                    String request = new String(buf, 0, len);
                    System.out.println("["+Thread.currentThread().getName()+"] 服务端接收到了数据：" + request);
                    //5.处理完客户端发送的数据后，向客户端发送数据
                    out.write("收到了！！！".getBytes());
                    // 重新定义一个char[]数组
                    buf = new char[1024];
                    // 读数据，无数据就阻塞
                    len = in.read(buf);
                }
                //5.关闭
                out.close();
                in.close();
                in.close();
                socket.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
