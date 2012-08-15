package net.awired.housecream.server.it;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import net.awired.ajsl.web.resource.mapper.AjslResponseExceptionMapper;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class HcsItContext {

    private AjslResponseExceptionMapper responseExceptionMapper;
    private LoggingInInterceptor inLogger;
    private LoggingOutInterceptor outLogger;
    private JacksonJaxbJsonProvider jacksonJaxbJsonProvider;

    private static final String HCS_HOST_DEFAULT = "localhost";
    private static final String HCS_HOST_PROPERTY_NAME = "hcs.host";

    private static final String HCS_PORT_DEFAULT = "8080";
    private static final String HCS_PORT_PROPERTY_NAME = "hcs.port";

    private static final String HCS_PATH_DEFAULT = "/hcs/ws";
    private static final String HCS_PATH_PROPERTY_NAME = "hcs.path";

    public static String getUrl() {
        return "http://" + System.getProperty(HCS_HOST_PROPERTY_NAME, HCS_HOST_DEFAULT) //
                + ":" + System.getProperty(HCS_PORT_PROPERTY_NAME, HCS_PORT_DEFAULT) //
                + System.getProperty(HCS_PATH_PROPERTY_NAME, HCS_PATH_DEFAULT);
    }

    public HcsItContext() {
        inLogger = new LoggingInInterceptor();
        inLogger.setPrettyLogging(true);
        inLogger.setOutputLocation("<stdout>");
        outLogger = new LoggingOutInterceptor();
        outLogger.setPrettyLogging(true);
        outLogger.setOutputLocation("<stderr>");

        ObjectMapper restfullObjectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
        jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(restfullObjectMapper);
    }

    public <T> T buildResourceProxy(Class<T> clazz, HcsItSession session) {
        JAXRSClientFactoryBean sf = new JAXRSClientFactoryBean();
        sf.setProviders(Arrays.asList(jacksonJaxbJsonProvider, responseExceptionMapper));
        sf.getInInterceptors().add(inLogger);
        sf.getOutInterceptors().add(outLogger);
        sf.setResourceClass(clazz);
        sf.setAddress(getUrl());

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", "JSESSIONID=" + session.getSessionId());
        //        headers.put("Cookie2", "$Version=1");
        sf.setHeaders(headers);

        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(sf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);
        T service = sf.create(clazz);
        //        if (session.isJson()) {
        WebClient.client(service).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);
        //        }
        return service;
    }
}
