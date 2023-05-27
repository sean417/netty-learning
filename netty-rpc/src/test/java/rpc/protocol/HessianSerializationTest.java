package com.sean.rpc.protocol;

import com.sean.rpc.serialization.HessianSerialization;

import java.util.UUID;

public class HessianSerializationTest {

    public static void main(String[] args) throws Exception{
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName("TestClass");
        rpcRequest.setMethodName("sayHello()");
        rpcRequest.setParameterClasses(new String[]{"String"});
        rpcRequest.setParameters(new Object[]{"sean"});
        rpcRequest.setInvokerApplicationName("RpcClient");
        rpcRequest.setInvokerIp("127.0.0.1");

        byte[] bytes = HessianSerialization.serialize(rpcRequest);
        System.out.println(bytes.length);

        RpcRequest deserializedRpcRequest =
                (RpcRequest) HessianSerialization.deserialize(bytes,RpcRequest.class);
        System.out.println(deserializedRpcRequest.toString());
    }
}
