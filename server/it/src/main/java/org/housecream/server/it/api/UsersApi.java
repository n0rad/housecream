package org.housecream.server.it.api;

import org.housecream.server.api.exception.UsernameAlreadyExistException;
import org.housecream.server.api.resource.generic.UsersResource;
import org.housecream.server.it.ItSession;
import fr.norad.jaxrs.oauth2.api.User;
import fr.norad.jaxrs.oauth2.api.UserNotFoundException;

public class UsersApi {

    private ItSession session;

    public UsersApi(ItSession session) {
        this.session = session;
    }

    public void create(User user) throws UsernameAlreadyExistException {
        session.getServer().getResource(UsersResource.class, session).create(user);
    }

    public User get(String username) throws UserNotFoundException {
        return session.getServer().getResource(UsersResource.class, session).user(username).getUser(username);
    }
}
