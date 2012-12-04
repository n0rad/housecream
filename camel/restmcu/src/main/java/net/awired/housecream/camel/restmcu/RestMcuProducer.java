package net.awired.housecream.camel.restmcu;

import net.awired.ajsl.core.lang.exception.NotFoundException;
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
        restMcuClient = endpoint.getRestContext().prepareClient(RestMcuLineResource.class, boardUrl, null, true);

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
