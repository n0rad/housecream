package net.awired.housecream.client;

import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS;
import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.rules.ExternalResource;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;

public class NotifyServer extends ExternalResource {

    private int port;
    private HttpServer server;
    private final Class<? extends HccNotifyResource> resource;

    public NotifyServer(int port, Class<? extends HccNotifyResource> resource) {
        this.resource = resource;
        this.port = port;
    }

    @Override
    protected void before() throws Throwable {
        //        ResourceConfig rc = newConfig();
        //        server = GrizzlyServerFactory.createHttpServer(uri, rc);

        DefaultResourceConfig resourceConfig = new DefaultResourceConfig(resource);
        resourceConfig.getProperties().put(PROPERTY_CONTAINER_REQUEST_FILTERS, new LoggingFilter());
        resourceConfig.getProperties().put(PROPERTY_CONTAINER_RESPONSE_FILTERS, new LoggingFilter());

        // The following line is to enable GZIP when client accepts it
        //        resourceConfig.getContainerResponseFilters().add(new GZIPContentEncodingFilter());
        server = GrizzlyServerFactory.createHttpServer("http://localhost:" + port, resourceConfig);
    }

    @Override
    protected void after() {
        server.stop();
    }
}
