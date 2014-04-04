/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.service;

import java.util.UUID;
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.api.exception.UsernameAlreadyExistException;
import org.housecream.server.storage.security.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.norad.jaxrs.oauth2.api.User;
import fr.norad.jaxrs.oauth2.core.service.PasswordHasher;
import fr.norad.jaxrs.oauth2.core.service.UserSpecService;

@Service
public class UserService extends UserSpecService {

    @Autowired
    HcProperties props;

    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordHasher passwordHasher;

    @Override
    protected int getMaxFailedLoginAttempt() {
        return props.getSecurityMaxFailedLoginAttempt();
    }

    public void createUser(User user) throws UsernameAlreadyExistException {
        user.setSalt(UUID.randomUUID().toString());
        user.setHashedPassword(passwordHasher.hash(user.getPassword(), user.getSalt()));
        userDao.create(user);
        user.setPassword(null);
    }


}
