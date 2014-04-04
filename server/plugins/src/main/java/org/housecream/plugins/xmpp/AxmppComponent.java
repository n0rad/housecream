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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.component.xmpp.XmppComponent;
import org.apache.camel.component.xmpp.XmppEndpoint;
import org.apache.camel.util.ServiceHelper;
import org.jivesoftware.smack.XMPPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AxmppComponent extends XmppComponent {
    private static final transient Logger LOG = LoggerFactory.getLogger(AxmppComponent.class);

    private final Map<String, XmppEndpoint> endpointCache = new HashMap<String, XmppEndpoint>();

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (endpointCache.containsKey(uri)) {
            LOG.debug("Using cached endpoint for URI {}", uri);
            return endpointCache.get(uri);
        }

        LOG.debug("Creating new endpoint for URI {}", uri);
        Collection<XmppEndpoint> values = endpointCache.values();
        XMPPConnection createConnection = null;
        if (values.size() > 0) {
            createConnection = values.iterator().next().createConnection();
        }
        XmppEndpoint endpoint = new SharedConnectionXmppEndpoint(uri, this, createConnection);

        URI u = new URI(uri);
        endpoint.setHost(u.getHost());
        endpoint.setPort(u.getPort());
        if (u.getUserInfo() != null) {
            endpoint.setUser(u.getUserInfo());
        }
        String remainingPath = u.getPath();
        if (remainingPath != null) {
            if (remainingPath.startsWith("/")) {
                remainingPath = remainingPath.substring(1);
            }

            // assume its a participant
            if (remainingPath.length() > 0) {
                endpoint.setParticipant(remainingPath);
            }
        }

        endpointCache.put(uri, endpoint);

        return endpoint;
    }

    @Override
    protected void doStop() throws Exception {
        ServiceHelper.stopServices(endpointCache.values());
        endpointCache.clear();
    }

}
