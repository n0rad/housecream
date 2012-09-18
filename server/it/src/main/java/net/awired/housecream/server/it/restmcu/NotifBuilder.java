package net.awired.housecream.server.it.restmcu;

import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition;

public class NotifBuilder {

    private int pinId;
    private String source;
    private float value;
    private float oldValue;
    private RestMcuLineNotify notify;

    public RestMcuLineNotification build() {
        RestMcuLineNotification pinNotif = new RestMcuLineNotification();
        pinNotif.setId(pinId);
        pinNotif.setSource(source);
        pinNotif.setValue(value);
        pinNotif.setOldValue(oldValue);
        pinNotif.setNotify(notify);
        return pinNotif;
    }

    public NotifBuilder lineId(int lineId) {
        this.pinId = lineId;
        return this;
    }

    public NotifBuilder source(String source) {
        this.source = source;
        return this;
    }

    public NotifBuilder value(float value) {
        this.value = value;
        return this;
    }

    public NotifBuilder oldValue(float oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public NotifBuilder notify(RestMcuLineNotifyCondition supOrEqual, int i) {
        this.notify = new RestMcuLineNotify(supOrEqual, i);
        return this;
    }
}
