package com.sean.netty.rpc.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerialization {

    // 序列化一个对象
    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(byteArrayOutputStream);
        ho.writeObject(object);
        // 序列化成二进制字节数组
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }
    // 反序列化
    public static Object deserialize(byte[] bytes,Class clazz) throws IOException{
        ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput((byteArrayInputStream));
        // 反序列化成对象
        Object object = hessianInput.readObject(clazz);

        return object;
    }
}
