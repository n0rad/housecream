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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.housecream.server.Housecream;
import org.housecream.server.application.security.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.core.io.file.FileLocker;
import lombok.Getter;

public enum HousecreamHome {
    HOUSECREAM_HOME;

    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String LOCKER_FILE_NAME = "lock";
    private static final String CASSANDRA_DIRECTORY_NAME = "cassandra";

    private FileLocker lock;
    private boolean inited;
    @Getter
    private String globalSalt;

    private HousecreamHome() {
    }

    public void start() {
        if (inited) {
            return;
        }
        logHousecreamInfo();
        lockHomeDirectory();
        loadGlobalSalt();
        if (System.getProperty(CASSANDRA_HOST_KEY) == null) {
            log.info("No '" + CASSANDRA_HOST_KEY + "' system property set. Using Cassandra embedded");
            startCassandraEmbedded();
        }
        updateDbVersion();
        inited = true;
    }

    private void loadGlobalSalt() {
        File saltFile = new File(HOUSECREAM.getHome(), "globalSalt");
        if (!saltFile.exists()) {
            // TODO file should be created with restricted permissions (full home dir in fact)
            try (PrintWriter printWriter = new PrintWriter(saltFile)) {
                printWriter.print(new RandomStringGenerator().base64(42));
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Cannot write global salt file : " + saltFile, e);
            }
        }
        try {
            globalSalt = new String(Files.readAllBytes(saltFile.toPath()));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read global salt file : " + saltFile, e);
        }
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
        File versionFile = new File(HOUSECREAM.getHome(), "/version");
        if (VERSION_UNKNOWN.equals(HOUSECREAM.getVersion())) {
            return;
        }

        try {
            String version = new String(Files.readAllBytes(versionFile.toPath()));
            if (!HOUSECREAM.getVersion().equals(version)) {
                log.warn("Version of DB is " + version + " but currently running " + HOUSECREAM.getVersion()
                        + ". DB will be dropped");
                FileUtils.deleteDirectory(new File(HOUSECREAM.getHome() + "/db"));
            }
        } catch (IOException e) {
            // no file found will not do anything
        } finally {
            try {
                Files.write(versionFile.toPath(), HOUSECREAM.getVersion().getBytes());
            } catch (IOException e) {
                System.err.println("cannot write housecream version to file" + versionFile);
            }
        }

    }

}
