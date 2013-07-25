/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.application.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.housecream.server.api.resource.InPointResource;
import org.housecream.server.api.resource.InPointsResource;
import org.housecream.server.api.resource.OutPointResource;
import org.housecream.server.api.resource.OutPointsResource;
import org.housecream.server.api.resource.RuleResource;
import org.housecream.server.api.resource.RulesResource;
import org.housecream.server.api.resource.UserResource;
import org.housecream.server.api.resource.UsersResource;
import org.housecream.server.api.resource.ZonesResource;
import org.housecream.server.application.DocService;
import org.housecream.server.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import fr.norad.jaxrs.client.server.resource.mapper.NotFoundExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.UpdateExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.ValidationExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.generic.GenericExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.generic.RuntimeExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.generic.WebApplicationExceptionMapper;

@Configuration
public class ServiceConfig {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private InPointResource inPointService;
    @Autowired
    private InPointsResource inPointsService;
    @Autowired
    private OutPointResource outPointService;
    @Autowired
    private OutPointsResource outPointsService;
    @Autowired
    private RuleResource ruleService;
    @Autowired
    private RulesResource rulesService;
    //    @Inject
    //    private ZoneResource zoneService;
    @Autowired
    private ZonesResource zonesService;
    @Autowired
    private UserResource userService;
    @Autowired
    private UsersResource usersService;
    @Autowired
    private DocService docService;

    @Autowired
    private EncodingConfig encodingContext;

    @Bean(name = "cxf", destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean(name = "restRouter")
    public Server server() {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.getInInterceptors().add(new LoggingInInterceptor());
        sf.getOutInterceptors().add(new LoggingOutInterceptor());
        sf.setServiceBeanObjects(docService, //                
                resourceService, //
                inPointService, //
                inPointsService, //
                outPointService, //
                outPointsService, //
                ruleService, //
                rulesService, //
                //                zoneService, //
                zonesService, //
                userService, //
                usersService);
        sf.setAddress("/");
        sf.setStaticSubresourceResolution(true);

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(encodingContext.jacksonJsonProvider());
        arrayList.add(encodingContext.jAXBElementProvider());
        arrayList.add(new GenericExceptionMapper());
        arrayList.add(new RuntimeExceptionMapper());
        arrayList.add(new WebApplicationExceptionMapper());
        arrayList.add(new ValidationExceptionMapper());
        arrayList.add(new NotFoundExceptionMapper());
        arrayList.add(new UpdateExceptionMapper());
        sf.setProviders(Arrays.asList(encodingContext.jacksonJsonProvider(), encodingContext.jAXBElementProvider()));
        Map<Object, Object> extensions = new HashMap<>();
        extensions.put("json", "application/json");
        extensions.put("xml", "application/xml");
        sf.setExtensionMappings(extensions);
        return sf.create();
    }

}
