package com.sean.netty.rpc.protocol;

import java.io.Serializable;
import java.util.Arrays;

public class RpcRequest implements Serializable {

    private String requestId;
    // 需要调用的接口
    private String serviceInterfaceClass;
    // 需要调用的方法
    private String methodName;
    // 方法的参数类型
    private Class[] parameterTypes;
    // 实际的参数值
    private Object[] args;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceInterfaceClass() {
        return serviceInterfaceClass;
    }

    public void setServiceInterfaceClass(String serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", serviceInterfaceClass='" + serviceInterfaceClass + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
