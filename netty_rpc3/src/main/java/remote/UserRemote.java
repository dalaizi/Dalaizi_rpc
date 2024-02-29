package remote;

import model.Response;
import model.User;

import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> userlist);
}
