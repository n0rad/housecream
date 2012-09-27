package net.awired.housecream.server.service.event;

import javax.inject.Inject;
import net.awired.housecream.server.api.domain.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Inject
    private EventWebSocketService eventWebSocketService;

    @Async
    public void saveEventAsync(Event event) {
        eventWebSocketService.sendEvent(event);
    }

}
