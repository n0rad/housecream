package net.awired.housecream.camel.axmpp;

import org.apache.camel.component.xmpp.XmppComponent;
import org.apache.camel.component.xmpp.XmppEndpoint;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class SharedConnectionXmppEndpoint extends XmppEndpoint {
    private XMPPConnection xmppConnection;

    public SharedConnectionXmppEndpoint(String uri, XmppComponent component, XMPPConnection xmppConnection) {
        super(uri, component);
        this.xmppConnection = xmppConnection;
    }

    @Override
    public XMPPConnection createConnection() throws XMPPException {
        if (xmppConnection != null) {
            return xmppConnection;
        }
        return super.createConnection();
    }
}
