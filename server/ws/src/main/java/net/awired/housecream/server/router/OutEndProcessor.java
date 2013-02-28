package net.awired.housecream.server.router;

import javax.inject.Inject;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.engine.EngineProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutEndProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String CONSEQUENCE_HEADER = "HC_CONSEQUENCE";

    @Inject
    private EngineProcessor engine;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("Receiving response for out action {}", exchange);
        Consequence consequence = exchange.getIn().getHeader(CONSEQUENCE_HEADER, Consequence.class);
        engine.setPointState(consequence.getOutPointId(), consequence.getValue());
    }

    //    Consequence action = exchange.getUnitOfWork().getOriginalInMessage().getHeader("ACTION", Consequence.class);

}
