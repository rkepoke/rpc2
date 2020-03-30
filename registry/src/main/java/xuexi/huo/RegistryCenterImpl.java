package xuexi.huo;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 *实现服务的注册
 * @author huo
 */
@Slf4j
public class RegistryCenterImpl implements IRegistryCenter {
    //首先就是创建与zk的连接，之后就是往zk里创建节点，表示服务节点
    //重连策略
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

    /*private CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
            .connectString(RegistryCenterConfig.CONNECTING_STR)
            .connectionTimeoutMs(RegistryCenterConfig.SESSION_TIMEOUT)
            .retryPolicy(retryPolicy)
            .build();*/

    //我学习网站上的写法，报错 Cannot resolve symbol 'start'  也就是没法使用curatorFramework这个变量
    ///F:/workspce_idea1/rpc/registry/src/main/java/xuexi/huo/RegistryCenterImpl.java:[20,27] 需要<标识符>
//    curatorFramework.start();
    /*
    找到原因：类里的语句只能是定义变量或方法! 可执行的语句只能在方法里或者{}（代码块）里。
    上面的语句不是变量定义语句，不是方法定义，不在代码块{}里，所以报错！
    导致错误的原因1是代码没在方法里显示
    解决，放在代码块里
     */
    //Modifier 'private' not allowed here,只能把接受变量提取出来了
    /**
     * 完成与zk的连接
     */
    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(RegistryCenterConfig.CONNECTING_STR)
                .connectionTimeoutMs(RegistryCenterConfig.SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();//ok
    }

    /**
     *  注册服务，先检查，在注册
     * @param serviceName
     * @param serviceAddress
     */
    public void registry(String serviceName, String serviceAddress) {
        String serviceNodePath = RegistryCenterConfig.NAME_SPACE + "/" + serviceName;
        try {
            /*
            Caused by: org.apache.zookeeper.KeeperException$UnimplementedException: KeeperErrorCode = Unimplemented for /RpcNode/xuexi.huo.IHelloService/127.0.0.1:12345
            会报这样的错误，改革pom.xml的版本依赖即可
             */


            //第一次连接的时候，它还没创建，所以要检查创建，之后在连接就直接添加节点即可，不必在创建了
            if (curatorFramework.checkExists().forPath(serviceNodePath) == null) {
                //创建一个路径，并规定了节点的类型和初始值
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceNodePath, RegistryCenterConfig.DEFAULT_VALUE);
            }
            //以上是创建服务概述，接着就是创建 具体服务了
            String concreteNodePath = serviceNodePath + "/" + serviceAddress;
            String hasNode = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(concreteNodePath, RegistryCenterConfig.DEFAULT_VALUE);
            System.out.println("节点创建"+hasNode);
            /*
            SLF4J: Class path contains multiple SLF4J bindings.
            这是因为zookeeper中和Slf4j的jar包冲突，去掉一个依赖就好了
             */
//            log.info("节点创建：{}", hasNode);
        } catch (Exception e) {
            throw new RuntimeException("创建节点异常。", e);
        }
    }
}
