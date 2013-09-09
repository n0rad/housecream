package org.housecream.server.storage.dao;

import java.lang.reflect.Constructor;
import org.housecream.server.storage.updater.DbUpdater;
import org.springframework.util.ReflectionUtils;
import com.datastax.driver.core.Session;

public class CassandraDaoRule<DAO> extends CassandraRule {

    private DAO dao;

    private DAO createDao(Class<DAO> daoClass, Session session) {
        DAO dao;
        try {
            Constructor<DAO> constructor = daoClass.getDeclaredConstructor(Session.class);
            ReflectionUtils.makeAccessible(constructor);
            dao = constructor.newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //        ReflectionTestUtils.setField(dao, sessionPropertyName, session);
        return dao;
    }

    public CassandraDaoRule(Class<DAO> daoClass, String sessionPropertyName) {
        super();
        before();
        dao = createDao(daoClass, getSession());
    }

    @Override
    protected void before() {
        super.before();
        prepareColumnFamilies();
    }

    private void prepareColumnFamilies() {
        new DbUpdater(getSession()).updateToLatest();
    }

    public DAO dao() {
        return dao;
    }

}
