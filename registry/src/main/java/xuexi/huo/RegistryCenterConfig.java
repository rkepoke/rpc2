package xuexi.huo;

/**
 *模拟配置文件了，这样就可以动态的配置连接信息。增强了代码的灵活性
 * @author huo
 */
public interface RegistryCenterConfig {

    /**
     * zk的地址和端口号
     */
    public String CONNECTING_STR="192.168.135.128:2181";

    /**
     * 会话超时连接  时间
     */
    public int SESSION_TIMEOUT=5000;

    /**
     * 创建命名空间，用于表示该节点是用做注册中心的，其下放着各种服务，以及对应的服务器
     */
    public String NAME_SPACE="/RpcNode";

    /**
     * 用于在创建一个节点时，赋予默认数据
     */
    public byte[] DEFAULT_VALUE="0".getBytes();
}
