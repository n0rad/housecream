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

import static org.housecream.server.application.CassandraEmbedded.CASSANDRA_EMBEDDED;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.housecream.server.application.CassandraConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.google.common.io.Files;

public class CassandraTester {

    public final Logger log = LoggerFactory.getLogger(getClass());

    private static Cluster cluster;

    private static Session session;

    static {
        final File tempDir = Files.createTempDir();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    FileUtils.deleteDirectory(tempDir);
                } catch (IOException e) {
                    // nothing to do
                }
            }
        });
        CassandraConfig config = new CassandraConfig("Test Cluster", tempDir).randomPorts();
        CASSANDRA_EMBEDDED.start(config);

        cluster = Cluster.builder().addContactPoint(CASSANDRA_EMBEDDED.getConfig().getCqlHost())
                .withPort(CASSANDRA_EMBEDDED.getConfig().getCqlPort()).build();

        session = cluster.connect();
        try {
            session.execute("CREATE KEYSPACE test WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");
        } catch (AlreadyExistsException e) {
            // ok if it exists
        }
        session.execute("USE test");

    }

    public static void removeColumnFamilies() {
        ResultSet execute = session.execute("select * from system.schema_columnfamilies;");
        for (Row row : execute) {
            if (row.getString("keyspace_name").equals("test")) {
                session.execute("DROP TABLE " + row.getString("columnfamily_name"));
            }
        }
    }

    public static void truncateColumnFamilies() {
        ResultSet execute = session.execute("select * from system.schema_columnfamilies;");
        for (Row row : execute) {
            if (row.getString("keyspace_name").equals("test")) {
                session.execute("TRUNCATE " + row.getString("columnfamily_name"));
            }
        }
    }

    public static Session getSession() {
        return session;
    }
}
