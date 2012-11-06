package net.awired.housecream.plugins.xmpp;

import java.util.Map;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;

public class XmppHousecreamPlugin implements HousecreamPlugin {

    @Override
    public String prefix() {
        return "xmpp";
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

}
