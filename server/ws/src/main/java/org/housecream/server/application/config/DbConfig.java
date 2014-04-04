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
package org.housecream.server.application.config;

import static org.housecream.server.Housecream.CASSANDRA_HOST_KEY;
import static org.housecream.server.Housecream.CASSANDRA_PASSWORD_KEY;
import static org.housecream.server.Housecream.CASSANDRA_PORT_KEY;
import static org.housecream.server.Housecream.CASSANDRA_USERNAME_KEY;
import javax.annotation.PostConstruct;
import org.housecream.server.storage.updater.DbUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

@Configuration
public class DbConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("#{systemProperties['" + CASSANDRA_HOST_KEY + "']}")
    private String host;

    @Value("#{systemProperties['" + CASSANDRA_PORT_KEY + "']}")
    private Integer port;

    @Value("#{systemProperties['" + CASSANDRA_USERNAME_KEY + "']}")
    private String username;

    @Value("#{systemProperties['" + CASSANDRA_PASSWORD_KEY + "']}")
    private String password;

    @Bean(destroyMethod = "close")
    public Cluster cluster() {
        return Cluster.builder().addContactPoint(host).withPort(port).build();
    }

    @PostConstruct
    private void postcontruct() {
        if (port == null) {
            port = 9042;
        }
    }

    @Bean(destroyMethod = "close")
    public Session session() {
        log.info("Creating cassandra session");
        Session session = cluster().connect();
        try {
            session.execute("CREATE KEYSPACE housecream WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");
        } catch (AlreadyExistsException e) {
            // ok if it exits
        }
        session.execute("USE housecream");
        new DbUpdater(session).updateToLatest();
        return session;
    }

}
