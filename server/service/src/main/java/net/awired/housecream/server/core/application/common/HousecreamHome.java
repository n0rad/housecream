package net.awired.housecream.server.core.application.common;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.awired.ajsl.core.io.FileUtils;
import net.awired.housecream.server.core.application.Housecream;
import net.awired.housecream.server.core.application.SingleInstanceFileLocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public enum HousecreamHome {
    INSTANCE;

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String LOCKER_FILE_NAME = "lock";

    private HousecreamHome() {
        File home = Housecream.INSTANCE.getHome();
        File lockFile = new File(home, LOCKER_FILE_NAME);
        SingleInstanceFileLocker lock = new SingleInstanceFileLocker(lockFile);
        if (lock.isAppActive()) {
            throw new IllegalStateException("Housecream Home folder : " + home + " is locked");
        }
    }

    public void updateDbVersion() {
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
                FileUtils.deleteRecursively(new File(hc.getHome() + "/db"));
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