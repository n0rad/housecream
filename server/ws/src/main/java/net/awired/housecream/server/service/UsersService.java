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
package net.awired.housecream.server.service;

import net.awired.housecream.server.api.domain.user.User;
import net.awired.housecream.server.api.resource.UsersResource;
import org.springframework.stereotype.Component;

@Component
public class UsersService implements UsersResource {

    //    @Inject
    //    private UserDao userDao;

    //    @Inject
    //    private PasswordEncoder encoder;
    //
    //    @Inject
    //    private SaltSource saltSource;

    @Override
    public User createUser(User user) {
        //        userDao.persist(user); //to have an ID
        //        user.setHashedPassword(encoder.encodePassword(user.getClearPassword(), saltSource.getSalt(user)));
        //        userDao.save(user);
        return user;
    }

}
