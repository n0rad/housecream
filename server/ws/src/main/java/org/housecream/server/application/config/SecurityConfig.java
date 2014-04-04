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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.application.security.ToggleSecuredAnnotationInterceptor;
import org.housecream.server.storage.security.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import fr.norad.jaxrs.oauth2.api.TokenFetcher;
import fr.norad.jaxrs.oauth2.client.interception.resourceinput.SecuredAnnotationInterceptor;
import fr.norad.jaxrs.oauth2.api.Client;
import fr.norad.jaxrs.oauth2.api.ClientNotFoundException;
import fr.norad.jaxrs.oauth2.core.service.PasswordHasher;

@Configuration
public class SecurityConfig {

    private static final String HOUSECREAM_WEB_CLIENT_SECRET_NAME = "HousecreamWebClientSecret";
    private static final String HOUSECREAM_WEB_CLIENT_ID_NAME = "HousecreamWebClientId";

    @Autowired
    HcProperties props;

    @Bean
    SecureRandom secureRandom() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        random.setSeed(random.generateSeed(10));
        return random;
    }

    @Bean
    SecuredAnnotationInterceptor authorizationInterceptor(TokenFetcher tokenFetcher, ClientDao clientDao) {
        ToggleSecuredAnnotationInterceptor interceptor = new ToggleSecuredAnnotationInterceptor(tokenFetcher);
        provideWebClient(clientDao);
        return interceptor;
    }

    @Bean
    PasswordHasher passwordHasher() {
        return new PasswordHasher(props.getSecurityGlobalSaltSecret());
    }

    void provideWebClient(ClientDao clientDao) {
        try {
            Client webClient = clientDao.currentWebClient();
            System.setProperty(HOUSECREAM_WEB_CLIENT_ID_NAME, webClient.getId());
            System.setProperty(HOUSECREAM_WEB_CLIENT_SECRET_NAME, webClient.getSecret());
        } catch (ClientNotFoundException e) {
            throw new IllegalStateException("Web client not found", e);
        }
    }
}
