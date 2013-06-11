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
