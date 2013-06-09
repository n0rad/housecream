package net.awired.housecream.server.it;

import net.awired.ajsl.test.LoggingRule;
import net.awired.ajsl.ws.rest.RestBuilder;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class HcWsItServer extends LoggingRule {

    private RestBuilder context = new RestBuilder();

    private WebSocketClientFactory factory;

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
        //        HcWsItSession session = session();
        //        session.inpoint().deleteAll();
        //        session.outpoint().deleteAll();
        //        session.rule().deleteAll();
        //        session.zone().deleteAll();

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
