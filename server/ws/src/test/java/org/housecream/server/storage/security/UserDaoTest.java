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


import org.fest.assertions.api.Assertions;
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.storage.CassandraDaoRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.oauth2.api.User;

public class UserDaoTest {

    @Rule
    public CassandraDaoRule<UserDao> db = new CassandraDaoRule<>(UserDao.class);

    @Before
    public void before() {
        db.dao().props = new HcProperties().setValue("securityFailedLoginLifetimeSeconds", "1");
    }

    @Test
    public void should_increase_failed_login_attempt() throws Exception {
        User user = new User();
        user.setUsername("toto");
        db.dao().create(user);
        db.dao().increaseFailedLoginAttempts(user);
        db.dao().increaseFailedLoginAttempts(user);

        Assertions.assertThat(db.dao().findUser("toto").getFailedLoginAttempt()).isEqualTo(2);
    }

    @Test
    public void should_remove_failed_login_attempt_at_ttl() throws Exception {
        User user = new User();
        user.setUsername("toto");
        user.setHashedPassword("HASSHED");
        db.dao().create(user);
        db.dao().increaseFailedLoginAttempts(user);
        Assertions.assertThat(db.dao().findUser("toto").getFailedLoginAttempt()).isEqualTo(1);
        Thread.sleep(1000);
        Assertions.assertThat(db.dao().findUser("toto").getFailedLoginAttempt()).isEqualTo(0);
    }
}
