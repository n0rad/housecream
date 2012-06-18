package net.awired.housecream.server.core.converter;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletRequest;
import net.awired.housecream.server.core.domain.HcEvent;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;

@Converter
public class HcEventConverter {

    @Inject
    private InPointDao inPointDao;

    @Converter
    public HcEvent toEvent(MessageContentsList contents, Exchange exchange) throws IOException {
        Message message = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
        ServletRequest request = (ServletRequest) message.get("HTTP.REQUEST");
        String remoteAddr = request.getRemoteAddr();
        return new HcEvent();
    }
}
