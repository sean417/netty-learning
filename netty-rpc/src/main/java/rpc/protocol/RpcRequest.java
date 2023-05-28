package rpc.protocol;

import java.io.Serializable;
import java.util.Arrays;

public class RpcRequest implements Serializable {

    private String requestId;
    // 需要调用的类
    private String className;
    // 需要调用的方法
    private String methodName;
    // 方法的参数类型
    private Class[] parameterTypes;
    // 方法参数名
    private Object[] args;
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
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                ", invokerApplicationName='" + invokerApplicationName + '\'' +
                ", invokerIp='" + invokerIp + '\'' +
                '}';
    }
}
