package xuexi.huo;
/**
 *模拟客户端，做业务处理，需要调用远程服务
 * @author huo
 */
public class RpcClient {
    public static void main(String[] args) {
       /* ProxyClient proxy=new ProxyClient();
//        HelloService helloService=null;
//        helloService=proxy.clientProxy(helloService,"127.0.0.1",12345);
        HelloService helloService = proxy.clientProxy(HelloService.class,"127.0.0.1",12345);
        String result = helloService.sayHello("小小");
        System.out.println(result);*/
        ProxyClient proxy = new ProxyClient();
        IDiscoveryService iDiscoveryService = new DiscoveryServiceImpl(RegistryCenterConfig.CONNECTING_STR);
        IHelloService helloService = proxy.clienProxy(IHelloService.class, iDiscoveryService);
        Object result = helloService.sayHello("小小");
        System.out.println(result);
    }
}
