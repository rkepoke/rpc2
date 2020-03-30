package xuexi.huo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import xuexi.huo.loadbalance.LoadBalance;
import xuexi.huo.loadbalance.RandomLoadBalance;

import java.util.List;
/**
 *发现服务
 * @author huo
 */
public class DiscoveryServiceImpl implements IDiscoveryService {
    //zookeeperAddress没用到，因为当前系统很简单，这是留给以后扩展用的
    private String zookeeperAddress;

    CuratorFramework curatorFramework;

    //获取服务列表的啊  这里得到的就是地址值了
    List<String> servicesAddress;

    /**
     * 获取所需服务所在节点，通过监听不断获取新的服务列表（因为可能会添加新的服务器，也有可能会出现服务器宕机），
     * 所以要不断获取最新列表
     * 得到列表后就是，通过负载均衡哪一个具体的服务器地址，
     * 然后消费方根据这个地址去做远程服务的调用
     * @param serviceName
     * @return
     */
    public String discover(String serviceName) {
        String serviceNode = RegistryCenterConfig.NAME_SPACE + "/" + serviceName;
        try {
            servicesAddress = curatorFramework.getChildren().forPath(serviceNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //然后对服务节点进行监听
        registryWatcher(serviceNode);
        //对服务列表做负载均衡
        LoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.doSelect(servicesAddress);
    }

    private void registryWatcher(final String serviceNode) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, serviceNode, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                servicesAddress = curatorFramework.getChildren().forPath(serviceNode);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        //监听也是一个单独的线程去干的，所以也要启动，不然会没效果的
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DiscoveryServiceImpl(String zookeeperAddress) {
        this.zookeeperAddress = zookeeperAddress;
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(RegistryCenterConfig.CONNECTING_STR)
                .connectionTimeoutMs(RegistryCenterConfig.SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(5000, 10))
                .build();
        curatorFramework.start();
    }
}
