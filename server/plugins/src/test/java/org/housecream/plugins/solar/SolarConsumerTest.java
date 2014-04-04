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
package org.housecream.plugins.solar;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.concurrent.ScheduledFuture;
import org.apache.camel.Processor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SolarConsumerTest {

    @Mock
    private Processor processor;

    @Mock
    private SolarEndpoint endpoint;

    @Test
    public void should_create_schedule_on_start() throws Exception {
        SolarConsumer solarConsumer = new SolarConsumer(endpoint, processor);

        solarConsumer.start();

        assertThat(solarConsumer.getSchedule()).isNotNull();
    }

    @Test
    public void should_stop_and_remove_schedule_on_stop() throws Exception {
        SolarConsumer solarConsumer = new SolarConsumer(endpoint, processor);
        solarConsumer.start();
        ScheduledFuture<SolarConsumerRunner> schedule = solarConsumer.getSchedule();

        solarConsumer.stop();

        assertThat(solarConsumer.getSchedule()).isNull();
        Assertions.assertThat(schedule.isCancelled()).isTrue();
    }

}
