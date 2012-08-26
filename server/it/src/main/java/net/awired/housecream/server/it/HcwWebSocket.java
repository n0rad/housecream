package net.awired.housecream.server.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.housecream.server.common.domain.Event;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HcwWebSocket implements WebSocket.OnTextMessage {

    private Connection connection;
    private List<Event> events = Collections.synchronizedList(new ArrayList<Event>());
    private WebSocketClient client;
    private CountDownLatch eventsLatchCount = new CountDownLatch(1);
    private ObjectMapper objectMapper = new ObjectMapper();

    public HcwWebSocket(WebSocketClient client) {
        this.client = client;
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
