/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.router;

import java.util.Map;
import net.awired.housecream.plugins.api.OutHousecreamPlugin;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsequenceProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OutPointDao outputDao;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private OutDynamicRouter outRouter;

    @Autowired
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
        OutHousecreamPlugin plugin = (OutHousecreamPlugin) pluginService.getPluginFromScheme(outpoint.getUri()
                .getScheme()); //TODO cast should not be necessary
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
