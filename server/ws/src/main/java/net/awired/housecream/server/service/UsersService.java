package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.housecream.server.api.domain.user.Role;
import net.awired.housecream.server.api.domain.user.User;
import net.awired.housecream.server.api.resource.UsersResource;
import net.awired.housecream.server.storage.dao.UserDao;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UsersService implements UsersResource {

    @Inject
    private UserDao userDao;

    @Inject
    private PasswordEncoder encoder;

    @Inject
    private SaltSource saltSource;

    @Override
    public User createUser(User user) {
        user.getRoles().add(new Role("user"));
        userDao.persist(user); //to have an ID
        user.setHashedPassword(encoder.encodePassword(user.getClearPassword(), saltSource.getSalt(user)));
        user.setClearPassword(null);
        userDao.save(user);
        return user;
    }

}
