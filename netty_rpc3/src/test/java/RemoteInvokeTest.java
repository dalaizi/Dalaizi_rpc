import annotation.RemoteInvoke;
import model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import remote.UserRemote;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RemoteInvokeTest.class)
@ComponentScan("\\")

public class RemoteInvokeTest {
    @RemoteInvoke
    public UserRemote userRemote;


    @Test
    public void testSaveUser(){

//		for(int i=0;i<100;i++){
        User user = new User();
        user.setId(100);
        user.setName("张三");

        userRemote.saveUser(user);
        System.out.println("成功了");
//		}

//		System.out.println("100个请求完毕");


//		Response response = NettyClient.send(clientRequest);
//		System.out.println(response.getResult());
    }
}
