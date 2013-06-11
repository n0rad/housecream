/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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
