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
package org.housecream.server.storage.security;

import static org.housecream.server.application.config.EncodingConfig.objectMapper;
import java.io.IOException;
import java.util.Date;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.exception.UsernameAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import fr.norad.jaxrs.oauth2.api.Group;
import fr.norad.jaxrs.oauth2.api.User;
import fr.norad.jaxrs.oauth2.api.UserNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.UserRepository;

@Repository
public class UserDao implements UserRepository {

    @Autowired
    Config props;

    private final Session session;
    private final PreparedStatement selectQuery;
    private final PreparedStatement insertQuery;

    @Autowired
    public UserDao(Session session) {
        this.session = session;
        insertQuery = session.prepare("INSERT INTO " +
                "users(username, hashedPassword, salt, groups)" +
                "VALUES (?,?,?,?) IF NOT EXISTS");
        selectQuery = session.prepare("SELECT * FROM users WHERE username = ?");
    }

    public void create(User user) throws UsernameAlreadyExistException {
        ResultSet execute = session.execute(insertQuery.bind(user.getUsername(),
                user.getHashedPassword(),
                user.getSalt(),
                user.getGroups()));
        if (!execute.one().getBool(0)) {
            throw new UsernameAlreadyExistException("Username '" + user.getUsername() + "' is already used");
        }
    }

    @Override
    public User findUser(String username) throws UserNotFoundException {
        ResultSet execute = session.execute(selectQuery.bind(username));
        if (execute.isExhausted()) {
            throw new UserNotFoundException("Cannot found user with username : " + username);
        }
        return map(execute.one());
    }

    @Override
    public void increaseFailedLoginAttempts(User user) {
        PreparedStatement prepare = session.prepare("UPDATE users USING TTL " + props.getSecurityFailedLoginLifetimeSeconds() +
                " SET failedLogin = failedLogin + {" + new Date().getTime() + "} WHERE username = ?");
        session.execute(prepare.bind(user.getUsername()));
    }

    private User map(Row row) {
        User user = new User();
        user.setUsername(row.getString("username"));
        user.setHashedPassword(row.getString("hashedPassword"));
        user.setSalt(row.getString("salt"));
        user.setFailedLoginAttempt(row.getSet("failedlogin", Date.class).size());
        try {
            for (String group : row.getSet("groups", String.class)) {
                user.getGroups().add(objectMapper().readValue(group, Group.class));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read group from DB : " + row.getString("groups"), e);
        }
        user.setFailedLoginAttempt(row.getSet("failedLogin", Date.class).size());
        return user;
    }

}
