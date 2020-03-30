package xuexi.huo;

import java.io.Serializable;

/**
 * 这个类是实现对数据的封装，方便数据在网络中传输的，同时要实现序列化接口
 * @author huo
 */

public class ConsumerRequest implements Serializable {
    private String className;
    private String methodName;
    private Object[] parameters;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
