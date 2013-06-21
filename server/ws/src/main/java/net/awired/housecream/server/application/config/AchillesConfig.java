package net.awired.housecream.server.application.config;

import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.manager.ThriftEntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PreDestroy;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.collect.ImmutableMap;

@Configuration
public class AchillesConfig {

    @Value("${cassandra.hosts:localhost}")
    private String cassandraHosts;

    @Value("${cassandra.keyspace:oauth}")
    private String keyspaceName;

    @Value("${cassandra.user:oauth}")
    private String user;

    @Value("${cassandra.cluster.clean:true}")
    private boolean cleanClusterShutdown;

    @Value("${cassandra.password:oauth}")
    private String password;

    @Autowired
    private Cluster cluster;

    @Bean
    public ThriftEntityManager achillesEntityManager() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("achilles.entity.packages", "net.awired");
        configMap.put("achilles.cassandra.cluster", cluster());
        configMap.put("achilles.cassandra.keyspace", keyspace());
        configMap.put("achilles.ddl.force.column.family.creation", true);
        return new ThriftEntityManagerFactory(configMap).createEntityManager();
    }

    @Bean
    public Cluster cluster() {
        Map<String, String> credentials = ImmutableMap.of("username", user, "password", password);
        return HFactory.getOrCreateCluster("housecream", new CassandraHostConfigurator(cassandraHosts), credentials);
    }

    private Keyspace keyspace() {
        return HFactory.createKeyspace(keyspaceName, cluster());
    }

    @PreDestroy
    public void destroy() {
        if (cleanClusterShutdown) {
            cluster.getConnectionManager().shutdown();
        }
    }

}
