package net.awired.housecream.server.application;

import javax.servlet.ServletContextEvent;
import net.awired.ajsl.web.WarManifestUtils;
import net.awired.housecream.server.Housecream;
import org.springframework.web.context.ContextLoaderListener;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Housecream.INSTANCE.init();

        Housecream.INSTANCE.updateVersion(WarManifestUtils.getWarManifestAttribute(event.getServletContext(),
                Housecream.VERSION_MANIFEST_KEY));

        HousecreamHome.INSTANCE.init();
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HousecreamHome.INSTANCE.close();
        super.contextDestroyed(event);
    }
}
