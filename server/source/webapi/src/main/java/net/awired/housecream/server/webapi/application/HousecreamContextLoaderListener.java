package net.awired.housecream.server.webapi.application;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import javax.servlet.ServletContextEvent;
import net.awired.housecream.server.core.application.common.ApplicationHelper;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.context.ContextLoaderListener;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        AnsiConsole.systemUninstall();
        SLF4JBridgeHandler.uninstall();
        super.contextDestroyed(event);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {

        AnsiConsole.systemInstall();

        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();

        String home = ApplicationHelper.findHomeDir();
        String version = getVersion(event);
        System.setProperty(ApplicationHelper.HOME_KEY, home);

        ApplicationHelper.changeLogLvl();
        updateDbVersion(home, version);

        System.out.println("######################################");
        System.out.println("version : " + version);
        System.out.println("home : " + System.getProperty(ApplicationHelper.HOME_KEY));
        System.out.println("######################################");

        super.contextInitialized(event);
    }

    private String getVersion(ServletContextEvent event) {
        InputStream in = event.getServletContext().getResourceAsStream("META-INF/MANIFEST.MF");
        return ApplicationHelper.getVersion(in);
    }

    private void updateDbVersion(String home, String currentVersion) {
        File versionFile = new File(home + "/version");

        if (ApplicationHelper.UNKNOW_VERSION.equals(currentVersion)) {
            return;
        }

        try {
            List<String> lines = Files.readLines(versionFile, Charsets.UTF_8);
            if (lines.size() > 0 && !lines.get(0).equals(currentVersion)) {
                System.out.println("Version of DB is " + lines.get(0) + " but currently running " + currentVersion
                        + " DB will be dropped");
                Files.deleteRecursively(new File(home + "/db"));
            }
        } catch (IOException e) {
            // no file found will not do anything
        } finally {
            try {
                Files.write(currentVersion.getBytes(), versionFile);
            } catch (IOException e) {
                System.err.println("cannot write visuwall version to file" + versionFile);
            }
        }

    }
}
