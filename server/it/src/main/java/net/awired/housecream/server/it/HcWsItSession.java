package net.awired.housecream.server.it;

import net.awired.ajsl.ws.rest.RestSession;
import net.awired.housecream.server.it.api.InpointApi;
import net.awired.housecream.server.it.api.OutpointApi;
import net.awired.housecream.server.it.api.RuleApi;
import net.awired.housecream.server.it.api.ZoneApi;

public class HcWsItSession extends RestSession {

    private final HcWsItServer server;

    public HcWsItSession(HcWsItServer server) {
        this.server = server;
    }

    public HcWebWebSocket webSocket() {
        return server.newWebSocket().open();
    }

    public ZoneApi zone() {
        return new ZoneApi(this);
    }

    public InpointApi inpoint() {
        return new InpointApi(this);
    }

    public OutpointApi outpoint() {
        return new OutpointApi(this);
    }

    public RuleApi rule() {
        return new RuleApi(this);
    }

    ///////////////////////////////////////////

    public HcWsItServer getServer() {
        return server;
    }

}
