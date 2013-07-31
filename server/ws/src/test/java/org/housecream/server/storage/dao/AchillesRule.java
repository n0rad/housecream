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

import static info.archinnov.achilles.configuration.CQLConfigurationParameters.CONNECTION_CONTACT_POINTS_PARAM;
import static info.archinnov.achilles.configuration.CQLConfigurationParameters.CONNECTION_PORT_PARAM;
import static info.archinnov.achilles.configuration.CQLConfigurationParameters.KEYSPACE_NAME_PARAM;
import static info.archinnov.achilles.configuration.ConfigurationParameters.ENSURE_CONSISTENCY_ON_JOIN_PARAM;
import static info.archinnov.achilles.configuration.ConfigurationParameters.ENTITY_PACKAGES_PARAM;
import static info.archinnov.achilles.configuration.ConfigurationParameters.FORCE_CF_CREATION_PARAM;
import static org.housecream.server.application.CassandraEmbedded.CASSANDRA_EMBEDDED;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.entity.manager.CQLEntityManagerFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.housecream.server.application.CassandraConfig;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.google.common.io.Files;

public class AchillesRule<DAO> extends ExternalResource {

    public final Logger log = LoggerFactory.getLogger(getClass());

    private final DAO dao;

    private CQLEntityManager em;

    private Cluster cluster;

    private Session session;

    static {
        File createTempDir = Files.createTempDir();
        createTempDir.deleteOnExit();
        CassandraConfig config = new CassandraConfig("Test Cluster", createTempDir).randomPorts();
        CASSANDRA_EMBEDDED.start(config);
    }

    public AchillesRule(String packageScan, Class<DAO> daoClass) {
        cluster = Cluster.builder().addContactPoint(CASSANDRA_EMBEDDED.getConfig().getCqlHost())
                .withPort(CASSANDRA_EMBEDDED.getConfig().getCqlPort()).build();

        session = cluster.connect();

        try {
            session.execute("CREATE KEYSPACE test WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");
        } catch (AlreadyExistsException e) {
            // ok if it exists
        }
        session.execute("USE test");

        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ENTITY_PACKAGES_PARAM, packageScan);
        configMap.put(CONNECTION_CONTACT_POINTS_PARAM, CASSANDRA_EMBEDDED.getConfig().getCqlHost());
        configMap.put(CONNECTION_PORT_PARAM, CASSANDRA_EMBEDDED.getConfig().getCqlPort() + "");
        configMap.put(KEYSPACE_NAME_PARAM, "test");
        configMap.put(FORCE_CF_CREATION_PARAM, true);
        configMap.put(ENSURE_CONSISTENCY_ON_JOIN_PARAM, true);

        CQLEntityManagerFactory emf = new CQLEntityManagerFactory(configMap);
        em = emf.createEntityManager();
        this.dao = createDao(daoClass, em);
    }

    public DAO dao() {
        return dao;
    }

    private DAO createDao(Class<DAO> daoClass, Object em) {
        DAO dao;
        try {
            Constructor<DAO> constructor = daoClass.getDeclaredConstructor();
            ReflectionUtils.makeAccessible(constructor);
            dao = constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ReflectionTestUtils.setField(dao, "em", em);
        return dao;
    }

    @Override
    protected void before() throws Throwable {
        ResultSet execute = session.execute("select * from system.schema_columnfamilies;");
        for (Row row : execute) {
            if (row.getString("keyspace_name").equals("test")) {
                session.execute("TRUNCATE " + row.getString("columnfamily_name"));
            }
        }
    }
}