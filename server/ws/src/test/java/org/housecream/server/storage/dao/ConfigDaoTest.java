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
package org.housecream.server.storage.dao;


import org.fest.assertions.api.Assertions;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.storage.CassandraDaoRule;
import org.junit.Rule;
import org.junit.Test;

public class ConfigDaoTest {
    @Rule
    public CassandraDaoRule<ConfigDao> db = new CassandraDaoRule<>(ConfigDao.class);

    @Test
    public void should_write_config() throws Exception {
        Config props = new Config();
//        db.dao().saveConfig(props);
//
        Config otherProps = new Config();
        otherProps.setValue("securityDefaultRefreshTokenLifetimeSeconds", "424242433");
        db.dao().loadConfig(otherProps);

        Assertions.assertThat(otherProps).isEqualTo(props);
    }
}
