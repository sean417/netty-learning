package rpc.protocol;

import rpc.serialization.HessianSerialization;

import java.util.UUID;

public class HessianSerializationTest {

    public static void main(String[] args) throws Exception{
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName("TestClass");
        rpcRequest.setMethodName("sayHello()");
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"sean"});
        rpcRequest.setInvokerApplicationName("RpcClient");
        rpcRequest.setInvokerIp("127.0.0.1");

        // 把对象序列化成字节数组
        byte[] bytes = HessianSerialization.serialize(rpcRequest);
        // 打印字节数组长度
        System.out.println(bytes.length);

        // 把字节数组反序列化成对象
        RpcRequest deserializedRpcRequest =
                (RpcRequest) HessianSerialization.deserialize(bytes,RpcRequest.class);
        System.out.println(deserializedRpcRequest.toString());
    }
}
