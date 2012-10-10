package net.awired.housecream.server.converter;

import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;

@Converter
public class EventConverter {

    //    @Inject
    //    private InPointDao inPointDao;

    @Converter
    public Event toEvent(RestMcuLineNotification notif, Exchange exchange) {
        String url = "restmcu://" + notif.getSource() + "/line/" + notif.getId();

        //        try {
        Event event = new Event();
        InPoint findFromUrl = null;//inPointDao.findFromUrl(url);
        event.setPointId(findFromUrl.getId());
        event.setValue(notif.getValue());
        return event;
        //        } catch (NotFoundException e) {
        //            throw new RuntimeException("Not found", e);
        //        }

    }
}
