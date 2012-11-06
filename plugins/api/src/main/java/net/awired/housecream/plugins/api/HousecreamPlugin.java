package net.awired.housecream.plugins.api;

import java.util.Map;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;

public interface HousecreamPlugin {

    String prefix();

    //TODO MOVE
    //
    //    Float getCurrentValue(Point point, CamelContext camelContext);
    //
    Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint);

    boolean isCommand();

}
