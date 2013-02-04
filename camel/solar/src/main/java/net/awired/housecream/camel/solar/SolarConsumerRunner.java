package net.awired.housecream.camel.solar;

import java.util.Date;
import net.awired.housecream.solar.SolarAdvancedCalculator;
import net.awired.housecream.solar.SolarState;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

public class SolarConsumerRunner implements Runnable, Trigger {

    private final SolarConsumer consumer;
    private final SolarEndpoint endpoint;
    private SolarAdvancedCalculator calculator;

    private SolarState nextEvent;

    public SolarConsumerRunner(SolarConsumer consumer) {
        this.consumer = consumer;
        endpoint = (SolarEndpoint) consumer.getEndpoint();
        calculator = new SolarAdvancedCalculator(endpoint.getLatitude(), endpoint.getLongitude(),
                endpoint.getTimezone());
    }

    @Override
    public void run() {
        Exchange camelExchange = endpoint.createExchange(ExchangePattern.InOnly);
        camelExchange.getIn().setBody(nextEvent);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        }
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        if (triggerContext.lastActualExecutionTime() == null) {
            nextEvent = calculator.findCurrentState();
        } else if (nextEvent != null && nextEvent.getEvent() != null) {
            nextEvent = new SolarState(nextEvent.getLatitude(), nextEvent.getLongitude(), nextEvent.getDate()
                    .plusMinutes(1), nextEvent.getPhase());
        } else {
            nextEvent = calculator.findNextEvent();
        }
        return nextEvent.getDate().toDate();
    }
}
