package xuexi.huo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 *实现了建立，连接，管理网络通信
 * @author huo
 */
public class Transport {
    private String serviceHost;

    public Transport(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    /**
     * 得到连接地址，进行远程服务器的连接
     * @return
     */
    public Socket getSocket(){
        String host=serviceHost.split(":")[0];
        String str_port=serviceHost.split(":")[1];
        System.out.println("准备连接："+host+":"+str_port);
        Integer port = Integer.valueOf(str_port);
        Socket socket= null;
        try {
            socket = new Socket(host,port);
            return socket;
        } catch (IOException e) {
           throw new RuntimeException("连接失败了。",e);
        }

    }

    /**
     * 把请求发送过去，然后接受返回的结果，交给调用处理器做进一步的处理
     * @param consumerRequest
     * @return
     */
    public Object send(ConsumerRequest consumerRequest) {
        Socket socket = null;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            socket=getSocket();
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(consumerRequest);
            outputStream.flush();
            /*
            java.io.EOFException运行时报了这个异常。
             */
            inputStream = new ObjectInputStream(socket.getInputStream());

            Object result = inputStream.readObject();
           // System.out.println(result);
            inputStream.close();
            outputStream.close();
            return result;

        } catch (Exception e) {
            throw new RuntimeException("发起远程调用异常：",e);
        }finally {
                //我在网上看了一圈也没发现什么比较好的解决办法，就在想是不是可能是我先关了socket才导致它报错的，应该先关其他的流，最后才关socket流
//                socket.close();
                if(socket!=null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
