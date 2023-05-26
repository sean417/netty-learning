package NIO;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class BioClientLongConnection {

    public static void main(String[] args) throws Exception{
        // 1.尝试连接
        Socket socket = new Socket("localhost",9000);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        InputStreamReader inReader = new InputStreamReader(in);

        while(true){
            try{
                // 2.发送数据
                out.write("你好".getBytes());
                // 3.接收数据
                char[] buf = new char[1024];
                //读数据，无数据就阻塞
                int len = inReader.read(buf);
                String request = new String(buf,0,len);
                System.out.println("客户端接收到了响应："+request);
                Thread.sleep(1000);
            } catch (Exception exception){
                // 4.关闭资源
                inReader.close();
                in.close();
                out.close();
                socket.close();// 四次挥手断开连接
            }
        }
    }
}
