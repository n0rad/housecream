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
package org.housecream.plugins.restmcu;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.housecream.restmcu.api.LineNotFoundException;
import org.housecream.restmcu.api.domain.line.RestMcuLine;
import org.housecream.restmcu.api.domain.line.RestMcuLineDirection;
import org.housecream.restmcu.api.resource.client.RestMcuLineResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import fr.norad.jaxrs.client.server.resource.mapper.HttpStatusResponseExceptionMapper;
import fr.norad.jaxrs.client.server.rest.RestBuilder;

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
        restMcuClient = endpoint.getRestContext()
                .addProvider(new JacksonJsonProvider())
                .addInFaultInterceptor(RestBuilder.Generic.inStderrLogger)
                .addInInterceptor(RestBuilder.Generic.inStdoutLogger)
                .addOutFaultInterceptor(RestBuilder.Generic.outStderrLogger)
                .addOutInterceptor(RestBuilder.Generic.outStdoutLogger)
                .addProvider(new HttpStatusResponseExceptionMapper())
                .buildClient(RestMcuLineResource.class, boardUrl);

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

    private void checkLineIsOuput() throws LineNotFoundException {
        LOG.debug("Get line description from board to check producer direction : {}", boardUrl);
        RestMcuLine line = restMcuClient.getLine(lineId);
        if (line.getDirection() != RestMcuLineDirection.OUTPUT) {
            throw new IllegalStateException("Cannot start a restmcu producer for a non OUTPUT line : " + line);
        }
    }

}
