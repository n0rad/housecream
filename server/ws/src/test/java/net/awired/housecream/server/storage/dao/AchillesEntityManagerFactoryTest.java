package net.awired.housecream.server.storage.dao;

import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.entity.manager.CQLEntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchillesEntityManagerFactoryTest<T> extends EmbeddedCassandraServerTest {

    private static final String ENTITY_PACKAGES = "com.libon.auth.domain";

    public static final Logger log = LoggerFactory.getLogger(AchillesEntityManagerFactoryTest.class);

    protected T dao;

    protected static CQLEntityManager em;

    static {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("achilles.entity.packages", ENTITY_PACKAGES);
        configMap.put("achilles.cassandra.cluster", cluster);
        configMap.put("achilles.cassandra.keyspace", keyspace);
        configMap.put("achilles.ddl.force.column.family.creation", true);

        CQLEntityManagerFactory emFactory = new CQLEntityManagerFactory(configMap);
        em = emFactory.createEntityManager();
    }

    protected AchillesEntityManagerFactoryTest(Class<T> daoClass) {
        this.dao = createDao(daoClass);
    }

    public static ThriftEntityManager getEm() {
        return em;
    }

}
