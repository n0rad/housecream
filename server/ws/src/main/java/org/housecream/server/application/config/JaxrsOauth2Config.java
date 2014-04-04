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

import java.util.Arrays;
import java.util.List;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.ResourceComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import fr.norad.jaxrs.oauth2.core.application.GrantTypeBasedResourceComparator;
import fr.norad.jaxrs.oauth2.core.exception.OAuthSpecExceptionMapper;
import fr.norad.jaxrs.oauth2.core.resource.OauthResource;

@Configuration
@ComponentScan(basePackages = "fr.norad.jaxrs.oauth2")
public class JaxrsOauth2Config {

    @Autowired
    private EncodingConfig encodingContext;

    @DependsOn(JaxrsConfig.CXF_BUS_BEAN)
    @Bean
    Server oauthServer(@OauthResource List<Object> oauthResources) {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setServiceBeans(oauthResources);
        sf.setAddress("/oauth2");
        sf.setResourceComparator(grantResourceComparator());
        sf.getInInterceptors().add(new LoggingInInterceptor());
        sf.getOutInterceptors().add(new LoggingOutInterceptor());

        List<Object> providers = Arrays.asList(
                encodingContext.jacksonJsonProvider(),
                new OAuthSpecExceptionMapper()
        );

        sf.setProviders(providers);
        return sf.create();
    }

    @Bean
    ResourceComparator grantResourceComparator() {
        return new GrantTypeBasedResourceComparator();
    }

}
