package xuexi.huo;
/**
 *服务方与消费方一同协商的公共接口
 * @author huo
 */
public interface IHelloService {
    /**
     * 这里面定义这个服务器，要提供的各种方法
     * @param name
     * @return
     */
    public String sayHello(String name);
}
