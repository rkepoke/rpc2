# rpc2

服务提供方：会将自己想要暴露的服务，放在自己内部维护的一张服务列表中，然后通过注册中心把自己注册到zookeeper中，有
RpcServer
ProviderServer
ProcessHandler
RpcAnnotation
IHelloService
IHelloServiceImpl

服务消费方：会通过注册中心发现自己想要的服务，通过负载均衡算法，获取一个具体的服务器来提供服务，完成自己的业务
RpcClient
IHelloService
ProxyClient
RemoteInvocationHandler
Transport
ConsumerRequest

注册中心：接连zookeeper，创建服务节点和服务器地址+端口；监听节点获取服务器。
IRegistryService
RegistryCenterConfig
RegistryCenterImpl
IDiscovertService
DiscoveryServiceImpl
loadBalance
AbstractLoadBalance
RandomLoadBalance

以下是参考到的一些资料：
dubbo架构：
https://blog.csdn.net/weixin_42288131/article/details/81282352

zk常用api操作：
https://www.jianshu.com/p/4322a5ecad39
https://www.iteye.com/blog/shift-alt-ctrl-1983073

理解注解：
https://www.cnblogs.com/hujunzheng/p/5719611.html

使用pathChildrenCache：
https://www.iteye.com/blog/shift-alt-ctrl-1983945

dubbo的负载均衡设计;
https://www.cnblogs.com/wyq178/p/9822731.html



