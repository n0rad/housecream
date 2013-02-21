package net.awired.housecream.server.router;

import java.util.Map;
import javax.inject.Inject;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.TriggerType;
import net.awired.housecream.server.engine.Action;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.service.PluginService;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsequenceProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private OutPointDao outputDao;

    @Inject
    private PluginService pluginService;

    @Inject
    private OutDynamicRouter outRouter;

    @Inject
    private EngineProcessor engine;

    @Override
    public void process(Exchange exchange) throws Exception {
        Action action = exchange.getIn().getBody(Action.class);

        boolean found = engine.findAndRemoveActionFromFacts(action);
        if (action.getTriggerType() == TriggerType.RETRIGGER && !found) {
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
            log.debug("Stop routing message for retrigger that was removed from facts : {}", action);
            return;
        }

        OutPoint outpoint = outputDao.find(action.getOutPointId());
        HousecreamPlugin plugin = pluginService.getPluginFromScheme(outpoint.getUri().getScheme());
        Pair<Object, Map<String, Object>> bodyAndHeaders = plugin.prepareOutBodyAndHeaders(action, outpoint);

        if (bodyAndHeaders.getRight() != null) {
            for (String key : bodyAndHeaders.getRight().keySet()) {
                exchange.getIn().setHeader(key, bodyAndHeaders.getRight().get(key));
            }
        }
        exchange.getIn().setBody(bodyAndHeaders.getLeft());
        outRouter.fillRoutingHeaders(exchange.getIn(), outpoint, action);

    }
}
