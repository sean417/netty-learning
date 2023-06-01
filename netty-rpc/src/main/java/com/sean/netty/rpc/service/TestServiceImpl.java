package rpc.service;

public class TestServiceImpl implements TestService{

    public String sayHello(String name){
        return "hello, "+ name;
    }
}
