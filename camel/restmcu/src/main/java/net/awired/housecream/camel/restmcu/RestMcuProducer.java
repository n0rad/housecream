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

import net.awired.core.lang.exception.NotFoundException;
import net.awired.restmcu.api.domain.line.RestMcuLine;
import net.awired.restmcu.api.domain.line.RestMcuLineDirection;
import net.awired.restmcu.api.resource.client.RestMcuLineResource;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The restmcu producer.
 */
public class RestMcuProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(RestMcuProducer.class);
    private RestMcuLineResource restMcuClient;
    private int lineId;
    private String boardUrl;

    public RestMcuProducer(RestMcuEndpoint endpoint) {
        super(endpoint);
        boardUrl = endpoint.findBoardUrl();
        lineId = endpoint.findLineId();
        restMcuClient = endpoint.getRestContext().buildClient(RestMcuLineResource.class, boardUrl);

    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Float value = exchange.getIn().getBody(Float.class);
        restMcuClient.setLineValue(lineId, value);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        checkLineIsOuput();
    }

    private void checkLineIsOuput() throws NotFoundException {
        LOG.debug("Get line description from board to check producer direction : {}", boardUrl);
        RestMcuLine line = restMcuClient.getLine(lineId);
        if (line.getDirection() != RestMcuLineDirection.OUTPUT) {
            throw new IllegalStateException("Cannot start a restmcu producer for a non OUTPUT line : " + line);
        }
    }

}
