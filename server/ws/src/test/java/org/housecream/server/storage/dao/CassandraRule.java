package org.housecream.server.storage.dao;

import static org.housecream.server.application.CassandraEmbedded.CASSANDRA_EMBEDDED;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.housecream.server.application.CassandraConfig;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.google.common.io.Files;

public class CassandraRule extends ExternalResource {

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

    @Override
    protected void before() {
        removeColumnFamilies();
    }

    private void removeColumnFamilies() {
        ResultSet execute = session.execute("select * from system.schema_columnfamilies;");
        for (Row row : execute) {
            if (row.getString("keyspace_name").equals("test")) {
                session.execute("DROP TABLE " + row.getString("columnfamily_name"));
            }
        }
    }

    public Session getSession() {
        return session;
    }
}
