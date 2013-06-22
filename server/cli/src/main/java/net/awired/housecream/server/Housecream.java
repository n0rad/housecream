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
package net.awired.housecream.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.awired.core.io.JarManifestUtils;
import net.awired.housecream.server.cli.ArgumentManager;
import net.awired.operating.system.specific.ApplicationHomeFactory;
import net.awired.typed.command.line.parser.argument.args.CliOneParamArgument;

public enum Housecream {
    HOUSECREAM;

    public static final String HOUSECREAM_NAME = "Housecream";
    private static final String HOUSECREAM_CONF = "housecream.conf";
    public static final String HOUSECREAM_HOME_KEY = "HOUSECREAM_HOME";

    private static final String LOG_CONF_FILE_PATH_KEY = "logback.configurationFile";
    private static final String LOG_CONF_FILE_NAME = "logback.xml";

    public static final String VERSION_MANIFEST_KEY = "HousecreamVersion";
    public static final String VERSION_UNKNOWN = "Unknow Version";

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
        housecreamConf = new File(home, HOUSECREAM_CONF);
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

    public void updateConfig(ArgumentManager manager) {
        Properties config = readConfig();
        updateConfig(config, manager);
        writeConfig(config);
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

    private void updateConfig(Properties config, ArgumentManager manager) {
        CliOneParamArgument<InetAddress> cassandraHost = manager.getCassandraHost();
        CliOneParamArgument<Integer> cassandraPort = manager.getCassandraPort();
        CliOneParamArgument<String> cassandraLogin = manager.getCassandraLogin();
        CliOneParamArgument<String> cassandraPassword = manager.getCassandraPassword();
        CliOneParamArgument<InetAddress> housecreamHost = manager.getHousecreamHost();
        CliOneParamArgument<String> housecreamContextPath = manager.getHousecreamContextPath();
        CliOneParamArgument<Integer> housecreamPort = manager.getHousecreamPort();

        if (cassandraHost.isSet()) {
            config.setProperty(cassandraHost.getName(), cassandraHost.getParamOneValue().toString());
        }
        if (cassandraPort.isSet()) {
            config.setProperty(cassandraPort.getName(), cassandraPort.getParamOneValue().toString());
        }
        if (cassandraLogin.isSet()) {
            config.setProperty(cassandraLogin.getName(), cassandraLogin.getParamOneValue());
        }
        if (cassandraPassword.isSet()) {
            config.setProperty(cassandraPassword.getName(), cassandraPassword.getParamOneValue());
        }

        if (housecreamHost.isSet()) {
            config.setProperty(housecreamHost.getName(), housecreamHost.getParamOneValue().toString());
        }
        if (housecreamContextPath.isSet()) {
            config.setProperty(housecreamContextPath.getName(), housecreamContextPath.getParamOneValue());
        }
        if (housecreamPort.isSet()) {
            config.setProperty(housecreamPort.getName(), housecreamPort.getParamOneValue().toString());
        }
    }

    private Properties readConfig() {
        Properties config = new Properties();
        try (FileInputStream inStream = new FileInputStream(getHousecreamConf())) {
            config.load(inStream);
            return config;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read configuration file " + getHousecreamConf());
        }
    }

    private void writeConfig(Properties config) {
        try {
            config.store(new FileOutputStream(getHousecreamConf()), null);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write configuration file " + getHousecreamConf());
        }
    }

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
        } catch (Throwable _) {
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
