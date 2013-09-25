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

import java.lang.reflect.Constructor;
import org.housecream.server.storage.updater.DbUpdater;
import org.junit.rules.ExternalResource;
import org.springframework.util.ReflectionUtils;
import com.datastax.driver.core.Session;

public class CassandraDaoRule<DAO> extends ExternalResource {

    private DAO dao;

    static {
        new DbUpdater(CassandraTester.getSession()).updateToLatest();
    }

    public CassandraDaoRule(Class<DAO> daoClass) {
        dao = createDao(daoClass, CassandraTester.getSession());
    }

    ////////////////////////////////////////

    private DAO createDao(Class<DAO> daoClass, Session session) {
        DAO dao;
        try {
            Constructor<DAO> constructor = daoClass.getDeclaredConstructor(Session.class);
            ReflectionUtils.makeAccessible(constructor);
            dao = constructor.newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dao;
    }

    public void truncateColumnFamilies() {
        CassandraTester.truncateColumnFamilies();
    }

    public DAO dao() {
        return dao;
    }

}
