package medium;

import java.lang.reflect.Method;

public class BeanMethod {  //存储着 （类＋方法） 的 一个类
    private Object bean;
    private Method method;

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
