package net.awired.housecream.plugins.xmpp;

import java.net.URI;
import java.util.Map;
import javax.validation.ValidationException;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;
import org.apache.camel.Message;
import org.apache.commons.lang3.tuple.Pair;

public class XmppHousecreamPlugin implements HousecreamPlugin {

    @Override
    public String scheme() {
        return "axmpp";
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, OutPoint outpoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCommand() {
        return true;
    }

    @Override
    public URI validateAndNormalizeUri(URI pointUri) throws ValidationException {
        return null;
    }

    @Override
    public Float readInValue(Message in) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
