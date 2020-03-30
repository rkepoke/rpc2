package xuexi.huo.loadbalance;

import java.util.List;
/**
 *定义负载均衡的顶层接口
 * @author huo
 */
public interface LoadBalance {
    /**
     * 定义负载均衡的方法
     * @param servicesAddress
     * @return
     */
    public String doSelect(List<String> servicesAddress);
}
