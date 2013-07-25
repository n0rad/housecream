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
package org.housecream.plugins.restmcu;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.plugins.api.InHousecreamPlugin;
import org.housecream.plugins.api.OutHousecreamPlugin;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotification;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.rule.Consequence;
import com.google.common.base.Preconditions;

public class RestMcuHousecreamPlugin implements InHousecreamPlugin, OutHousecreamPlugin {

    private static final String SCHEME = "restmcu";
    public static final int DEFAULT_COMPONENT_PORT = 80;

    @Override
    public Float readInValue(Message in) throws Exception {
        RestMcuLineNotification notif = in.getBody(RestMcuLineNotification.class);
        Preconditions.checkNotNull(notif, "Notification cannot be null");
        Preconditions.checkNotNull(notif.getSource(), "Source of notification cannot be null");
        return notif.getValue();
    }

    @Override
    public String scheme() {
        return SCHEME;
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint) {
        return new ImmutablePair<Object, Map<String, Object>>(action.getValue(), null);
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public URI validateAndNormalizeUri(URI uri) throws ValidationException {
        Preconditions.checkNotNull(uri, "Uri cannot be null");
        Preconditions.checkState(SCHEME.equals(uri.getScheme()), "Invalid schema for uri : " + uri);

        int port = uri.getPort();
        if (port == DEFAULT_COMPONENT_PORT) {
            port = -1;
        }
        try {
            return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(),
                    uri.getFragment());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("cannot rebuild URI after removing the default port : " + uri, e);
        }
    }

}
