package net.awired.housecream.plugins.restmcu;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.validation.ValidationException;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.google.common.base.Preconditions;

public class RestMcuHousecreamPlugin implements HousecreamPlugin {

    private static final String SCHEME = "restmcu";
    public static final int DEFAULT_COMPONENT_PORT = 80;

    @Override
    public Float readInValue(Message in) throws Exception {
        RestMcuLineNotification notif = in.getBody(RestMcuLineNotification.class);
        Preconditions.checkNotNull(notif, "Notification cannot be null");
        Preconditions.checkNotNull(notif.getSource(), "Source of notification cannot be null");
        return notif.getValue();
    }

    @Override
    public String scheme() {
        return SCHEME;
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
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint) {
        return new ImmutablePair<Object, Map<String, Object>>(action.getValue(), null);
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public URI validateAndNormalizeUri(URI uri) throws ValidationException {
        Preconditions.checkNotNull(uri, "Uri cannot be null");
        Preconditions.checkState(SCHEME.equals(uri.getScheme()), "Invalid schema for uri : " + uri);

        int port = uri.getPort();
        if (port == DEFAULT_COMPONENT_PORT) {
            port = -1;
        }
        try {
            return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(),
                    uri.getFragment());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("cannot rebuild URI after removing the default port : " + uri, e);
        }
    }

}
