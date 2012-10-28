package net.awired.housecream.camel.axmpp;

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
