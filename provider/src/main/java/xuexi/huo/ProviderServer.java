package xuexi.huo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 *绑定我要暴露的服务，监听指定的端口，来一个请求，就开一个线程去处理。
 * @author huo
 */
@Slf4j
public class ProviderServer {
    private IRegistryCenter iRegistryCenter;
    private String serviceAddress;
    //创建一个服务列表，因为一个服务器可以对外暴露多个服务。比如morningHello(),afternoonHello(),eveningHello().等等,所以要用列表来装啊
    //服务列表 ，维护一组:服务名和对应的服务
    private  Map<String, Object> SERVICE_TABLE=null;

    public ProviderServer(IRegistryCenter iRegistryCenter, String serviceAddress) {
        this.iRegistryCenter = iRegistryCenter;
        this.serviceAddress = serviceAddress;
    }

    private ExecutorService executor= Executors.newCachedThreadPool();

    /**
     * 监听端口，来一个请求，就开一个线程去处理
     */
    public void pubilsher() {
        int port = Integer.valueOf(serviceAddress.split(":")[1]);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new ProcessHandler(socket,SERVICE_TABLE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 绑定要提供的服务 和具体的实现,好让处理线程去真正的调用相应的服务来完成消费方的请求
     * @param concreteService
     */
    public void bind(Object... concreteService) {//因为不知道具体服务，所以只能用Object接受。以确保通用
        /*
        一开始的想法是，bind绑定服务的时候就直接通过注册中心进行注册就行了。
        但在开启线程处理请求的时候发现，应该传一个服务列表才符合对外提供多个服务的概念
        如果我不给一张服务表的话，那以后消费者怎么去拿它想要的服务。所以啊，每个节点都应该维护一张服务表
        以上思想是错的，一开始没弄明白，我要暴露的服务和我要注册哪些信息到zookeeper中没想明白
         */
//        iRegistryCenter.registry(concreteService.getClass().getName(),serviceAddress);

        //考虑到以后服务的变更，使用注解来增加灵活性
//        serviceTable.put(concreteService.getClass().getName(),concreteService);
        //模拟懒加载
        if(SERVICE_TABLE==null||SERVICE_TABLE.size()==0){
            SERVICE_TABLE=new HashMap<>();
        }
        for (Object service : concreteService) {
            RpcAnnotion annotation = service.getClass().getAnnotation(RpcAnnotion.class);
            if (annotation == null) {
                continue;
            }
            //如果这个服务是要暴露的服务，那么我就把它添加到服务列表
//            serviceTable.put(service.getClass().getName(),serviceAddress);代码的可读性差
//            String serviceName = service.getClass().getName();致命错误，这样写消费方永远都不能通过服务名拿到服务。
//            String serviceName = annotation.getClass().getName();有写错了，这样拿到的是代理对象的名字，com.sun.proxy.$Proxy1
            //看@Component的源代码有所明白
            String serviceName = annotation.value().getName();
//            SERVICE_TABLE.put(serviceName, serviceAddress);//服务列表是 每个机械自己维护的，不是有zk维护的
            SERVICE_TABLE.put(serviceName, service);//服务列表是 每个机械自己维护的，不是有zk维护的,所以这里应该是服务而不是地址
        }
        //绑定完成就可以注册了
        SERVICE_TABLE.keySet().forEach(serviceName->{
            iRegistryCenter.registry(serviceName,serviceAddress);
            System.out.println("注册服务,serviceName:"+serviceName+",serviceAddress:"+serviceAddress);
        });
    }


}
