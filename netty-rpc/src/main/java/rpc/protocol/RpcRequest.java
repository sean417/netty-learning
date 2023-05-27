package com.sean.rpc.protocol;

import java.io.Serializable;
import java.util.Arrays;

public class RpcRequest implements Serializable {

    private String requestId;
    // 需要调用的类
    private String className;
    // 需要调用的方法
    private String methodName;
    // 方法的参数类型
    private String[] parameterClasses;
    // 方法参数名
    private Object[] parameters;
    // 调用者的应用名称
    private String invokerApplicationName;
    // 调用者的 IP
    private String invokerIp;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParameterClasses() {
        return parameterClasses;
    }

    public void setParameterClasses(String[] parameterClasses) {
        this.parameterClasses = parameterClasses;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getInvokerApplicationName() {
        return invokerApplicationName;
    }

    public void setInvokerApplicationName(String invokerApplicationName) {
        this.invokerApplicationName = invokerApplicationName;
    }

    public String getInvokerIp() {
        return invokerIp;
    }

    public void setInvokerIp(String invokerIp) {
        this.invokerIp = invokerIp;
    }


    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterClasses=" + Arrays.toString(parameterClasses) +
                ", parameters=" + Arrays.toString(parameters) +
                ", invokerApplicationName='" + invokerApplicationName + '\'' +
                ", invokerIp='" + invokerIp + '\'' +
                '}';
    }
}
