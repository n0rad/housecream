package net.awired.housecream.server.it;

import net.awired.ajsl.test.LoggingRule;
import net.awired.ajsl.ws.rest.RestContext;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class HcWsItServer extends LoggingRule {

    private RestContext context = new RestContext();

    private WebSocketClientFactory factory;

    public HcWsItSession session() {
        return new HcWsItSession(this);
    }

    public <T> T getResource(Class<T> clazz, HcWsItSession session) {
        T client = context.prepareClient(clazz, HcWsItContext.getUrl(), session.getSessionId(), session.isUseJson());
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
