package net.awired.housecream.server.webapi.application;

import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import javax.servlet.ServletContextEvent;
import net.awired.housecream.server.core.application.Housecream;
import net.awired.housecream.server.core.application.common.HousecreamHome;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.context.ContextLoaderListener;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // do not uninstall it because it does not reinstall well on hot swap
        //        AnsiConsole.systemUninstall();
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

        Housecream hc = Housecream.INSTANCE;
        HousecreamHome hcHome = HousecreamHome.INSTANCE;

        InputStream manifest = event.getServletContext().getResourceAsStream("META-INF/MANIFEST.MF");
        try {
            hc.updateVersion(manifest);
        } finally {
            try {
                manifest.close();
            } catch (Exception e) {
            }
        }

        System.out.println("######################################");
        System.out.println("version : " + hc.getVersion());
        System.out.println("home : " + hc.getHome());
        System.out.println("######################################");

        super.contextInitialized(event);
    }

}
