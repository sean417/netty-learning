package com.sean.netty.rpc.reference;

public class NettyRpcReadTimeoutException extends RuntimeException{
    public NettyRpcReadTimeoutException(String message){
        super(message);
    }

}
