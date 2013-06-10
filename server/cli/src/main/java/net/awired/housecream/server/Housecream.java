package net.awired.housecream.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.awired.ajsl.core.io.JarManifestUtils;
import net.awired.ajsl.os.ApplicationHomeFactory;

public enum Housecream {
    INSTANCE;

    private static final String HOUSECREAM_NAME = "Housecream";
    //    private static final String HOUSECREAM_CONF = "housecream.conf";
    public static final String HOUSECREAM_HOME_KEY = "HOUSECREAM_HOME";

    private static final String LOG_CONF_FILE_PATH_KEY = "logback.configurationFile";
    private static final String LOG_CONF_FILE_NAME = "logback.xml";

    public static final String VERSION_MANIFEST_KEY = "HousecreamVersion";
    public static final String VERSION_UNKNOWN = "Unknow Version";

    private File home;
    private String version;
    private File logbackConf;
    //    private File housecreamConf;
    private File pluginDirectory;
    private boolean inited = false;

    private Housecream() {
        home = findHomeDir();
        version = JarManifestUtils.getManifestAttribute(VERSION_MANIFEST_KEY);
        if (version == null) {
            version = VERSION_UNKNOWN;
        }
        logbackConf = new File(home, LOG_CONF_FILE_NAME);
        //        housecreamConf = new File(home, HOUSECREAM_CONF);
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

    public void updateVersion(String versionFromWar) {
        if (versionFromWar != null && (version == null || VERSION_UNKNOWN.equals(version))) {
            version = versionFromWar;
        }
    }

    public void printInfo() {
    }

    public File getHome() {
        return home;
    }

    public String getVersion() {
        return version;
    }

    //////////////////////////////////

    public File findDefaultOsHomeDirectory() {
        return ApplicationHomeFactory.getApplicationHome().getFolder(HOUSECREAM_NAME);
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

    //    public File getHousecreamConf() {
    //        return housecreamConf;
    //    }

    public File getPluginDirectory() {
        return pluginDirectory;
    }

    public File getLogbackConf() {
        return logbackConf;
    }

}
