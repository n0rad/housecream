package net.awired.housecream.server.it;

import net.awired.ajsl.test.LoggingRule;
import net.awired.ajsl.ws.rest.RestContext;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class HcsItServer extends LoggingRule {

    private RestContext context = new RestContext();

    private WebSocketClientFactory factory;

    public HcsItSession session() {
        return new HcsItSession(this);
    }

    public <T> T getResource(Class<T> clazz, HcsItSession session) {
        T client = context.prepareClient(clazz, HcsItContext.getUrl(), session.getSessionId(), session.isUseJson());
        return client;
    }

    public HcwWebSocket newWebSocket() {
        HcwWebSocket socket = new HcwWebSocket(factory.newWebSocketClient());
        return socket;
    }

    @Override
    public void before() throws Throwable {
        HcsItSession session = session();
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
