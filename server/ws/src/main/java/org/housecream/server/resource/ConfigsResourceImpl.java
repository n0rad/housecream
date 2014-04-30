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


import java.util.Map;
import org.housecream.server.api.domain.config.configDefinition;
import org.housecream.server.api.resource.ConfigsResource;
import org.housecream.server.application.ConfigHolder;
import org.housecream.server.application.JaxrsResource;
import org.housecream.server.storage.dao.ConfigDao;
import org.springframework.beans.factory.annotation.Autowired;

@JaxrsResource
public class ConfigsResourceImpl implements ConfigsResource {
    @Autowired
    private PropertyResourceImpl configResource;

    @Autowired
    private ConfigHolder configHolder;

    @Override
    public configDefinition getConfigDefinition() {
        return configHolder.getConfigDefinition();
    }

    @Override
    public Map<String, Object> getConfig() {
        return configHolder.getConfigValues();
    }

    @Override
    public PropertyResource property(String name) {
        return configResource;
    }

    @JaxrsResource
    public static class PropertyResourceImpl implements PropertyResource {
        @Autowired
        private ConfigDao configDao;

        @Autowired
        private ConfigHolder configHolder;

        @Override
        public void setProperty(String name, String value) {
            configDao.saveProperty(name, value);
            configHolder.setValue(name, value);
        }
    }
}
