package net.awired.housecream.server.router;

import java.util.Map;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.TriggerType;
import net.awired.housecream.server.engine.Action;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.service.PluginService;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
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
        HousecreamPlugin plugin = pluginService.getPluginFromPrefix(outpoint.extractUrlPrefix());
        Pair<Object, Map<String, Object>> bodyAndHeaders = plugin.prepareOutBodyAndHeaders(action, outpoint);
        Message outMessage = outRouter.buildMessage(bodyAndHeaders, outpoint, action);

        exchange.setIn(outMessage);
    }
}
