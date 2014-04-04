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
package org.housecream.server.application.security;


import org.apache.cxf.message.Message;
import org.housecream.server.api.domain.HcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.WebApplicationExceptionMapper;
import fr.norad.jaxrs.oauth2.api.TokenFetcher;
import fr.norad.jaxrs.oauth2.client.interception.resourceinput.SecuredAnnotationInterceptor;

public class ToggleSecuredAnnotationInterceptor extends SecuredAnnotationInterceptor {

    @Autowired
    HcProperties props;

    public ToggleSecuredAnnotationInterceptor(TokenFetcher tokenFetcher) {
        super(tokenFetcher, new ErrorExceptionMapper(), new WebApplicationExceptionMapper());
    }

    @Override
    public void handleMessage(Message message) {
        if (props.isSecurityEnabled()) {
            super.handleMessage(message);
        }
    }
}
