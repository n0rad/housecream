package net.awired.housecream.server.it;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.test.LoggingRule;
import net.awired.ajsl.test.RestContext;
import net.awired.housecream.server.common.resource.HcRestMcuNotifyResource;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.common.resource.InPointsResource;
import net.awired.housecream.server.common.resource.OutPointResource;
import net.awired.housecream.server.common.resource.OutPointsResource;
import net.awired.housecream.server.common.resource.RuleResource;
import net.awired.housecream.server.common.resource.RulesResource;
import net.awired.housecream.server.common.resource.ZoneResource;
import net.awired.housecream.server.common.resource.ZonesResource;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

public class HcsItServer extends LoggingRule {

    RestContext context = new RestContext();

    private WebSocketClientFactory factory;

    public InPointResource inPointResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(InPointResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public InPointsResource inPointsResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(InPointsResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public OutPointsResource outPointsResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(OutPointsResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public OutPointResource outPointResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(OutPointResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public RulesResource rulesResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(RulesResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public RuleResource ruleResource() {
        HcsItSession hcsItSession = new HcsItSession();
        hcsItSession.setJson(true);
        return context.prepareClient(RuleResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public ZoneResource zoneResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(ZoneResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public ZonesResource zonesResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(ZonesResource.class, HcsItContext.getUrl(), hcsItSession.getSessionId(),
                hcsItSession.isJson());
    }

    public HcRestMcuNotifyResource notifyResource() {
        HcsItSession hcsItSession = new HcsItSession();
        return context.prepareClient(HcRestMcuNotifyResource.class, HcsItContext.getUrl(),
                hcsItSession.getSessionId(), hcsItSession.isJson());
    }

    public HcwWebSocket webSocketConnection() {
        WebSocketClient client = factory.newWebSocketClient();

        HcwWebSocket socket = new HcwWebSocket(client);
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
