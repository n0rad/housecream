//package net.awired.housecream.server.storage.dao;
//
//import net.awired.ajsl.core.lang.exception.NotFoundException;
//import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
//import net.awired.housecream.server.api.domain.user.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class UserDao extends GenericDaoImpl<User, String> implements UserDetailsService {
//
//    public UserDao() {
//        super(User.class, String.class);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        try {
//            return find(username);
//        } catch (NotFoundException e) {
//            throw new UsernameNotFoundException("Cannot found username", e);
//        }
//    }
//}
