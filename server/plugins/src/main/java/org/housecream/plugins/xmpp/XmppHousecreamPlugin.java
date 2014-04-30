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
package org.housecream.plugins.xmpp;

import java.net.URI;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.plugins.api.HousecreamPlugin;
import org.housecream.plugins.gmail.GoogleServiceConfig;
import org.housecream.server.api.domain.PluginDescription;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.rule.Consequence;

public class XmppHousecreamPlugin implements HousecreamPlugin {

    private final PluginDescription pluginDescription;

    public XmppHousecreamPlugin() {
        pluginDescription = new PluginDescription()
                .setId("axmpp")
                .setName("Xmpp")
                .setDescription("desc")
                .setConfigClass(GoogleServiceConfig.class);
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, Point point) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Float readValue(Message in) {
        return null;
    }

    @Override
    public PluginDescription description() {
        return pluginDescription;
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public URI validateAndNormalizeUri(URI pointUri) throws ValidationException {
        return null;
    }

}
