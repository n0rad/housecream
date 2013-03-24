package net.awired.housecream.server.storage.dao;

import javax.persistence.TypedQuery;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericDaoImpl<User, Long> implements UserDetailsService {

    public UserDao() {
        super(User.class, Long.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return findByUsername(username);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("Cannot found username", e);
        }
    }

    public User findByUsername(String username) throws NotFoundException {
        TypedQuery<User> query = entityManager.createNamedQuery(User.QUERY_BY_USERNAME, User.class);
        query.setParameter(User.QUERY_PARAM_USERNAME, username);
        return findSingleResult(query);
    }

}
