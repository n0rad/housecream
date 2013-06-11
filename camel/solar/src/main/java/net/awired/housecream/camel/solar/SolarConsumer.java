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

import java.util.concurrent.ScheduledFuture;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

public class SolarConsumer extends DefaultConsumer {

    private static TaskScheduler scheduler = new ConcurrentTaskScheduler();
    private ScheduledFuture<SolarConsumerRunner> schedule;

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

    public ScheduledFuture<SolarConsumerRunner> getSchedule() {
        return schedule;
    }

}
