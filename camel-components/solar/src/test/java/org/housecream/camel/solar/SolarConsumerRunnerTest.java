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
package org.housecream.camel.solar;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Date;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultExchange;
import org.housecream.camel.solar.SolarConsumer;
import org.housecream.camel.solar.SolarConsumerRunner;
import org.housecream.camel.solar.SolarEndpoint;
import org.housecream.solar.SolarAdvancedCalculator;
import org.housecream.solar.SolarState;
import org.housecream.solar.enumeration.SolarTimeEvent;
import org.housecream.solar.enumeration.SolarTimePhase;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class SolarConsumerRunnerTest {

    @Mock
    private Processor processor;

    @Mock
    private SolarEndpoint endpoint;

    @Mock
    private SolarAdvancedCalculator calculator;

    private SolarConsumer consumer;

    private SolarConsumerRunner runner;

    private SimpleTriggerContext triggerContext = new SimpleTriggerContext();

    @Before
    public void before() {
        consumer = new SolarConsumer(endpoint, processor);
        runner = new SolarConsumerRunner(consumer);
        ReflectionTestUtils.setField(runner, "calculator", calculator);
    }

    @Test
    public void should_give_current_state_at_start() throws Exception {
        DateTime current = new DateTime();
        when(calculator.findCurrentState()).thenReturn(new SolarState(0, 0, current, SolarTimePhase.AFTERNOON));

        Date nextExecution = runner.nextExecutionTime(triggerContext);

        assertThat(nextExecution.getTime()).isEqualTo(current.getMillis());
    }

    @Test
    public void should_give_next_event() throws Exception {
        should_give_current_state_at_start();
        DateTime next = new DateTime().plusHours(1);
        when(calculator.findNextEvent()).thenReturn(new SolarState(0, 0, next, SolarTimeEvent.noon));
        triggerContext.update(new Date(), new Date(), new Date());

        Date nextExecution = runner.nextExecutionTime(triggerContext);

        assertThat(nextExecution.getTime()).isEqualTo(next.getMillis());
    }

    @Test
    public void should_give_phase_state_1_min_after_event() throws Exception {
        should_give_current_state_at_start();
        DateTime next = new DateTime().plusHours(1);
        when(calculator.findNextEvent()).thenReturn(new SolarState(0, 0, next, SolarTimeEvent.noon));
        triggerContext.update(new Date(), new Date(), new Date());
        runner.nextExecutionTime(triggerContext);

        Date nextExecution = runner.nextExecutionTime(triggerContext);

        assertThat(nextExecution.getTime()).isEqualTo(next.plusMinutes(1).getMillis());
    }

    @Test
    public void should_create_camel_exchange_with_next_event() throws Exception {
        should_give_current_state_at_start();
        Exchange exchange = new DefaultExchange(endpoint);
        when(endpoint.createExchange(ExchangePattern.InOnly)).thenReturn(exchange);

        runner.run();

        assertThat(((SolarState) exchange.getIn().getBody()).getPhase()).isEqualTo(SolarTimePhase.AFTERNOON);
        assertThat(((SolarState) exchange.getIn().getBody()).getDate()).isNotNull();
    }
}
