package net.awired.housecream.client.poc;

import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS;
import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import org.glassfish.grizzly.http.server.HttpServer;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;

public class HccPocServer {
    private int port;
    private HttpServer server;
    private final Class<? extends HccNotifyResource> resource;

    //    org.eclipse.jetty.server.Server server;

    public HccPocServer(int port, Class<? extends HccNotifyResource> resource) {
        this.resource = resource;
        this.port = port;
    }

    public void start() throws Exception {
        //        ClassNamesResourceConfig r = new ClassNamesResourceConfig(HttpJsonRpc.class);
        DefaultResourceConfig resourceConfig = new DefaultResourceConfig(resource);
        //        resourceConfig.getContainerResponseFilters().add(BufferResponseFilter.class);
        resourceConfig.getProperties().put(PROPERTY_CONTAINER_REQUEST_FILTERS, new LoggingFilter());
        resourceConfig.getProperties().put(PROPERTY_CONTAINER_RESPONSE_FILTERS, new LoggingFilter());

        // The following line is to enable GZIP when client accepts it
        //        resourceConfig.getContainerResponseFilters().add(new GZIPContentEncodingFilter());
        server = GrizzlyServerFactory.createHttpServer("http://192.168.42.211:" + port, resourceConfig);
        server.getListeners().iterator().next().setChunkingEnabled(false);
        //        System.out.println(size);
        //        server.getHttpHandler
    }

    //    public void start() throws Exception {
    //
    //        server = new org.eclipse.jetty.server.Server(port);
    //        server.setHandler(new WebAppContext("src/main/webapp", "/"));
    //        server.start();
    //    }

    public void stop() {
        //        server.stop();
    }

}
