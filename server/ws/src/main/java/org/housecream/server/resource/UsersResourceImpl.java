package org.housecream.server.resource;

import org.housecream.server.api.exception.UsernameAlreadyExistException;
import org.housecream.server.api.resource.UsersResource;
import org.housecream.server.application.JaxRsResource;
import org.housecream.server.service.UserService;
import org.housecream.server.storage.security.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import fr.norad.jaxrs.oauth2.api.User;
import fr.norad.jaxrs.oauth2.api.UserNotFoundException;

@JaxRsResource
public class UsersResourceImpl implements UsersResource {

    @Autowired
    private UserService userService;

    @Autowired
    private UserResource userResource;

    @Override
    public void create(User user) throws UsernameAlreadyExistException {
        userService.createUser(user);
    }

    @Override
    public UserResource user(String username) {
        return userResource;
    }

    @JaxRsResource
    public static class UserResourceImpl implements UserResource {

        @Autowired
        private UserDao user;

        @Override
        public User getUser(String username) throws UserNotFoundException {
            return user.findUser(username);
        }
    }

}
