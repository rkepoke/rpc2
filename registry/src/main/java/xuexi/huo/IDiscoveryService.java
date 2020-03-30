package xuexi.huo;
/**
 *发现服务的顶层接口
 * @author huo
 */
public interface IDiscoveryService {
    /**
     * 根据消费方要的服务，返回一个具体的服务器地址
     * @param serviceName
     * @return
     */
    public String discover(String serviceName);
}
