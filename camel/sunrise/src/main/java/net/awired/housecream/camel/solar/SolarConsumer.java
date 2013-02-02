package net.awired.housecream.camel.solar;

import java.util.concurrent.ScheduledFuture;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

public class SolarConsumer extends DefaultConsumer {

    private static TaskScheduler scheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<?> schedule;

    public SolarConsumer(SolarEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    protected void doStart() throws Exception {
        SolarConsumerRunner task = new SolarConsumerRunner(this);
        schedule = scheduler.schedule(task, task);
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        if (schedule != null) {
            schedule.cancel(true);
            schedule = null;
        }
        super.doStop();
    }

}
