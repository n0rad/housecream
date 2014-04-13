package org.housecream.plugins.openweathermap;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;


public class OpenWeatherMapConsumer extends DefaultConsumer {
    private static TaskScheduler scheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<OpenWeatherMapConsumerRunner> schedule;

    public OpenWeatherMapConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    protected void doStart() throws Exception {
        OpenWeatherMapConsumerRunner task = new OpenWeatherMapConsumerRunner(this);
        schedule = scheduler.scheduleAtFixedRate(task, new Date(), 10000); //TODO ?????
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

    public ScheduledFuture<OpenWeatherMapConsumerRunner> getSchedule() {
        return schedule;
    }

}
