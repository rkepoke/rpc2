package xuexi.huo;

import java.lang.reflect.Proxy;
/**
 *动态代理类，根据所需服务提供的类加载器和所要具备的一组接口，和自己实现的调用处理器创建一个动态代理对象
 * @author huo
 */
public class ProxyClient {
    /**
     * 生成代理对象
     * @param interfaceClass
     * @param iDiscoveryService
     * @param <T>
     * @return
     */
    public <T>  T clienProxy(Class<T> interfaceClass, IDiscoveryService iDiscoveryService) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},new RemoteInvocationHandler(iDiscoveryService));
    }
}
