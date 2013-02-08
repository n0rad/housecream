package net.awired.housecream.server.it;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.test.LoggingRule;
import net.awired.ajsl.ws.rest.RestContext;
import net.awired.housecream.server.api.resource.InPointResource;
import net.awired.housecream.server.api.resource.InPointsResource;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.api.resource.OutPointsResource;
import net.awired.housecream.server.api.resource.RuleResource;
import net.awired.housecream.server.api.resource.RulesResource;
import net.awired.housecream.server.api.resource.ZoneResource;
import net.awired.housecream.server.api.resource.ZonesResource;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class HcWsItServer extends LoggingRule {

    RestContext context = new RestContext();

    private WebSocketClientFactory factory;

    public InPointResource inPointResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(InPointResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public InPointsResource inPointsResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(InPointsResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public OutPointsResource outPointsResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(OutPointsResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public OutPointResource outPointResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(OutPointResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public RulesResource rulesResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(RulesResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public RuleResource ruleResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        hcWsItSession.setJson(true);
        return context.prepareClient(RuleResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public ZoneResource zoneResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(ZoneResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public ZonesResource zonesResource() {
        HcWsItSession hcWsItSession = new HcWsItSession();
        return context.prepareClient(ZonesResource.class, HcWsItContext.getUrl(), hcWsItSession.getSessionId(),
                hcWsItSession.isJson());
    }

    public HcWebWebSocket webSocketConnection() {
        WebSocketClient client = factory.newWebSocketClient();

        HcWebWebSocket socket = new HcWebWebSocket(client);
        try {
            client.open(new URI("ws://localhost:8888"), socket, 10, TimeUnit.SECONDS);
            return socket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void before() throws Throwable {
        inPointsResource().deleteAllInPoints();
        outPointsResource().deleteAllOutPoints();
        rulesResource().deleteAllRules();
        zonesResource().deleteAllZones();

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
