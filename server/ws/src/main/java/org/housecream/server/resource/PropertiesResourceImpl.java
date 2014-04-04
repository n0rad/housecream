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
package org.housecream.server.resource;


import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.api.resource.PropertiesResource;
import org.housecream.server.application.JaxRsResource;
import org.housecream.server.storage.dao.HcPropertiesDao;
import org.springframework.beans.factory.annotation.Autowired;

@JaxRsResource
public class PropertiesResourceImpl implements PropertiesResource {
    @Autowired
    private PropertyResourceImpl propertyResource;

    @Autowired
    private HcProperties properties;

    @Override
    public HcProperties getProperties() {
        return properties;
    }

    @Override
    public PropertyResource property(String propertyName) {
        return propertyResource;
    }

    @JaxRsResource
    public static class PropertyResourceImpl implements PropertyResource {
        @Autowired
        private HcPropertiesDao propertiesDao;

        @Autowired
        private HcProperties properties;

        @Override
        public void setProperty(String propertyName, String propertyValue) {
            properties.setValue(propertyName, propertyValue);
            propertiesDao.saveProperty(propertyName, propertyValue);
        }
    }
}
