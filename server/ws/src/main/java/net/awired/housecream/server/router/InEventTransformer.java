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

import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.plugins.api.InHousecreamPlugin;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InEventTransformer implements Processor {

    private final Logger log = LoggerFactory.getLogger(getClass());
    public static final String PLUGIN_HEADER_NAME = "hc-plugin";
    public static final String INPOINT_HEADER_NAME = "hc-inpoint";

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            InHousecreamPlugin plugin = (InHousecreamPlugin) exchange.getIn().getHeader(PLUGIN_HEADER_NAME,
                    HousecreamPlugin.class);
            InPoint inpoint = exchange.getIn().getHeader(INPOINT_HEADER_NAME, InPoint.class);
            Float readInValue = plugin.readInValue(exchange.getIn());
            Event event = new Event();
            event.setPointId(inpoint.getId());
            event.setValue(readInValue);
            exchange.getIn().setBody(event);
        } catch (Exception e) {
            log.warn("Cannot convert in message to event", e);
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
    }

}
