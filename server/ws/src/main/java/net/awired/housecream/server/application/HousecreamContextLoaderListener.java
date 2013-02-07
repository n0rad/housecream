package net.awired.housecream.server.application;

import java.io.InputStream;
import javax.servlet.ServletContextEvent;
import net.awired.housecream.server.Housecream;
import org.springframework.web.context.ContextLoaderListener;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Housecream.INSTANCE.init();

        InputStream manifest = event.getServletContext().getResourceAsStream("META-INF/MANIFEST.MF");
        try {
            Housecream.INSTANCE.updateVersion(manifest);
        } finally {
            try {
                manifest.close();
            } catch (Exception e) {
            }
        }

        HousecreamHome.INSTANCE.init();
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HousecreamHome.INSTANCE.close();
        super.contextDestroyed(event);
    }
}
