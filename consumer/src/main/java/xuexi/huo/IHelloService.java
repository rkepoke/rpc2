package xuexi.huo;
/**
 *服务调用的  双方都要知道的共同的接口；里面可以定义各种功能服务，这里知识简单模拟，所以就写了一个
 * @author huo
 */
public interface IHelloService {
    /**
     * 消费方要知道提供提供哪些服务
     * @param name
     * @return
     */
    public String sayHello(String name);
}
