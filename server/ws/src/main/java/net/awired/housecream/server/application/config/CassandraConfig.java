package net.awired.housecream.server.application.config;

import static info.archinnov.achilles.configuration.CQLConfigurationParameters.CONNECTION_CONTACT_POINTS_PARAM;
import static info.archinnov.achilles.configuration.CQLConfigurationParameters.CONNECTION_PORT_PARAM;
import static info.archinnov.achilles.configuration.CQLConfigurationParameters.KEYSPACE_NAME_PARAM;
import static info.archinnov.achilles.configuration.ConfigurationParameters.ENSURE_CONSISTENCY_ON_JOIN_PARAM;
import static info.archinnov.achilles.configuration.ConfigurationParameters.ENTITY_PACKAGES_PARAM;
import static info.archinnov.achilles.configuration.ConfigurationParameters.FORCE_CF_CREATION_PARAM;
import static net.awired.housecream.server.Housecream.CASSANDRA_HOST_KEY;
import static net.awired.housecream.server.Housecream.CASSANDRA_PASSWORD_KEY;
import static net.awired.housecream.server.Housecream.CASSANDRA_PORT_KEY;
import static net.awired.housecream.server.Housecream.CASSANDRA_USERNAME_KEY;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.entity.manager.CQLEntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import net.awired.housecream.server.storage.updater.DbUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

@Configuration
public class CassandraConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("#{systemProperties['" + CASSANDRA_HOST_KEY + "']}")
    private String host;

    @Value("#{systemProperties['" + CASSANDRA_PORT_KEY + "']}")
    private Integer port;

    @Value("#{systemProperties['" + CASSANDRA_USERNAME_KEY + "']}")
    private String username;

    @Value("#{systemProperties['" + CASSANDRA_PASSWORD_KEY + "']}")
    private String password;

    @Bean(destroyMethod = "shutdown")
    public Cluster cluster() {
        return Cluster.builder().addContactPoint(host).withPort(port).build();
    }

    @Bean
    public CQLEntityManager entityManager(DbUpdater updater) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ENTITY_PACKAGES_PARAM, "net.awired.housecream");
        configMap.put(CONNECTION_CONTACT_POINTS_PARAM, host);
        configMap.put(CONNECTION_PORT_PARAM, port + "");
        configMap.put(KEYSPACE_NAME_PARAM, "housecream");
        configMap.put(FORCE_CF_CREATION_PARAM, true);
        configMap.put(ENSURE_CONSISTENCY_ON_JOIN_PARAM, true);

        CQLEntityManagerFactory emf = new CQLEntityManagerFactory(configMap);
        return emf.createEntityManager();
    }

    @Bean(destroyMethod = "shutdown")
    public Session session() {
        log.info("Creating cassandra session");
        Session session = cluster().connect();
        try {
            session.execute("CREATE KEYSPACE housecream WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");
        } catch (AlreadyExistsException e) {
            // ok if it exits
        }
        session.execute("USE housecream;");
        return session;
    }

}
