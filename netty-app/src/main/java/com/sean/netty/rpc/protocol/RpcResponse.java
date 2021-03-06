package com.sean.netty.rpc.protocol;

import java.io.Serializable;

public class RpcResponse implements Serializable {

    public static final Boolean SUCCESS = true;
    public static final Boolean FAILURE = false;
    // 响应对应的请求id
    public String requestId;
    // 响应是否成功
    public Boolean isSuccess;
    // 响应结果
    public Object result;
    // 响应异常
    public Throwable exception;
    // 响应是否超时
    public Boolean timeout;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Boolean getTimeout() {
        return timeout;
    }

    public void setTimeout(Boolean timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", isSuccess=" + isSuccess +
                ", result=" + result +
                ", exception=" + exception +
                ", timeout=" + timeout +
                '}';
    }
}
