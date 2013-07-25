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
package org.housecream.server.application;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.cassandra.service.CassandraDaemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CassandraEmbedded {
    CASSANDRA_EMBEDDED;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ExecutorService executor;
    private CassandraConfig config;

    public void start(CassandraConfig config) {
        this.config = config;
        if (isAlreadyRunning()) {
            log.info("Cassandra is already running, not starting new one");
            return;
        }

        log.info("Starting Cassandra...");
        config.write();
        System.setProperty("cassandra.config", "file:" + config.getConfigFile().getAbsolutePath());
        System.setProperty("cassandra-foreground", "true");

        final CountDownLatch startupLatch = new CountDownLatch(1);
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CassandraDaemon cassandraDaemon = new CassandraDaemon();
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

    private boolean isAlreadyRunning() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            MBeanInfo mBeanInfo = mbs.getMBeanInfo(new ObjectName("org.apache.cassandra.db:type=StorageService"));
            if (mBeanInfo != null) {
                return true;
            }
            return false;
        } catch (InstanceNotFoundException e) {
            return false;
        } catch (IntrospectionException | MalformedObjectNameException | ReflectionException e) {
            throw new IllegalStateException("Cannot check if cassandra is already running", e);
        }
    }

    public CassandraConfig getConfig() {
        config.load();
        return config;
    }
}
