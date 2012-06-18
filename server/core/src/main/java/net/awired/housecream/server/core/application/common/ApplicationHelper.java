package net.awired.housecream.server.core.application.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.common.io.CharStreams;

public class ApplicationHelper {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ApplicationHelper.class);

    public static final String UNKNOW_VERSION = "Unknow Version";
    public static final String HOME_KEY = "HOUSECREAM_HOME";
    public static final String MANIFEST_VERSION_KEY = "HouseCreamVersion";

    public static final String LOG_HOUSECREAM_AWIRED = "log.housecream.awired";
    public static final String LOG_HOUSECREAM_ROOT = "log.housecream.root";

    private static String version;

    public static String getVersion() {
        return getVersion(null);
    }

    public static String getVersion(InputStream manifestIn) {
        if (version == null) {
            version = findVersion(manifestIn);
        }
        return version;
    }

    private static String findVersion(InputStream manifestIn) {
        // runnnable war
        try {
            Enumeration<URL> manifests = ApplicationHelper.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (manifests.hasMoreElements()) {
                URL res = manifests.nextElement();
                Manifest manifest = new Manifest(res.openStream());
                String versionManifest = manifest.getMainAttributes().getValue(MANIFEST_VERSION_KEY);
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
                String versionManifest = manifest.getMainAttributes().getValue(MANIFEST_VERSION_KEY);
                if (versionManifest != null) {
                    return versionManifest;
                }
            } catch (IOException e) {
            }
        }
        return UNKNOW_VERSION;
    }

    public static String findHomeDir() {

        // TODO redirect to tmp or $TMP or check at start in cli if home is not
        // writable
        // if (home == null) {
        // home = "/tmp";
        // }

        // check JNDI for the home directory first
        try {
            InitialContext iniCtxt = new InitialContext();
            Context env = (Context) iniCtxt.lookup("java:comp/env");
            String value = (String) env.lookup(HOME_KEY);
            if (value != null && value.trim().length() > 0) {
                return value.trim();
            }

            value = (String) iniCtxt.lookup(HOME_KEY);
            if (value != null && value.trim().length() > 0) {
                return value.trim();
            }
        } catch (NamingException e) {
            // ignore
        }

        // finally check the system property
        String sysProp = System.getProperty(HOME_KEY);
        if (sysProp != null) {
            return sysProp.trim();
        }

        // look at the env var next
        try {
            String env = System.getenv(HOME_KEY);
            if (env != null) {
                return env.trim();
            }
        } catch (Throwable _) {
            // when this code runs on JDK1.4, this method fails
            // fall through to the next method
        }

        // if for some reason we can't put it within the webapp, use home
        // directory.
        return System.getProperty("user.home") + "/.housecream";
    }

    public static void changeLogLvl() {
        try {
            InputStream logConfStream = ApplicationHelper.class.getResourceAsStream("/housecream-logback.xml");
            String logConfString = CharStreams.toString(new InputStreamReader(logConfStream));
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(lc);
                lc.reset();
                configurator.doConfigure(new ByteArrayInputStream(logConfString.getBytes()));
            } catch (JoranException je) {
                je.printStackTrace();
            }
            StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
        } catch (IOException e) {
            LOG.error("Can not change application log level", e);
        }

        // don't change root lvl as is may put hibernate or jetty in debug
        //        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        //        root.setLevel(loglvl);
    }
}
