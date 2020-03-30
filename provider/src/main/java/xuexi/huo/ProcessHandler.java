package xuexi.huo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
/**
 *处理消费方的请求
 * @author huo
 */
public class ProcessHandler implements Runnable {
    private Socket socket;
    //应该在每台服务器里面维护着，要暴露的服务以及具体实现
    private Map<String,Object> serviceTable;

    public ProcessHandler(Socket socket, Map<String,Object> serviceTable) {
        this.socket = socket;
        this.serviceTable = serviceTable;
    }

    /**
     * 接受数据，交给invoke处理
     * 并把结果通过网络返回去
     */
    @Override
    public void run() {
        ObjectInputStream inputStream=null;
        ObjectOutputStream outputStream=null;

        try {
            /*
            报java.lang.NoSuchMethodException: java.lang.String.sayHello(java.lang.String)错误
             */
            inputStream = new ObjectInputStream(socket.getInputStream());
            ConsumerRequest consumerRequest =(ConsumerRequest) inputStream.readObject();
            Object result = invoke(consumerRequest);
            System.out.println("服务端的处理结果是："+result);
            //处理好之后，将结果返回
            outputStream=new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();
            //要用flush的不然客户端会报EOFException
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            Exception in thread "pool-3-thread-1" java.lang.NullPointerException  说明代码根本就没有执行到 outputStream=new ObjectOutputStream(socket.getOutputStream());
            //也进一步定位了问题代码范围
//                inputStream.close();
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 在这里才真正的实现了对消费方所需服务的真正实现
     * @param consumerRequest
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invoke(ConsumerRequest consumerRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("服务端开始被调用!");
        Object[] parameters = consumerRequest.getParameters();
        Class[] parametersType=new Class[parameters.length];
        for(int i=0;i<parameters.length;i++){
            parametersType[i]=parameters[i].getClass();
        }
        //这里相比于version1的改动在于，获取服务已经不是服务端指定的了，而是从服务列表中获取
        Object service = serviceTable.get(consumerRequest.getClassName());
        System.out.println(service);//service是127.0.0.1:12345，这样当然得不到HelloServiceImpl这个服务啊。说明serviceTable设计的右问题，在去改改
        //也正因为如此才报java.lang.NoSuchMethodException: java.lang.String.sayHello(java.lang.String)异常
        Method method = service.getClass().getMethod(consumerRequest.getMethodName(), parametersType);//
        System.out.println(method.getName());
        return method.invoke(service,parameters);
    }
}
