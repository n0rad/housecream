package net.awired.housecream.camel.axmpp;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.xmpp.XmppComponent;
import org.apache.camel.component.xmpp.XmppEndpoint;
import org.apache.camel.component.xmpp.XmppMessage;
import org.apache.camel.impl.DefaultExchange;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

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

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new AXmppConsumer(this, processor);
    }

    @Override
    public Producer createProducer() throws Exception {
        return super.createProducer();
    }

    public Exchange createExchange(ExchangePattern pattern, Message message) {
        Exchange exchange = new DefaultExchange(this, pattern);
        exchange.setProperty(Exchange.BINDING, getBinding());
        exchange.setIn(new XmppMessage(message));
        return exchange;
    }

}
