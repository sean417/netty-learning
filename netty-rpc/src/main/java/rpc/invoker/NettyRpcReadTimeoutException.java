package rpc.invoker;

public class NettyRpcReadTimeoutException extends RuntimeException{
    public NettyRpcReadTimeoutException(String message){
        super(message);
    }

}
