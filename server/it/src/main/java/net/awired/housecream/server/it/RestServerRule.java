package net.awired.housecream.server.it;

import java.util.Arrays;
import net.awired.ajsl.web.resource.mapper.AjslExceptionMapper;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.JSONProvider;
import org.junit.rules.ExternalResource;

public class RestServerRule extends ExternalResource {
    private int port;
    private final Class<?>[] resources;
    private Server server;

    public RestServerRule(int port, Class<?>... resources) {
        this.resources = resources;
        this.port = port;
    }

    @Override
    public void before() throws Throwable {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();

        JSONProvider jsonProvider = new JSONProvider();
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setDropRootElement(true);

        factory.setProviders(Arrays.asList(jsonProvider, new AjslExceptionMapper()));

        factory.setResourceClasses(resources);
        factory.setAddress("http://localhost:" + port + "/");
        server = factory.create();
    }

    @Override
    public void after() {
        server.stop();
    }

}
