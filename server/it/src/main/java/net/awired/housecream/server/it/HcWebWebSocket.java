/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.server.it;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.housecream.server.api.domain.Event;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HcWebWebSocket implements WebSocket.OnTextMessage {

    private Connection connection;
    private List<Event> events = Collections.synchronizedList(new ArrayList<Event>());
    private WebSocketClient client;
    private CountDownLatch eventsLatchCount = new CountDownLatch(1);
    private ObjectMapper objectMapper = new ObjectMapper();

    public HcWebWebSocket(WebSocketClient client) {
        this.client = client;
    }

    public HcWebWebSocket open() {
        try {
            client.open(new URI("ws://localhost:8888"), this, 10, TimeUnit.SECONDS);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetLatchEventsCounter(int size) {
        eventsLatchCount = new CountDownLatch(size);
    }

    public List<Event> awaitEvents() throws InterruptedException {
        if (!eventsLatchCount.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Countdown timeout");
        }
        return events;
    }

    //////////////////////:

    public List<Event> getEvents() {
        return events;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void onClose(int closeCode, String message) {
        this.connection = null;
        //System.out.printf( "Closing with %d and message %s%n", closeCode, message );
    }

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;
    }

    public void sendMessage(String text) {
        try {
            connection.sendMessage(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(String text) {
        try {
            this.events.add(objectMapper.readValue(text, Event.class));
            eventsLatchCount.countDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
