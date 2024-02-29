package controller;

import model.Response;
import org.springframework.stereotype.Controller;
import model.User;
import service.UserService;
import util.ResponseUtil;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController { //总控制室，为UserService匹配对应Model（User）

    @Resource
    private UserService userService;  //UserService处理业务逻辑

    public Response saveUser(User user) {
        userService.saveUser(user);
        Response response = ResponseUtil.createSuccessResponse(user);
        return response;
    }

    public Response saveUsers(List<User> userlist){
        userService.saveUserList(userlist);
        Response response = ResponseUtil.createSuccessResponse(userlist);
        return response;
    }
}
