package net.awired.housecream.server.router;

import java.util.Map;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.ConsequenceAction;
import net.awired.housecream.server.service.PluginService;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ConsequenceProcessor implements Processor {

    @Inject
    private OutPointDao outputDao;

    @Inject
    private PluginService pluginService;

    @Inject
    private OutDynamicRouter outRouter;

    @Override
    public void process(Exchange exchange) throws Exception {
        ConsequenceAction action = exchange.getIn().getBody(ConsequenceAction.class);

        //        exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE); 

        OutPoint outpoint = outputDao.find(action.getPointId());
        HousecreamPlugin plugin = pluginService.getPluginFromPrefix(outpoint.extractUrlPrefix());
        Pair<Object, Map<String, Object>> bodyAndHeaders = plugin.prepareOutBodyAndHeaders(action, outpoint);
        Message outMessage = outRouter.buildMessage(bodyAndHeaders, outpoint, action);

        exchange.setIn(outMessage);
    }
}
