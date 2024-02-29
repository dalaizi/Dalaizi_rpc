import consumer.annotation.RemoteInvoke;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.TestRemote;
import user.User;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //为了让测试在Spring容器环境下执行
@ContextConfiguration(classes=RemoteInvokeTest.class)
@ComponentScan("\\")     //扫描文件，发现 InvokeProxy 带 @Component 注解，把它实例化放在 IOC 容器中
public class RemoteInvokeTest {
 //   public static List<User> list = new ArrayList<User>();
    @RemoteInvoke   //在客户端这里看来，调用时就像是在用自己本地的一个接口TestRemote
    public static TestRemote userRemote;  //注入的属性是TestRemote的代理，在执行方法时代理会拦截并添加自己的增强逻辑（读取参数构造请求，发送给远程服务器，接收响应）
   // public static User user;
    //public static Long count = 0l;

//    static{
//        user = new User();
//        user.setId(1000);
//        user.setName("张三");
//    }
    @Test
    public void testSaveUser(){
        //构建user
        User user = new User();
        user.setId(1000);
        user.setName("张三");
        //发送user
        userRemote.testUser(user);
		Long start = System.currentTimeMillis();
		for(int i=1;i<1000000;i++){
			userRemote.testUser(user);
		}
		Long end = System.currentTimeMillis();
		Long count = end-start;
		System.out.println("总计时:"+count/1000+"秒");

    }


}