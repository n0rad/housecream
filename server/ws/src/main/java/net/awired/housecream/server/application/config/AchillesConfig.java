/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.server.application.config;

import static net.awired.housecream.server.application.CassandraEmbedded.CASSANDRA_EMBEDDED;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@Configuration
public class AchillesConfig {

    //    @Value("${cassandra.hosts}")
    //    private String cassandraHosts;
    //
    //    @Value("${cassandra.keyspace:housecream}")
    //    private String keyspaceName;
    //
    //    @Value("${cassandra.user:housecream}")
    //    private String user;
    //
    //    @Value("${cassandra.cluster.clean:true}")
    //    private boolean cleanClusterShutdown;
    //
    //    @Value("${cassandra.password:}")
    //    private String password;

    //    @Autowired
    //    private Cluster cluster;

    @Bean
    public CQLEntityManager achillesEntityManager() {

        //        createKeyspace();
        //        createTables();
        //
        //        Map<String, Object> configMap = new HashMap<>();
        //        //        configMap.put("achilles.cassandra.cluster", cluster());
        //        configMap.put(FORCE_CF_CREATION_PARAM, true);
        //        configMap.put(KEYSPACE_NAME_PARAM, "housecream");
        //        configMap.put("achilles.entity.packages", "net.awired");
        //        configMap.put("achilles.cassandra.connection.contactPoints", CASSANDRA_EMBEDDED.getCqlHost());
        //        configMap.put("achilles.cassandra.connection.port", String.valueOf(CASSANDRA_EMBEDDED.getCqlPort()));
        //
        //        return new CQLEntityManagerFactory(configMap).createEntityManager();
        return null;
    }

    private void createTables() {
        Cluster cluster = Cluster.builder().addContactPoint(CASSANDRA_EMBEDDED.getCqlHost())
                .withPort(CASSANDRA_EMBEDDED.getCqlPort()).build();

        Session session = cluster.connect("housecream");
        session.execute("create table achilles_counter_table(fqcn text,pk text,key text,counter_value counter, primary key ((fqcn,pk),key))");
    }

    private void createKeyspace() {
        Cluster cluster = Cluster.builder().addContactPoint(CASSANDRA_EMBEDDED.getCqlHost())
                .withPort(CASSANDRA_EMBEDDED.getCqlPort()).build();

        Session session = cluster.connect();
        session.execute("CREATE KEYSPACE housecream WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");
        session.shutdown();

    }
    //    @Bean
    //    public Cluster cluster() {
    //        String hostname = CASSANDRA_EMBEDDED.getCqlHost();
    //        int port = CASSANDRA_EMBEDDED.getCqlPort();
    //        return Cluster.builder().addContactPoint(hostname).withPort(port).build();
    //    }

    //    @PreDestroy
    //    public void destroy() {
    //        cluster.shutdown();
    //    }
}
