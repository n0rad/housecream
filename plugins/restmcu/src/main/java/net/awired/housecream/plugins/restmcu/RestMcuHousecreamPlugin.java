package net.awired.housecream.plugins.restmcu;

import net.awired.housecream.plugins.api.HousecreamPlugin;

public class RestMcuHousecreamPlugin implements HousecreamPlugin {

    private static final String PREFIX = "restmcu";

    @Override
    public String prefix() {
        return PREFIX;
    }

    //    @Override
    //    public void updatePointNotification(Point point, String routerUrl) {
    //        updateNotifyUrl(point, routerUrl + HcRestMcuNotifyResource.INNER_ROUTE_CONTEXT);
    //        // TODO update notify conditions
    //        // TOO update name
    //    }
    //
    //    private String getBoardUrl(Point point) {
    //        int indexOf = point.getUrl().indexOf("://");
    //        int indexOf2 = point.getUrl().indexOf("/", indexOf + 3);
    //        String host = point.getUrl().substring(prefix().length() + 3, indexOf2);
    //        return "http://" + host;
    //    }
    //
    //    public int getLineId(Point point) {
    //        int indexOf = point.getUrl().lastIndexOf("/");
    //        return Integer.valueOf(point.getUrl().substring(indexOf + 1));
    //    }
    //
    //    private void updatePin(Point point) {
    //
    //        //TODO
    //        //        RestMcuPin pin = new RestMcuPin();
    //        //        try {
    //        //            pinResource.setPinSettings(getPinId(point), pin);
    //        //        } catch (NotFoundException e) {
    //        //            // TODO Auto-generated catch block
    //        //            e.printStackTrace();
    //        //        } catch (UpdateException e) {
    //        //            // TODO Auto-generated catch block
    //        //            e.printStackTrace();
    //        //        }
    //
    //    }
    //
    //    private void updateNotifyUrl(Point point, String routerUrl) {
    //        RestMcuBoardResource boardResource = new RestContext().prepareClient(RestMcuBoardResource.class,
    //                getBoardUrl(point), null, true);
    //
    //        //        RestMcuBoardResource boardResource = JAXRSClientFactory.create(getBoardUrl(point),
    //        //                RestMcuBoardResource.class, buildProviders());
    //        //        WebClient.client(boardResource).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);
    //
    //        // can be done with camel but its much cleaner with rsclient
    //        //        ProducerTemplate createProducerTemplate = camelContext.createProducerTemplate();
    //        //        createProducerTemplate.request();
    //
    //        RestMcuBoardSettings settings = new RestMcuBoardSettings();
    //        settings.setNotifyUrl(routerUrl);
    //        try {
    //            boardResource.setBoardSettings(settings);
    //        } catch (UpdateException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    @Override
    //    public Float getCurrentValue(Point point, CamelContext camelContext) {
    //        RestMcuLineResource boardResource = new RestContext().prepareClient(RestMcuLineResource.class,
    //                getBoardUrl(point), null, true);
    //        Float f;
    //        try {
    //            f = boardResource.getLineValue(getLineId(point));
    //            return f;
    //        } catch (NotFoundException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //            throw new RuntimeException(e);
    //        }
    //    }
    //
    //    @Override
    //    public Message buildOutputMessage(ConsequenceAction action, OutPoint outpoint) {
    //        DefaultMessage message = new DefaultMessage();
    //        message.setHeader(CxfConstants.OPERATION_NAME, "setPinValue");
    //        message.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);
    //
    //        //        int indexOf = outpoint.getUrl().indexOf("://");
    //        //        "http" + outpoint.getUrl().substring(indexOf)
    //        String endUrl = "?resourceClasses=net.awired.housecream.server.service.web.Toto42&loggingFeatureEnabled=true";
    //        //        url = "cxfrs://http://localhost:5879/?resourceClasses=net.awired.housecream.server.core.service.web.Toto42&loggingFeatureEnabled=true";
    //
    //        message.setHeader("ACTION", action);
    //        message.setHeader(OutDynamicRouter.OUT_URL, "cxfrs://" + getBoardUrl(outpoint) + endUrl);
    //        message.setBody(new Object[] { getLineId(outpoint), action.getValue() });
    //        return message;
    //    }
}
