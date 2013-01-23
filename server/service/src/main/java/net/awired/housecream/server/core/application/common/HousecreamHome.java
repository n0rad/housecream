package net.awired.housecream.server.core.application.common;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.awired.ajsl.core.io.FileUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public enum HousecreamHome implements Closeable {

    INSTANCE;

    private HousecreamHome() {
        // shutdown hook
    }

    public void updateDbVersion(String home, String currentVersion) {
        File versionFile = new File(home + "/version");

        //TODO
        //        if (  UNKNOW_VERSION.equals(currentVersion)) {
        //            return;
        //        }

        try {
            List<String> lines = Files.readLines(versionFile, Charsets.UTF_8);
            if (lines.size() > 0 && !lines.get(0).equals(currentVersion)) {
                System.out.println("Version of DB is " + lines.get(0) + " but currently running " + currentVersion
                        + " DB will be dropped");
                FileUtils.deleteRecursively(new File(home + "/db"));
            }
        } catch (IOException e) {
            // no file found will not do anything
        } finally {
            try {
                Files.write(currentVersion.getBytes(), versionFile);
            } catch (IOException e) {
                System.err.println("cannot write housecream version to file" + versionFile);
            }
        }

    }

    @Override
    public void close() throws IOException {
        // remove lock
    }

}
