package net.awired.housecream.server.webapi.application;

import java.io.InputStream;
import javax.servlet.ServletContextEvent;
import net.awired.housecream.server.core.application.Housecream;
import net.awired.housecream.server.core.application.common.HousecreamHome;
import org.springframework.web.context.ContextLoaderListener;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
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

        hcHome.updateDbVersion();

        System.out.println("######################################");
        System.out.println("version : " + hc.getVersion());
        System.out.println("home : " + hc.getHome());
        System.out.println("######################################");

        super.contextInitialized(event);
    }

}
