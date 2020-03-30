package xuexi.huo;

/**
 *这是注册中心的接口；因为注册中心不止zk，也有其它的选择，所以这里不能写死
 * @author huo
 */
public interface IRegistryCenter {
    /**
     * 实现方法注册，只接受服务和相应的地址，不绑定具体的服务，为的是单一职责
     * @param serviceName
     * @param serviceAddress
     */
    public void registry(String serviceName, String serviceAddress);
}
