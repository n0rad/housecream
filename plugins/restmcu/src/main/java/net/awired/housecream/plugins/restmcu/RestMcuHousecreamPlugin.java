package net.awired.housecream.plugins.restmcu;

import java.util.Map;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.ConsequenceAction;

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
    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(ConsequenceAction action, OutPoint outpoint) {
        return new Pair<Object, Map<String, Object>>(action.getValue(), null);
    }

    @Override
    public boolean isCommand() {
        return false;
    }
}
