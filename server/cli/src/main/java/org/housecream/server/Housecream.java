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
package org.housecream.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import fr.norad.core.io.JarManifestUtils;
import fr.norad.operating.system.specific.ApplicationHomeFactory;

public enum Housecream {
    HOUSECREAM;

    public static final String HOUSECREAM_NAME = "Housecream";
    public static final String HOUSECREAM_HOME_KEY = "HOUSECREAM_HOME";

    private static final String LOG_CONF_FILE_PATH_KEY = "logback.configurationFile";
    private static final String LOG_CONF_FILE_NAME = "logback.xml";

    public static final String VERSION_MANIFEST_KEY = "HousecreamVersion";
    public static final String VERSION_UNKNOWN = "Unknow Version";

    public static final String CASSANDRA_HOST_KEY = "housecream.db.host";
    public static final String CASSANDRA_PORT_KEY = "housecream.db.port";
    public static final String CASSANDRA_USERNAME_KEY = "housecream.db.username";
    //TODO read password from prompt instead of command line param.
    //TODO write password to file into home directory 
    //TODO read password from file and delete it
    public static final String CASSANDRA_PASSWORD_KEY = "housecream.db.password";

    private File home;
    private String version;
    private File logbackConf;
    private File housecreamConf;
    private File pluginDirectory;
    private boolean inited = false;

    private Housecream() {
        home = findHomeDir();
        version = JarManifestUtils.getManifestAttribute(VERSION_MANIFEST_KEY);
        if (version == null) {
            version = VERSION_UNKNOWN;
        }
        logbackConf = new File(home, LOG_CONF_FILE_NAME);
        pluginDirectory = new File(home, "plugins");
    }

    public synchronized void init() {
        if (inited) {
            return;
        }

        System.setProperty(HOUSECREAM_HOME_KEY, home.getAbsolutePath());
        if (System.getProperty(LOG_CONF_FILE_PATH_KEY) == null) {
            System.setProperty(LOG_CONF_FILE_PATH_KEY, logbackConf.getAbsolutePath());
        }

        if (!home.exists()) {
            home.mkdirs();
            pluginDirectory.mkdirs();
        }

        if (!logbackConf.exists()) {
            copyLogbackFile();
        }
        inited = true;
    }

    public void discoveredVersion(String versionFromWar) {
        if (versionFromWar != null && (version == null || VERSION_UNKNOWN.equals(version))) {
            version = versionFromWar;
        }
    }

    public File getHome() {
        return home;
    }

    public String getVersion() {
        return version;
    }

    public File getHousecreamConf() {
        return housecreamConf;
    }

    public File getPluginDirectory() {
        return pluginDirectory;
    }

    public File getLogbackConf() {
        return logbackConf;
    }

    public File findDefaultOsHomeDirectory() {
        return ApplicationHomeFactory.getApplicationHome().getFolder(HOUSECREAM_NAME);
    }

    //////////////////////////////////

    private File findHomeDir() {
        try {
            InitialContext iniCtxt = new InitialContext();
            Context env = (Context) iniCtxt.lookup("java:comp/env");
            String value = (String) env.lookup(HOUSECREAM_HOME_KEY);
            if (value != null && value.trim().length() > 0) {
                return new File(value.trim());
            }

            value = (String) iniCtxt.lookup(HOUSECREAM_HOME_KEY);
            if (value != null && value.trim().length() > 0) {
                return new File(value.trim());
            }
        } catch (NamingException e) {
        }

        String sysProp = System.getProperty(HOUSECREAM_HOME_KEY);
        if (sysProp != null) {
            return new File(sysProp.trim());
        }

        try {
            String env = System.getenv(HOUSECREAM_HOME_KEY);
            if (env != null) {
                return new File(env.trim());
            }
        } catch (Throwable e) {
        }
        return findDefaultOsHomeDirectory();
    }

    private void copyLogbackFile() {
        InputStream from = null;
        OutputStream to = null;
        try {
            from = getClass().getResourceAsStream("/housecream-logback.xml");
            to = new FileOutputStream(new File(home, LOG_CONF_FILE_NAME));
            byte[] buf = new byte[0x800];
            int bytesRead = from.read(buf);
            while (bytesRead > -1) {
                to.write(buf, 0, bytesRead);
                bytesRead = from.read(buf);
            }
        } catch (IOException e) {
            System.err.println("Cannot write logback configuration file to home folder : " + home);
            e.printStackTrace();
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
