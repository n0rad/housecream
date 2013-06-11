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

import java.net.URI;
import java.util.Map;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutDynamicRouter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String ROUTED_FLAG = "invoked";
    private static final String OUT_URL = "outUrl";

    public void fillRoutingHeaders(Message message, OutPoint outpoint, Consequence action) {
        message.setHeader(OutEndProcessor.CONSEQUENCE_HEADER, action);
        message.setHeader(OutDynamicRouter.OUT_URL, outpoint.getUri());
    }

    public Object route(Exchange exchange, @Body Object body, @Properties Map<String, Object> properties,
            @Header(OUT_URL) URI url) {
        exchange.setPattern(ExchangePattern.InOut);
        //        Object url = properties.get(OUT_URL);
        if (url == null) {
            log.error("Cannot found url for output message", body);
            return null;
        }

        int invoked = 0;
        Object current = properties.get(ROUTED_FLAG);
        if (current != null) {
            invoked = Integer.valueOf(current.toString());
        }
        invoked++;
        properties.put(ROUTED_FLAG, invoked);

        if (invoked == 1) {
            log.debug("routing body {} to component url {}", body, url);
            return url;
        }
        return null;
    }
}
