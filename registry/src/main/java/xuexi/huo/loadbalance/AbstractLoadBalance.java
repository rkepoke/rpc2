package xuexi.huo.loadbalance;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
/**
 *负载均衡的一个出现类实现，先做一些简单的判断
 * @author huo
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    /**
     * 先做初步的处理
     * @param servicesAddress
     * @return
     */
    public String doSelect(List<String> servicesAddress){
        if(CollectionUtils.isEmpty(servicesAddress)){
            return null;
        }else if(servicesAddress.size()==1){
            return servicesAddress.get(0);
        }
        return selectConcreteNode(servicesAddress);
    }

    /**
     * 初步通过之后，就交给具体的负载均衡处理类去处理
     * @param serviceTable
     * @return
     */
    protected abstract String selectConcreteNode(List<String> serviceTable);
}
