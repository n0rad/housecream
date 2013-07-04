package net.awired.housecream.server.application.config;

import static net.awired.housecream.server.application.CassandraEmbedded.CASSANDRA_EMBEDDED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

@Configuration
public class CassandraConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean(destroyMethod = "shutdown")
    public Cluster cluster() {
        String hostname = CASSANDRA_EMBEDDED.getConfig().getCqlHost();
        int port = CASSANDRA_EMBEDDED.getConfig().getCqlPort();
        return Cluster.builder().addContactPoint(hostname).withPort(port).build();
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
