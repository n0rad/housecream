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
