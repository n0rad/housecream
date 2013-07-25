/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.router;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.housecream.server.api.domain.rule.Consequence;
import org.housecream.server.engine.EngineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutEndProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String CONSEQUENCE_HEADER = "HC_CONSEQUENCE";

    @Autowired
    private EngineProcessor engine;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("Receiving response for out action {}", exchange);
        Consequence consequence = exchange.getIn().getHeader(CONSEQUENCE_HEADER, Consequence.class);
        engine.setPointState(consequence.getOutPointId(), consequence.getValue());
    }

    //    Consequence action = exchange.getUnitOfWork().getOriginalInMessage().getHeader("ACTION", Consequence.class);

}
