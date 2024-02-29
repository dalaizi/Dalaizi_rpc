package medium;

import annotation.Remote;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.HashMap;

@Component
public class InitMedium implements BeanPostProcessor {  //在所有放入IOC容器的bean初始化前后操作

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;  //这里不能返回null，否则配置类会报错 Delegate listener must not be null（委托监听器不能为空）
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if(bean.getClass().isAnnotationPresent(Controller.class)) { //把标有Controller注解的bean中的方法都放在Medium的Map中
        if(bean.getClass().isAnnotationPresent(Remote.class)) { //把标有Controller注解的bean中的方法都放在Medium的Map中
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method m : methods) {
                //key
//                String key = bean.getClass().getName() + "." + m.getName();
                String key = m.getName();
                HashMap<String, BeanMethod> beanMap = Medium.mediaMap;
                //value
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(m);
                //put k v in beanMap
                beanMap.put(key, beanMethod);
                System.out.println(key);

            }
        }
        return bean;
    }


}
