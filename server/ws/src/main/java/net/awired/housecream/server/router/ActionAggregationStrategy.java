package net.awired.housecream.server.router;

import java.util.ArrayList;
import net.awired.housecream.server.engine.Action;
import net.awired.housecream.server.engine.Actions;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.CompletionAwareAggregationStrategy;
import org.apache.camel.processor.aggregate.TimeoutAwareAggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActionAggregationStrategy implements CompletionAwareAggregationStrategy, TimeoutAwareAggregationStrategy {

    public static final String AGGREGATION_ID_HEADER = "aggregation-id";
    public static final String AGGREGATION_SIZE_HEADER = "aggregation-size";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    @SuppressWarnings("unchecked")
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Object newBody = newExchange.getIn().getBody();
        ArrayList<Object> list = null;
        if (oldExchange == null) {
            list = new ArrayList<Object>();
            list.add(newBody);
            newExchange.getIn().setBody(list);
            return newExchange;
        }
        list = oldExchange.getIn().getBody(ArrayList.class);
        list.add(newBody);
        return oldExchange;
    }

    @Override
    public void onCompletion(Exchange exchange) {
        log.debug("Complete aggregation for exchange {}", exchange);
    }

    @Override
    public void timeout(Exchange oldExchange, int index, int total, long timeout) {
        log.debug("Timeout aggregation for exchange {}", oldExchange);
    }

    public void fillCorrelationId(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(AGGREGATION_ID_HEADER, exchange.getExchangeId());
    }

    public void fillAggregationSize(Exchange exchange) throws Exception {
        Actions actions = exchange.getIn().getBody(Actions.class);
        int size = 0;
        for (Action action : actions.getActions()) {
            if (action.getDelayMili() == 0) {
                size++;
            }
        }
        exchange.getIn().setHeader(AGGREGATION_SIZE_HEADER, size);
    }

    public void removeCorrelationIdOnAsync(Exchange exchange) {
        Action action = exchange.getIn().getBody(Action.class);
        if (action.getDelayMili() > 0) {
            exchange.getIn().removeHeader(AGGREGATION_ID_HEADER);
        }
    }

}