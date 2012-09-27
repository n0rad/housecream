package net.awired.housecream.server.converter;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.storage.dao.InPointDao;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;

@Converter
public class EventConverter {

    @Inject
    private InPointDao inPointDao;

    @Converter
    public Event toEvent(MessageContentsList contents, Exchange exchange) {
        if (!(contents.get(0) instanceof RestMcuLineNotification)) {
            throw new RuntimeException("Cannot handle this kind of message" + contents.get(0));
        }

        RestMcuLineNotification notif = (RestMcuLineNotification) contents.get(0);

        Message message = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
        ServletRequest request = (ServletRequest) message.get("HTTP.REQUEST");
        String remoteAddr = request.getRemoteAddr();

        String url = "restmcu://" + notif.getSource() + "/line/" + notif.getId();

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
