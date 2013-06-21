package net.awired.housecream.server.application;

import static net.awired.housecream.server.Housecream.VERSION_MANIFEST_KEY;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import net.awired.housecream.server.Housecream;
import net.awired.housecream.server.application.config.RootConfig;
import net.awired.servlet.sample.html.template.WarManifestUtils;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        initHousecream(container);
        container.addListener(new HousecreamContextLoaderListener(initApplicationContext()));
        ServletRegistration.Dynamic cxf = container.addServlet("dispatcher", new CXFServlet());
        cxf.setLoadOnStartup(1);
        cxf.addMapping("/ws/*");
    }

    private void initHousecream(ServletContext container) {
        Housecream.HOUSECREAM.init();
        Housecream.HOUSECREAM.discoveredVersion(WarManifestUtils.getWarManifestAttribute(container,
                VERSION_MANIFEST_KEY));
    }

    private WebApplicationContext initApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan(RootConfig.class.getPackage().getName());
        context.setDisplayName("Housecream");
        return context;
    }
}
