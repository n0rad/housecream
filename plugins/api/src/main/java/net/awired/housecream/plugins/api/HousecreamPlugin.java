package net.awired.housecream.plugins.api;

import java.util.Map;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.ConsequenceAction;

public interface HousecreamPlugin {

    String prefix();

    //TODO MOVE
    //
    //    Float getCurrentValue(Point point, CamelContext camelContext);
    //
    Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(ConsequenceAction action, OutPoint outpoint);

}
