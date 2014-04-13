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

import static org.housecream.server.Housecream.CASSANDRA_HOST_KEY;
import static org.housecream.server.Housecream.HOUSECREAM;
import static org.housecream.server.Housecream.VERSION_UNKNOWN;
import static org.housecream.server.application.CassandraEmbedded.CASSANDRA_EMBEDDED;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.housecream.server.Housecream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fr.norad.core.io.file.FileLocker;

public enum HousecreamHome {
    HOUSECREAM_HOME;

    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String LOCKER_FILE_NAME = "lock";
    private static final String CASSANDRA_DIRECTORY_NAME = "cassandra";

    private FileLocker lock;
    private boolean inited;

    private HousecreamHome() {
    }

    public void start() {
        if (inited) {
            return;
        }
        logHousecreamInfo();
        lockHomeDirectory();
        if (System.getProperty(CASSANDRA_HOST_KEY) == null) {
            log.info("No '" + CASSANDRA_HOST_KEY + "' system property set. Using Cassandra embedded");
            startCassandraEmbedded();
        }
        updateDbVersion();
        inited = true;
    }

    private void startCassandraEmbedded() {
        CASSANDRA_EMBEDDED.start(new CassandraConfig("Housecream Cluster", new File(HOUSECREAM.getHome(),
                CASSANDRA_DIRECTORY_NAME)).randomPorts());

        System.setProperty(Housecream.CASSANDRA_HOST_KEY, CASSANDRA_EMBEDDED.getConfig().getCqlHost());
        System.setProperty(Housecream.CASSANDRA_PORT_KEY, String.valueOf(CASSANDRA_EMBEDDED.getConfig().getCqlPort()));
    }

    public void stop() {
        lock.close();
        inited = false;
    }

    ///////////////////////////////////////////////////////////////////////

    private void logHousecreamInfo() {
        log.info("############# Housecream #############");
        log.info("## Version           : " + HOUSECREAM.getVersion());
        log.info("## Home              : " + HOUSECREAM.getHome());
        log.info("## Log conf          : " + HOUSECREAM.getLogbackConf());
//        log.info("## Plugins directory : " + HOUSECREAM.getPluginDirectory());
        log.info("######################################");
    }

    private void lockHomeDirectory() {
        lock = new FileLocker(new File(HOUSECREAM.getHome(), LOCKER_FILE_NAME));
        if (lock.isLocked()) {
            throw new SecurityException("Housecream Home folder : " + HOUSECREAM.getHome() + " is locked");
        }
    }

    private void updateDbVersion() {
        File versionFile = new File(HOUSECREAM.getHome() + "/version");
        if (VERSION_UNKNOWN.equals(HOUSECREAM.getVersion())) {
            return;
        }

        try {
            List<String> lines = Files.readLines(versionFile, Charsets.UTF_8);
            if (lines.size() > 0 && !lines.get(0).equals(HOUSECREAM.getVersion())) {
                log.warn("Version of DB is " + lines.get(0) + " but currently running " + HOUSECREAM.getVersion()
                        + ". DB will be dropped");
                FileUtils.deleteDirectory(new File(HOUSECREAM.getHome() + "/db"));
            }
        } catch (IOException e) {
            // no file found will not do anything
        } finally {
            try {
                Files.write(HOUSECREAM.getVersion().getBytes(), versionFile);
            } catch (IOException e) {
                System.err.println("cannot write housecream version to file" + versionFile);
            }
        }

    }

}
