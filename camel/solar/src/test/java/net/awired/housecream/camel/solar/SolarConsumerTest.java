package net.awired.housecream.camel.solar;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.concurrent.ScheduledFuture;
import org.apache.camel.Processor;
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
        assertThat(schedule.isCancelled()).isTrue();
    }

}
