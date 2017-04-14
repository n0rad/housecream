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
package org.housecream.camel.axmpp;

import java.net.URI;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultConsumer;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link org.apache.camel.Consumer Consumer} which listens to XMPP packets
 * 
 * @version
 */
public class AXmppConsumer extends DefaultConsumer implements PacketListener, MessageListener, ChatManagerListener {
    private static final transient Logger LOG = LoggerFactory.getLogger(AXmppConsumer.class);
    private final SharedConnectionXmppEndpoint endpoint;
    private MultiUserChat muc;
    private Chat privateChat;
    private ChatManager chatManager;
    private XMPPConnection connection;

    public AXmppConsumer(SharedConnectionXmppEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        connection = endpoint.createConnection();
        chatManager = connection.getChatManager();
        chatManager.addChatListener(this);

        if (endpoint.getRoom() == null) {
            privateChat = chatManager.getThreadChat(endpoint.getChatId());

            if (privateChat != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Adding listener to existing chat opened to " + privateChat.getParticipant());
                }
                privateChat.addMessageListener(this);
            } else {
                privateChat = connection.getChatManager().createChat(endpoint.getParticipant(), endpoint.getChatId(),
                        this);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Opening private chat to " + privateChat.getParticipant());
                }
            }
        } else {
            // add the presence packet listener to the connection so we only get packets that concerns us
            // we must add the listener before creating the muc
            final ToContainsFilter toFilter = new ToContainsFilter(endpoint.getParticipant());
            final AndFilter packetFilter = new AndFilter(new PacketTypeFilter(Presence.class), toFilter);
            connection.addPacketListener(this, packetFilter);

            muc = new MultiUserChat(connection, endpoint.resolveRoom(connection));
            muc.addMessageListener(this);
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxChars(0); // we do not want any historical messages

            muc.join(endpoint.getNickname(), null, history, SmackConfiguration.getPacketReplyTimeout());
            if (LOG.isInfoEnabled()) {
                LOG.info("Joined room: {} as: {}", muc.getRoom(), endpoint.getNickname());
            }
        }

        super.doStart();
    }

    //    private void sendMessage(final String participant, Exchange exchange) {
    //        try {
    //            // make sure we are connected
    //            if (!connection.isConnected()) {
    //                if (LOG.isDebugEnabled()) {
    //                    LOG.debug("Reconnecting to: {}", XmppEndpoint.getConnectionMessage(connection));
    //                }
    //                connection.connect();
    //            }
    //        } catch (XMPPException e) {
    //            throw new RuntimeExchangeException("Cannot connect to: " + XmppEndpoint.getConnectionMessage(connection),
    //                    exchange, e);
    //        }
    //
    //        ChatManager chatManager = connection.getChatManager();
    //
    //        LOG.trace("Looking for existing chat instance with thread ID {}", endpoint.getChatId());
    //        Chat chat = chatManager.getThreadChat(endpoint.getChatId());
    //        if (chat == null) {
    //            LOG.trace("Creating new chat instance with thread ID {}", endpoint.getChatId());
    //            chat = chatManager.createChat(participant, endpoint.getChatId(), new MessageListener() {
    //                @Override
    //                public void processMessage(Chat chat, Message message) {
    //                    // not here to do conversation
    //                    if (LOG.isDebugEnabled()) {
    //                        LOG.debug("Received and discarding message from {} : {}", participant, message.getBody());
    //                    }
    //                }
    //            });
    //        }
    //
    //        Message message = null;
    //        try {
    //            message = new Message();
    //            message.setTo(participant);
    //            //            message.setThread(endpoint.getChatId());
    //            message.setType(Message.Type.normal);
    //
    //            endpoint.getBinding().populateXmppMessage(message, exchange);
    //
    //            if (LOG.isDebugEnabled()) {
    //                LOG.debug("Sending XMPP message to {} from {} : {}", new Object[] { endpoint.getParticipant(),
    //                        endpoint.getUser(), message.getBody() });
    //            }
    //            chat.sendMessage(message);
    //        } catch (XMPPException xmppe) {
    //            throw new RuntimeExchangeException("Cannot send XMPP message: to " + endpoint.getParticipant() + " from "
    //                    + endpoint.getUser() + " : " + message + " to: " + XmppEndpoint.getConnectionMessage(connection),
    //                    exchange, xmppe);
    //        } catch (Exception e) {
    //            throw new RuntimeExchangeException("Cannot send XMPP message to " + endpoint.getParticipant() + " from "
    //                    + endpoint.getUser() + " : " + message + " to: " + XmppEndpoint.getConnectionMessage(connection),
    //                    exchange, e);
    //        }
    //    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (muc != null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Leaving room: {}", muc.getRoom());
            }
            muc.removeMessageListener(this);
            muc.leave();
            muc = null;
        }
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        if (!createdLocally) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Accepting incoming chat session from " + chat.getParticipant());
            }
            chat.addMessageListener(this);
        }
    }

    @Override
    public void processPacket(Packet packet) {
        if (packet instanceof Message) {
            processMessage(null, (Message) packet);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received XMPP message for {} from {} : {}",
                    new Object[] { endpoint.getUser(), endpoint.getParticipant(), message.getBody() });
        }

        Exchange exchange = endpoint.createExchange(ExchangePattern.InOut, message);
        try {
            getProcessor().process(exchange);
        } catch (Exception e) {
            exchange.setException(e);
        } finally {
            // must remove message from muc to avoid messages stacking up and causing OutOfMemoryError
            // pollMessage is a non blocking method
            // (see http://issues.igniterealtime.org/browse/SMACK-129)
            if (muc != null) {
                muc.pollMessage();
            }
        }
        sendResponseIfNeeded(message, exchange);
    }

    private void sendResponseIfNeeded(Message message, Exchange requestExchange) {
        Exchange responseExchange = endpoint.createExchange();
        if (responseExchange.getException() != null) {
            responseExchange.getIn().setBody(responseExchange.getException().toString());
        } else {
            responseExchange.getIn().setBody(requestExchange.getIn().getBody());
        }

        try {

            URI current = new URI(getEndpoint().getEndpointUri());
            String other = current.getScheme() + "://" + current.getAuthority() + "/"
                    + message.getFrom().split("/")[0] + "?" + current.getQuery();
            Endpoint createEndpoint = endpoint.getComponent().createEndpoint(other);
            Producer createProducer = createEndpoint.createProducer();
            //            Producer createProducer = endpoint.createPrivateChatProducer("alemaire@norad.fr"); //message.getFrom()
            createProducer.start();
            createProducer.process(responseExchange);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //        sendMessage(message.getFrom(), requestExchange);
    }
}
