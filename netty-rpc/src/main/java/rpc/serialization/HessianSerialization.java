package com.sean.rpc.serialization;

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

        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    public static Object deserialize(byte[] bytes,Class clazz) throws IOException{
        ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput((byteArrayInputStream));
        Object object = hessianInput.readObject(clazz);

        return object;
    }
}
