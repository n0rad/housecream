package net.awired.housecream.server.service.event;

import javax.inject.Inject;
import net.awired.housecream.server.api.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private EventWebSocketService eventWebSocketService;

    @Async
    public void saveEventAsync(Event event) {
        log.debug("Saving event {}", event);
        eventWebSocketService.sendEvent(event);
    }

}
