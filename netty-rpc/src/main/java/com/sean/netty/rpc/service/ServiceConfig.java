package rpc.service;

public class ServiceConfig {
        // 服务名称
        private String serviceName;
        // 监听端口号
        private int port;
        // 服务的接口类型
        private Class serviceInterfaceClass;


    public ServiceConfig(String serviceName, int port, Class serviceInterfaceClass) {
        this.serviceName = serviceName;
        this.port = port;
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Class getServiceInterfaceClass() {
        return serviceInterfaceClass;
    }

    public void setServiceInterfaceClass(Class serviceInterfaceClass) {
        this.serviceInterfaceClass = serviceInterfaceClass;
    }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "serviceName='" + serviceName + '\'' +
                ", port=" + port +
                ", serviceInterfaceClass=" + serviceInterfaceClass +
                '}';
    }
}
