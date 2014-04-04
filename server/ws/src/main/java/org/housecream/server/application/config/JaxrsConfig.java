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
import java.util.List;
import java.util.Map;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.message.Message;
import org.housecream.server.application.JaxRsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.ValidationExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.WebApplicationExceptionMapper;
import fr.norad.jaxrs.oauth2.client.interception.resourceinput.SecuredAnnotationInterceptor;

@Configuration
public class JaxrsConfig {

    public static final String CXF_BUS_BEAN = "cxf";

    @Autowired
    private EncodingConfig encodingContext;

    @Autowired
    private SecuredAnnotationInterceptor securedAnnotationInterceptor;

    @Bean(name = CXF_BUS_BEAN, destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }


    @Bean(name = "restRouter")
    @DependsOn(JaxrsConfig.CXF_BUS_BEAN)
    public Server server(@JaxRsResource List<Object> resources) {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setInInterceptors(messageInterceptors(securedAnnotationInterceptor));
        sf.getInInterceptors().add(new LoggingInInterceptor());
        sf.getOutInterceptors().add(new LoggingOutInterceptor());
        sf.setServiceBeans(resources);
        sf.setAddress("/");
        sf.setStaticSubresourceResolution(true);

        ArrayList<Object> providers = new ArrayList<>();

        providers.add(encodingContext.jacksonJsonProvider());
        providers.add(encodingContext.jAXBElementProvider());
        providers.add(new ErrorExceptionMapper());
        providers.add(new WebApplicationExceptionMapper());
        providers.add(new ValidationExceptionMapper());
        sf.setProviders(providers);
        Map<Object, Object> extensions = new HashMap<>();
        extensions.put("json", "application/json");
        extensions.put("xml", "application/xml");
        sf.setExtensionMappings(extensions);
        return sf.create();
    }

    @SafeVarargs
    private static List<Interceptor<? extends Message>> messageInterceptors(Interceptor<? extends Message>... msgInterceptors) {
        return Arrays.<Interceptor<? extends Message>>asList(msgInterceptors);
    }

}
