package service;

import model.User;
import org.springframework.stereotype.Service;

//在服务端的Service下添加你自己的Service,并加上@Service注解
@Service
public class TestService {
    public void test(User user){
        System.out.println("调用了TestService.test");
    }
}
