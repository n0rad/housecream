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
package org.housecream.server.it;

import org.eclipse.jetty.websocket.WebSocketClientFactory;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import fr.norad.jaxrs.junit.LoggingRule;

public class HcWsItServer extends LoggingRule {

    private RestBuilder context = new RestBuilder().withExceptionMapper();

    private WebSocketClientFactory factory;

    public HcWsItSession session() {
        return new HcWsItSession(this);
    }

    public HcWsItSession session(String username, String password) {
        return new HcWsItSession(this, username, password);
    }

    public <T> T getResource(Class<T> clazz, HcWsItSession session) {
        T client = context.buildClient(clazz, HcWsItContext.getUrl(), session);
        return client;
    }

    public HcWebWebSocket newWebSocket() {
        HcWebWebSocket socket = new HcWebWebSocket(factory.newWebSocketClient());
        return socket;
    }

    @Override
    public void before() throws Throwable {
        HcWsItSession session = session();
        session.inpoint().deleteAll();
        session.outpoint().deleteAll();
        session.rule().deleteAll();
        session.zone().deleteAll();

        factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();
    }

    @Override
    protected void after() {
        try {
            factory.stop();
        } catch (Exception e) {
        }
    }

}
