package annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})  //用于描述注解的使用范围（即：被描述的注解可以用在什么地方
@Retention(RetentionPolicy.RUNTIME)  //表示需要在什么级别保存该注释信息，用于描述注解的生命周期（即：被描述的注解在什么范围内有效）
@Documented
@Component
public @interface Remote {
    String value() default "";
}
