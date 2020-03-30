package xuexi.huo;
/**
 *模拟一台真正的服务器，为外界提供服务
 * @author huo
 */
public class RpcServer  {
    public static void main(String[] args) {
        /*ProviderServer providerServer=new ProviderServer();
        providerServer.publisher(new HelloServiceImpl(),12345);*/
        //现在应该是传注册中心过去，并把自己提供的服务绑上去。，不像以前是指定服务了
        IRegistryCenter iRegistryCenter=new RegistryCenterImpl();
        ProviderServer providerServer=new ProviderServer(iRegistryCenter,"127.0.0.1:12345");
        //这里可不可以扫描一个包，然后把包下的所有服务都绑定进来？就像通用Mapper那样
        //这样也不好，有些服务我只希望内部调用，而不是给外部用。
        providerServer.bind(new HelloServiceImpl());
        providerServer.pubilsher();
    }
}
