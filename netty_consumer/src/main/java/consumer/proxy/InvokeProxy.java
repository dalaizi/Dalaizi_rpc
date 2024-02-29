package consumer.proxy;

import consumer.annotation.RemoteInvoke;
import consumer.core.TCPClient;
import consumer.model.ClientRequest;
import consumer.model.Response;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

@Component
public class InvokeProxy implements BeanPostProcessor {
    public static Enhancer enhancer = new Enhancer();

    //对属性的所有方法和属性类型放入到HashMap中
    private void putMethodClass(HashMap<Method, Class> methodmap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for(Method method : methods){
            methodmap.put(method, field.getType());
        }

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String arg1) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String arg1) throws BeansException {
//		System.out.println(bean.getClass().getName());
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(RemoteInvoke.class)){
                field.setAccessible(true);  //指示反射的对象在使用时应该取消 Java 语言访问检查，由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的

//				final HashMap<Method, Class> methodmap = new HashMap<Method, Class>();
//				putMethodClass(methodmap,field);
//				Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[]{field.getType()});  //设置需要动态代理的接口 TestRemote
                enhancer.setCallback(new MethodInterceptor() {   //重写intercept拦截方法，并选择拦截哪些方法

                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        ClientRequest clientRequest = new ClientRequest();
                        clientRequest.setContent(args[0]);  //把要调用的方法 TestRemote.testUser() 中的参数放在请求包
//						String command= methodmap.get(method).getName()+"."+method.getName();
                        String command = method.getName();//把要调用的方法名字放在请求中作为命令
//						System.out.println("InvokeProxy中的Command是:"+command);
                        clientRequest.setCommand(command);

                        Response response = TCPClient.send(clientRequest); //代理把请求发送给远程服务器，并接受响应
                        System.out.println("代理已发送请求" + clientRequest.getCommand());
                        return response;
                    }
                });
                try {
                    field.set(bean, enhancer.create());  //bean.field = value 即 RemoteInvokeTset.userRemote = 代理
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return bean;
    }
}
