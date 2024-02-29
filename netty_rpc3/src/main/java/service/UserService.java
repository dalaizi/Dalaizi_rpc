package service;

import org.springframework.stereotype.Service;
import model.User;

import java.util.List;

@Service
public class UserService { //UserService 输入要处理的数据Model（User），进行业务逻辑处理
    public void saveUser(User user) {
        //访问MySQL
        System.out.println("saveUser调用");
    }
    public void saveUserList(List<User> userList) {

    }
}
