package net.awired.housecream.server.application;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.awired.ajsl.core.io.FileLocker;
import net.awired.housecream.server.Housecream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public enum HousecreamHome implements Closeable {
    INSTANCE;

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String LOCKER_FILE_NAME = "lock";

    private FileLocker lock;
    private boolean inited;

    private HousecreamHome() {
    }

    public void init() {
        if (inited) {
            return;
        }
        lockHomeDirectory();
        updateDbVersion();
        logHousecreamInfo();
        inited = true;
    }

    @Override
    public void close() {
        lock.close();
        inited = false;
    }

    ///////////////////////////////////////////////////////////////////////

    private void logHousecreamInfo() {
        log.info("############# Housecream #############");
        log.info("## Version           : " + Housecream.INSTANCE.getVersion());
        log.info("## Home              : " + Housecream.INSTANCE.getHome());
        log.info("## Log conf          : " + Housecream.INSTANCE.getLogbackConf());
        log.info("## Plugins directory : " + Housecream.INSTANCE.getPluginDirectory());
        log.info("######################################");
    }

    private void lockHomeDirectory() {
        File home = Housecream.INSTANCE.getHome();
        lock = new FileLocker(new File(home, LOCKER_FILE_NAME));
        if (lock.isLocked()) {
            throw new SecurityException("Housecream Home folder : " + home + " is locked");
        }
    }

    private void updateDbVersion() {
        Housecream hc = Housecream.INSTANCE;
        File versionFile = new File(hc.getHome() + "/version");
        if (Housecream.VERSION_UNKNOWN.equals(hc.getVersion())) {
            return;
        }

        try {
            List<String> lines = Files.readLines(versionFile, Charsets.UTF_8);
            if (lines.size() > 0 && !lines.get(0).equals(hc.getVersion())) {
                log.warn("Version of DB is " + lines.get(0) + " but currently running " + hc.getVersion()
                        + ". DB will be dropped");
                FileUtils.deleteDirectory(new File(hc.getHome() + "/db"));
            }
        } catch (IOException e) {
            // no file found will not do anything
        } finally {
            try {
                Files.write(hc.getVersion().getBytes(), versionFile);
            } catch (IOException e) {
                System.err.println("cannot write housecream version to file" + versionFile);
            }
        }

    }

}
