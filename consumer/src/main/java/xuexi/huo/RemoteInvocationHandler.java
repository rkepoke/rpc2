package xuexi.huo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 *在这里实现了远程调用
 * @author huo
 */
public class RemoteInvocationHandler implements InvocationHandler {
    private IDiscoveryService iDiscoveryService;

    public RemoteInvocationHandler(IDiscoveryService iDiscoveryService) {
        this.iDiscoveryService = iDiscoveryService;
    }

    /**
     * 这里只是进行了方法的增强，先封装了数据，然后远程调用，返回结果还是可以做进一步的处理的
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //封装数据
        ConsumerRequest consumerRequest=new ConsumerRequest();
        //最好是使用method.getDeclaringClass(),因为你要的方法可能是私有的
        consumerRequest.setClassName(method.getDeclaringClass().getName());
        consumerRequest.setMethodName(method.getName());
        consumerRequest.setParameters(args);
        //寻找服务
        String serviceHost = iDiscoveryService.discover(method.getDeclaringClass().getName());//是根据服务名,获得一台能提供服务的机器，
        Transport transport=new Transport(serviceHost);//这个的serviceHost就是一个具体的地址
        return transport.send(consumerRequest);//这里可以不直接返回结果，对结果在做进一步的解析，但这个小demo中的方法很简单，所以就直接返回了。
        //远程调用层的基本功能作用还是体现出来了
    }
}
