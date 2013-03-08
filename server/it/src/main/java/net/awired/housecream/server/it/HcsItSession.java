package net.awired.housecream.server.it;

import net.awired.housecream.server.it.api.InpointApi;
import net.awired.housecream.server.it.api.OutpointApi;
import net.awired.housecream.server.it.api.RuleApi;
import net.awired.housecream.server.it.api.ZoneApi;

public class HcsItSession {

    private final HcsItServer server;

    private String sessionId;
    private boolean useJson = true;

    public HcsItSession(HcsItServer server) {
        this.server = server;
    }

    public HcwWebSocket webSocket() {
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

    public String getSessionId() {
        return sessionId;
    }

    public boolean isUseJson() {
        return useJson;
    }

    public void setUseJson(boolean useJson) {
        this.useJson = useJson;
    }

    public HcsItServer getServer() {
        return server;
    }

}
