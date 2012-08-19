package net.awired.housecream.server.it;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.test.LoggingRule;
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

    private HcsItContext context = new HcsItContext();

    private WebSocketClientFactory factory;

    public InPointResource inPointResource() {
        return context.buildResourceProxy(InPointResource.class, new HcsItSession());
    }

    public InPointsResource inPointsResource() {
        return context.buildResourceProxy(InPointsResource.class, new HcsItSession());
    }

    public OutPointsResource outPointsResource() {
        return context.buildResourceProxy(OutPointsResource.class, new HcsItSession());
    }

    public OutPointResource outPointResource() {
        return context.buildResourceProxy(OutPointResource.class, new HcsItSession());
    }

    public RulesResource rulesResource() {
        return context.buildResourceProxy(RulesResource.class, new HcsItSession());
    }

    public RuleResource ruleResource() {
        HcsItSession session = new HcsItSession();
        session.setJson(true);
        return context.buildResourceProxy(RuleResource.class, session);
    }

    public ZoneResource zoneResource() {
        return context.buildResourceProxy(ZoneResource.class, new HcsItSession());
    }

    public ZonesResource zonesResource() {
        return context.buildResourceProxy(ZonesResource.class, new HcsItSession());
    }

    public HcRestMcuNotifyResource notifyResource() {
        return context.buildResourceProxy(HcRestMcuNotifyResource.class, new HcsItSession());
    }

    public HcsItContext context() {
        return context;
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
