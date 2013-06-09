package net.awired.housecream.server.it.api;

import net.awired.housecream.server.api.domain.user.User;
import net.awired.housecream.server.api.resource.UsersResource;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.HcWsItSession;

public class UserApi {

    private HcWsItSession session;

    public UserApi(HcWsItServer server) {
        //        this.session = new HcWsItSession(server);
    }

    public UserApi(HcWsItSession session) {
        this.session = session;
    }

    public User create(String username, String password) {
        return session.getServer().getResource(UsersResource.class, session).createUser(new User(username, password));
    }
}
