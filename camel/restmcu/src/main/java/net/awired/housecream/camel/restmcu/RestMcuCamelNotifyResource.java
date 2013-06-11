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
package net.awired.housecream.camel.restmcu;

import javax.ws.rs.Path;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;

@Path("/")
public class RestMcuCamelNotifyResource implements RestMcuNotifyResource {

    private RestMcuConsumer consumer;

    public RestMcuCamelNotifyResource(RestMcuConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void lineNotification(RestMcuLineNotification pinNotification) {
        //TODO check remote address to be sure it match the source
        Exchange camelExchange = consumer.getEndpoint().createExchange(ExchangePattern.InOnly);
        Message in = camelExchange.getIn();
        in.setBody(pinNotification);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        }
    }

    @Override
    public void boardNotification(RestMcuBoardNotification boardNotification) {
        //TODO check remote address to be sure it match the source
        Exchange camelExchange = consumer.getEndpoint().createExchange(ExchangePattern.InOnly);
        Message in = camelExchange.getIn();
        in.setBody(boardNotification);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        }
    }

    @Override
    public long getPosixTime() {
        return System.currentTimeMillis() / 1000L;
    }

}
