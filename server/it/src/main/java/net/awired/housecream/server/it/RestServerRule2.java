package net.awired.housecream.server.it;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.junit.rules.ExternalResource;

public class RestServerRule2 extends ExternalResource {
    private int port;
    private final Class<?> resource;
    private Server server;

    public RestServerRule2(int port, Class<?> resource) {
        this.resource = resource;
        this.port = port;
    }

    @Override
    public void before() throws Throwable {
        //        OutputLightResource helloWorld = new OutputLightResource();
        //create WebService service factory
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setServiceClass(resource);
        factory.setAddress("http://localhost:" + port + "/");
        //        factory.setServiceBean(helloWorld);
        server = factory.create();
    }

    @Override
    public void after() {
        server.stop();
    }

}
