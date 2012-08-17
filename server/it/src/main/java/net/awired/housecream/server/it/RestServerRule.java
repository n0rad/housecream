package net.awired.housecream.server.it;

import java.util.Arrays;
import net.awired.ajsl.web.resource.mapper.AjslExceptionMapper;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.junit.rules.ExternalResource;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

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

        ObjectMapper restfullObjectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
        JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(restfullObjectMapper);

        factory.setProviders(Arrays.asList(jacksonJaxbJsonProvider, new AjslExceptionMapper()));

        factory.setResourceClasses(resources);
        factory.setAddress("http://localhost:" + port + "/");
        server = factory.create();
    }

    @Override
    public void after() {
        server.stop();
    }

}
