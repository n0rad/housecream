package org.housecream.plugins.direct;

import java.net.URI;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.plugins.api.HousecreamPlugin;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.rule.Consequence;


public class DirectPlugin implements HousecreamPlugin {
    @Override
    public String scheme() {
        return "direct";
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public URI validateAndNormalizeUri(URI pointUri) throws ValidationException {
        return null;
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, Point point) {
        return null;
    }

    @Override
    public Float readValue(Message in) throws Exception {
        return null;
    }
}
