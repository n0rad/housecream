package net.awired.housecream.plugins.api;

import java.net.URI;
import java.util.Map;
import javax.validation.ValidationException;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;

public interface HousecreamPlugin {

    String scheme();

    //TODO MOVE
    //
    //    Float getCurrentValue(Point point, CamelContext camelContext);
    //
    Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint);

    boolean isCommand();

    /**
     * @return null if valid.
     */
    URI validateAndNormalizeUri(URI pointUri) throws ValidationException;

}
