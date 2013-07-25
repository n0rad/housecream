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
package org.housecream.server.command;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.housecream.server.component.HcWebConsumer;
import org.housecream.server.engine.OutEvent;
import org.springframework.stereotype.Component;

@Component
public class CommandService {

    private HcWebConsumer consumer;

    public void processOutEvent(OutEvent event) throws Exception {
        Exchange exchange = consumer.getEndpoint().createExchange(ExchangePattern.InOut);
        try {
            exchange.getIn().setBody(event);
            consumer.getProcessor().process(exchange);
            //TODO            return exchange.getOut().getBody();
        } catch (Exception e) {
            exchange.setException(e);
        }
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
    }

    public void setConsumer(HcWebConsumer consumer) {
        this.consumer = consumer;
    }

}
