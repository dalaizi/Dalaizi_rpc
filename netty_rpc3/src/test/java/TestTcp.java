import model.ClientRequest;
import model.Response;
import client.TCPClient;
import org.junit.Test;
import model.User;

public class TestTcp {
    @Test
    public void testGetResponse() {
        ClientRequest request = new ClientRequest();
        request.setContent("test tcp connection");

        Response response = TCPClient.send(request);
        System.out.println(response.getContent());
    }

    @Test
    public void testSaveUser() {
        //构建user
        User u = new User();
        u.setId(1);
        u.setName("zhang");

        //构建Request
        ClientRequest request = new ClientRequest();
        //request.setId(...) 原子自增
        request.setContent(u);
        request.setCommand("controller.UserController.saveUser");

        //发送Request，接收Response
        Response response = TCPClient.send(request);
        System.out.println(response.getContent());
    }
}
