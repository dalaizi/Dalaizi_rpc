package remote;


import annotation.Remote;
import model.Response;
import model.User;
import service.TestService;
import util.ResponseUtil;

import javax.annotation.Resource;


//生成1个服务接口并生成1个实现该接口的类
//实现类如下，为你的实现类添加@Remote注解，该类是你真正调用服务的地方，你可以生成自己想返回给客户端的任何形式的Response
@Remote
public class TestRemoteImpl implements TestRemote {
    @Resource
    private TestService service;

    @Override
    public Response testUser(User user) {
        service.test(user);
        Response response = ResponseUtil.createSuccessResponse(user);

        return response;
    }
}
