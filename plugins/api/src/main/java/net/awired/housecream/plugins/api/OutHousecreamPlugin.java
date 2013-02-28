package net.awired.housecream.plugins.api;

import java.util.Map;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;
import org.apache.commons.lang3.tuple.Pair;

public interface OutHousecreamPlugin extends HousecreamPlugin {

    Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint);

}
