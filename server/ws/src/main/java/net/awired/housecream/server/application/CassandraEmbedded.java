package net.awired.housecream.server.application;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.CassandraDaemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CassandraEmbedded {
    CASSANDRA_EMBEDDED;

    //    private static final String INTERNAL_CASSANDRA_KEYSPACE = "system";
    //    private static final String INTERNAL_CASSANDRA_AUTH_KEYSPACE = "system_auth";
    //    private static final String INTERNAL_CASSANDRA_TRACES_KEYSPACE = "system_traces";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private CassandraDaemon cassandraDaemon;
    private ExecutorService executor;

    public void start(File cassandraHome, CassandraConfig config) {
        if (cassandraDaemon != null) {
            log.warn("Cassandra is already started");
            return;
        }
        log.info("Starting Cassandra...");

        File configFile = new File(cassandraHome, "cassandraConfig.yaml");
        config.writeToFile(configFile);
        System.setProperty("cassandra.config", "file:" + configFile.getAbsolutePath());
        System.setProperty("cassandra-foreground", "true");

        cleanupAndLeaveDirs();
        final CountDownLatch startupLatch = new CountDownLatch(1);
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cassandraDaemon = new CassandraDaemon();
                cassandraDaemon.activate();
                startupLatch.countDown();
            }
        });
        try {
            startupLatch.await(30, SECONDS);
        } catch (InterruptedException e) {
            log.error("Timeout starting Cassandra embedded", e);
            throw new IllegalStateException("Timeout starting Cassandra embedded", e);
        }
    }

    public void stop() {
        log.info("Stopping cassandra embedded server...");
        cassandraDaemon.stop();
        cassandraDaemon = null;
    }

    public boolean isStarted() {
        return cassandraDaemon != null;
    }

    public String getThriftHost() {
        checkStarted();
        return DatabaseDescriptor.getRpcAddress().getHostName();
    }

    public int getThriftPort() {
        checkStarted();
        return DatabaseDescriptor.getRpcPort();
    }

    public String getCqlHost() {
        checkStarted();
        return DatabaseDescriptor.getNativeTransportAddress().getHostName();
    }

    public int getCqlPort() {
        checkStarted();
        return DatabaseDescriptor.getNativeTransportPort();
    }

    //    public void dropKeyspaces(Cluster cluster) {
    //        List<KeyspaceDefinition> keyspaces = cluster.describeKeyspaces();
    //        for (KeyspaceDefinition keyspaceDefinition : keyspaces) {
    //            String keyspaceName = keyspaceDefinition.getName();
    //            if (!INTERNAL_CASSANDRA_KEYSPACE.equals(keyspaceName)
    //                    && !INTERNAL_CASSANDRA_AUTH_KEYSPACE.equals(keyspaceName)
    //                    && !INTERNAL_CASSANDRA_TRACES_KEYSPACE.equals(keyspaceName)) {
    //                cluster.dropKeyspace(keyspaceName);
    //            }
    //        }
    //    }

    //////////////////////////////////////

    private void checkStarted() {
        if (!isStarted()) {
            throw new IllegalStateException("Cassandra is not started");
        }
    }

    private void cleanupAndLeaveDirs() {
        DatabaseDescriptor.createAllDirectories();
        cleanup();
        DatabaseDescriptor.createAllDirectories();
        CommitLog.instance.resetUnsafe();
    }

    private void cleanup() {
        String[] directoryNames = { DatabaseDescriptor.getCommitLogLocation(), };
        for (String dirName : directoryNames) {
            File dir = new File(dirName);
            if (!dir.exists()) {
                throw new RuntimeException("No such directory: " + dir.getAbsolutePath());
            }
            FileUtils.deleteRecursive(dir);
        }

        for (String dirName : DatabaseDescriptor.getAllDataFileLocations()) {
            File dir = new File(dirName);
            if (!dir.exists()) {
                throw new RuntimeException("No such directory: " + dir.getAbsolutePath());
            }
            FileUtils.deleteRecursive(dir);
        }
    }

}
