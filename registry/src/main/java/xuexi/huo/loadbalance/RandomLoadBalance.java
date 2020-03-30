package xuexi.huo.loadbalance;

import java.util.List;
import java.util.Random;
/**
 *负载均衡的真正实现，这里用的是随机负载
 * @author huo
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    /**
     * 随机返回一台服务器，不建议使用这个方法来做负载均衡.
     * @param servicesAddress
     * @return
     */
    protected String selectConcreteNode(List<String> servicesAddress) {
        return servicesAddress.get(new Random().nextInt(servicesAddress.size()));
    }
}
