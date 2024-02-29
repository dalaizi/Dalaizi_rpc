package remote;

import model.Response;
import model.User;

//生成1个服务接口并生成1个实现该接口的类
//接口如下
public interface TestRemote {
    public Response testUser(User user);
}
