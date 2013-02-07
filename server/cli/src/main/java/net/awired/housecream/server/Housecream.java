package net.awired.housecream.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.awired.ajsl.os.ApplicationHomeFactory;

public enum Housecream {
    INSTANCE;

    private static final String HOUSECREAM_NAME = "Housecream";
    private static final String HOUSECREAM_CONF = "housecream.conf";
    public static final String HOUSECREAM_HOME_KEY = "HOUSECREAM_HOME";

    private static final String LOG_CONF_FILE_PATH_KEY = "logback.configurationFile";
    private static final String LOG_CONF_FILE_NAME = "logback.xml";

    private static final String VERSION_MANIFEST_KEY = "HousecreamVersion";
    public static final String VERSION_UNKNOWN = "Unknow Version";

    private File home;
    private String version;
    private File logbackConf;
    private File housecreamConf;
    private File pluginDirectory;
    private boolean inited = false;

    private Housecream() {
        home = findHomeDir();
        version = findVersion(null);
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

    public void updateVersion(InputStream manifest) {
        if (manifest == null) {
            return;
        }
        version = findVersion(manifest);
    }

    public void printInfo() {
        System.out.println("Housecream Version          : " + version);
        System.out.println("Housecream Home             : " + home);
        System.out.println("Housecream Conf             : " + housecreamConf);
        System.out.println("Housecream Log conf         : " + logbackConf);
        System.out.println("Housecream Plugin directory : " + pluginDirectory);
    }

    public File getHome() {
        return home;
    }

    public String getVersion() {
        return version;
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
        } catch (Throwable _) {
        }
        return ApplicationHomeFactory.getApplicationHome().getFolder(HOUSECREAM_NAME);
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

    private String findVersion(InputStream manifestIn) {
        // runnable war
        try {
            Enumeration<URL> manifests = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (manifests.hasMoreElements()) {
                URL res = manifests.nextElement();
                Manifest manifest = new Manifest(res.openStream());
                String versionManifest = manifest.getMainAttributes().getValue(VERSION_MANIFEST_KEY);
                if (versionManifest != null) {
                    return versionManifest;
                }
            }
        } catch (IOException e) {
        }

        if (manifestIn != null) {
            // tomcat like
            try {
                Manifest manifest = new Manifest(manifestIn);
                String versionManifest = manifest.getMainAttributes().getValue(VERSION_MANIFEST_KEY);
                if (versionManifest != null) {
                    return versionManifest;
                }
            } catch (IOException e) {
            }
        }
        return VERSION_UNKNOWN;
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

}
