package com.sean.netty.rpc.reference;

public class ReferenceConfig {

    private static final long DEFAULT_TIMEOUT = 5000;
    private static final String DEFAULT_SERVICE_HOST = "127.0.0.1";
    private static final int DEFAULT_SERVICE_PORT = 8998;

    private Class serviceInterfaceClass;
    private String servceHost;
    private int servicePort;
    private long timeout;


    public ReferenceConfig(Class serviceInterfaceClass) {
        this(serviceInterfaceClass,DEFAULT_SERVICE_HOST,DEFAULT_SERVICE_PORT,DEFAULT_TIMEOUT);
    }

    public ReferenceConfig(Class serviceInterfaceClass, String servceHost, int servicePort) {
        this(serviceInterfaceClass,servceHost,servicePort,DEFAULT_TIMEOUT);
    }

    public ReferenceConfig(Class serviceInterfaceClass, String servceHost, int servicePort, long timeout) {
        this.serviceInterfaceClass = serviceInterfaceClass;
        this.servceHost = servceHost;
        this.servicePort = servicePort;
        this.timeout = timeout;
    }

    public Class getServiceInterfaceClass() {
        return serviceInterfaceClass;
    }

    public void setServiceInterfaceClass(Class serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    public String getServceHost() {
        return servceHost;
    }

    public void setServceHost(String servceHost) {
        this.servceHost = servceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
