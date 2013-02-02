package net.awired.housecream.camel.solar;

import java.util.Date;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

public final class SolarConsumerRunner implements Runnable, Trigger {

    private final SolarConsumer consumer;
    private final SolarEndpoint endpoint;
    private final SolarAdvancedCalculator calculator;

    private SolarState nextEvent;

    public SolarConsumerRunner(SolarConsumer consumer) {
        this.consumer = consumer;
        endpoint = (SolarEndpoint) consumer.getEndpoint();
        calculator = new SolarAdvancedCalculator(endpoint.getLatitude(), endpoint.getLongitude(),
                endpoint.getTimezone());

    }

    @Override
    public void run() {
        Exchange camelExchange = consumer.getEndpoint().createExchange(ExchangePattern.InOnly);
        camelExchange.getIn().setBody(nextEvent);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        } finally {
            nextEvent = null;
        }
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        nextEvent = calculator.findNextEvent();
        return nextEvent.getDate();
    }
}
