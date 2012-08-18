package net.awired.housecream.server.service.event;

import javax.inject.Inject;
import net.awired.housecream.server.engine.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Inject
    private EventWebSocketService eventWebSocketService;

    @Async
    public void saveEventAsync(Event event) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eventWebSocketService.sendEvent(event);
    }

}
