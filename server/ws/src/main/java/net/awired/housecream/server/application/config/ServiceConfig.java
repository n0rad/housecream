package net.awired.housecream.server.application.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.awired.ajsl.ws.resource.mapper.NotFoundExceptionMapper;
import net.awired.ajsl.ws.resource.mapper.UpdateExceptionMapper;
import net.awired.ajsl.ws.resource.mapper.ValidationExceptionMapper;
import net.awired.ajsl.ws.resource.mapper.generic.GenericExceptionMapper;
import net.awired.ajsl.ws.resource.mapper.generic.RuntimeExceptionMapper;
import net.awired.ajsl.ws.resource.mapper.generic.WebApplicationExceptionMapper;
import net.awired.housecream.server.service.InPointService;
import net.awired.housecream.server.service.InPointsService;
import net.awired.housecream.server.service.OutPointService;
import net.awired.housecream.server.service.OutPointsService;
import net.awired.housecream.server.service.ResourceService;
import net.awired.housecream.server.service.RuleService;
import net.awired.housecream.server.service.RulesService;
import net.awired.housecream.server.service.UserService;
import net.awired.housecream.server.service.UsersService;
import net.awired.housecream.server.service.ZoneService;
import net.awired.housecream.server.service.ZonesService;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Inject
    private ResourceService resourceService;
    @Inject
    private InPointService inPointService;
    @Inject
    private InPointsService inPointsService;
    @Inject
    private OutPointService outPointService;
    @Inject
    private OutPointsService outPointsService;
    @Inject
    private RuleService ruleService;
    @Inject
    private RulesService rulesService;
    @Inject
    private ZoneService zoneService;
    @Inject
    private ZonesService zonesService;
    @Inject
    private UserService userService;
    @Inject
    private UsersService usersService;

    @Autowired
    private EncodingConfig encodingContext;

    @Bean(name = "restRouter")
    public Server server() {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.getInInterceptors().add(new LoggingInInterceptor());
        sf.getOutInterceptors().add(new LoggingOutInterceptor());
        sf.setServiceBeanObjects(resourceService, //
                inPointService, //
                inPointsService, //
                outPointService, //
                outPointsService, //
                ruleService, //
                rulesService, //
                zoneService, //
                zonesService, //
                userService, //
                usersService);
        sf.setAddress("/");
        sf.setStaticSubresourceResolution(true);

        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(encodingContext.jacksonJsonProvider());
        arrayList.add(encodingContext.jAXBElementProvider());
        arrayList.add(new GenericExceptionMapper());
        arrayList.add(new RuntimeExceptionMapper());
        arrayList.add(new WebApplicationExceptionMapper());
        arrayList.add(new ValidationExceptionMapper());
        arrayList.add(new NotFoundExceptionMapper());
        arrayList.add(new UpdateExceptionMapper());
        sf.setProviders(Arrays.asList(encodingContext.jacksonJsonProvider(), encodingContext.jAXBElementProvider()));
        Map<Object, Object> extensions = new HashMap<Object, Object>();
        extensions.put("json", "application/json");
        extensions.put("xml", "application/xml");
        sf.setExtensionMappings(extensions);
        return sf.create();
    }

}
