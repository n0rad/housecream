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
package org.housecream.plugins.api;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.server.api.domain.PluginDescription;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.rule.Consequence;

public interface HousecreamPlugin {

    URL DEFAULT_LOGO = HousecreamPlugin.class.getResource("/plugin-default-logo.png");

    PluginDescription description();

    default URL getLogo() {
        return DEFAULT_LOGO;
    }

    /**
     * config class for plugins may be needed for example for client_id & client_secret
     */
    default <T extends Config> void setConfig(T config) {
    }

    //TODO MOVE
    //
    //    Float getCurrentValue(Point point, CamelContext camelContext);
    //

    boolean isCommand();

    /**
     * @return can return null if valid.
     */
    URI validateAndNormalizeUri(URI pointUri) throws ValidationException;

    Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, Point point);

    //    String getInFormDirective();

    Float readValue(Message in);

}
