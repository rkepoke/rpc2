package xuexi.huo;
/**
 *远程调用服务的真正实现，加上注解是表明，这个服务是我想要暴露出去的，去掉注解表明这个服务是我不想暴露的，就是自己内部自己调用的
 * 辅助完成其他功能
 * @author huo
 */
@RpcAnnotion(value = IHelloService.class)
public class HelloServiceImpl implements IHelloService {
    /**
     * 服务的具体实现
     * @param name
     * @return
     */
    public String sayHello(String name) {
        return name+"你好啊！";
    }
}
