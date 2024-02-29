package user;

import consumer.model.Response;

import java.util.List;

public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> userlist);
}
