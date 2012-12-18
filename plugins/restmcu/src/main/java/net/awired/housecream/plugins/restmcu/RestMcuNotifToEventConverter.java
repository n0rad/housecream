package net.awired.housecream.plugins.restmcu;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.engine.InPointDaoInterface;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;

@Converter
public class RestMcuNotifToEventConverter {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private InPointDaoInterface inPointDao;

    @Converter
    public Event toEvent(RestMcuLineNotification notif, Exchange exchange) {
        Preconditions.checkNotNull(notif, "Notification cannot be null");
        Preconditions.checkNotNull(notif.getSource(), "Source of notification cannot be null");
        String[] split = notif.getSource().split(":");
        if (split.length != 2) {
            throw new IllegalStateException("Received notification with invalid source : " + notif.getSource());
        }

        String url = "restmcu://";
        //        if (split[1].equals("80")) {
        //            url += split[0];
        //        } else {
        url += notif.getSource();
        //        }
        url += "/" + notif.getId();

        try {
            Event event = new Event();
            InPoint findFromUrl = inPointDao.findFromUrl(url);
            event.setPointId(findFromUrl.getId());
            event.setValue(notif.getValue());
            return event;
        } catch (NotFoundException e) {
            log.warn("No point found to process notification : " + notif);
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
        return null;
    }
}
