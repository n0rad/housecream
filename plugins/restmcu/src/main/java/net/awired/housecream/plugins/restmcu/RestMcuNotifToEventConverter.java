package net.awired.housecream.plugins.restmcu;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.engine.InPointDaoInterface;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;

@Converter
public class RestMcuNotifToEventConverter {

    @Inject
    private InPointDaoInterface inPointDao;

    @Converter
    public Event toEvent(RestMcuLineNotification notif, Exchange exchange) {
        String url = "restmcu://" + notif.getSource() + "/" + notif.getId();

        try {
            Event event = new Event();
            InPoint findFromUrl = inPointDao.findFromUrl(url);
            event.setPointId(findFromUrl.getId());
            event.setValue(notif.getValue());
            return event;
        } catch (NotFoundException e) {
            throw new RuntimeException("Not found", e);
        }

    }
}
