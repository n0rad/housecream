package net.awired.housecream.server.router.component;

import javax.ws.rs.core.MediaType;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.ajsl.web.resource.mapper.AjslResponseExceptionMapper;
import net.awired.housecream.server.common.domain.Point;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.ConsequenceAction;
import net.awired.housecream.server.router.ComponentType;
import net.awired.housecream.server.router.OutDynamicRouter;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;
import net.awired.restmcu.api.resource.client.RestMcuPinResource;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultMessage;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JSONProvider;
import com.google.common.collect.ImmutableList;

public class RestMcuComponent implements EndPointComponent {

    @Override
    public void updatePointNotification(Point point, String routerUrl) {
        updateNotifyUrl(point, routerUrl);
        // TODO update notify conditions
    }

    public String getBoardUrl(Point point) {
        int indexOf = point.getUrl().indexOf("://");
        int indexOf2 = point.getUrl().indexOf("/", indexOf + 3);
        String host = point.getUrl().substring(ComponentType.RESTMCU.name().length() + 3, indexOf2);
        return "http://" + host;
    }

    public int getPinId(Point point) {
        int indexOf = point.getUrl().lastIndexOf("/");
        return Integer.valueOf(point.getUrl().substring(indexOf + 1));
    }

    private void updateNotifyUrl(Point point, String routerUrl) {
        JSONProvider jsonProvider = new JSONProvider();
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setDropRootElement(true);

        AjslResponseExceptionMapper exceptionMapper = new AjslResponseExceptionMapper(jsonProvider);
        ImmutableList<Object> providers = ImmutableList.of(exceptionMapper, jsonProvider);

        String boardUrl = getBoardUrl(point);
        RestMcuBoardResource boardResource = JAXRSClientFactory.create(boardUrl, RestMcuBoardResource.class,
                providers);
        WebClient.client(boardResource).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);

        // can be done with camel but its much cleaner with rsclient
        //        ProducerTemplate createProducerTemplate = camelContext.createProducerTemplate();
        //        createProducerTemplate.request();

        RestMcuBoard board = boardResource.getBoard();
        board.setNotifyUrl(routerUrl);
        try {
            boardResource.setBoard(board);
        } catch (UpdateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Float getCurrentValue(Point point, CamelContext camelContext) {
        JSONProvider jsonProvider = new JSONProvider();
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setDropRootElement(true);

        AjslResponseExceptionMapper exceptionMapper = new AjslResponseExceptionMapper(jsonProvider);
        ImmutableList<Object> providers = ImmutableList.of(exceptionMapper, jsonProvider);

        RestMcuPinResource boardResource = JAXRSClientFactory.create(getBoardUrl(point), RestMcuPinResource.class,
                providers);
        WebClient.client(boardResource).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);

        Float f;
        try {
            f = boardResource.getPinValue(getPinId(point));
            return f;
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message buildOutputMessage(ConsequenceAction action, OutPoint outpoint) {
        DefaultMessage message = new DefaultMessage();
        message.setHeader(CxfConstants.OPERATION_NAME, "setPinValue");
        message.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);

        //        int indexOf = outpoint.getUrl().indexOf("://");
        //        "http" + outpoint.getUrl().substring(indexOf)
        String endUrl = "?resourceClasses=net.awired.housecream.server.service.web.Toto42&loggingFeatureEnabled=true";
        //        url = "cxfrs://http://localhost:5879/?resourceClasses=net.awired.housecream.server.core.service.web.Toto42&loggingFeatureEnabled=true";

        int lastPos = outpoint.getUrl().lastIndexOf('/');
        Integer pinId = Integer.valueOf(outpoint.getUrl().substring(lastPos + 1));

        message.setHeader("ACTION", action);
        message.setHeader(OutDynamicRouter.OUT_URL, "cxfrs://" + getBoardUrl(outpoint) + endUrl);
        message.setBody(new Object[] { pinId, action.getValue() });
        return message;
    }

}
