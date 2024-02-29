package remote;

import annotation.Remote;
import model.Response;
import model.User;
import service.UserService;
import util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote {

    @Resource
    private UserService service;  //UserService处理业务逻辑

    @Override
    public Response saveUser(User user) {
        service.saveUser(user);
        Response response = ResponseUtil.createSuccessResponse(user);
        return response;
    }

    @Override
    public Response saveUsers(List<User> userlist) {
        service.saveUserList(userlist);
        Response response = ResponseUtil.createSuccessResponse(userlist);

        return response;
    }
}
