package net.awired.housecream.server.it.restmcu;

import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotify;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;

public class NotifBuilder {

    private int pinId;
    private String source;
    private float value;
    private float oldValue;
    private RestMcuPinNotify notify;

    public RestMcuPinNotification build() {
        RestMcuPinNotification pinNotif = new RestMcuPinNotification();
        pinNotif.setId(pinId);
        pinNotif.setSource(source);
        pinNotif.setValue(value);
        pinNotif.setOldValue(oldValue);
        pinNotif.setNotify(notify);
        return pinNotif;
    }

    public NotifBuilder pinId(int pinId) {
        this.pinId = pinId;
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

    public NotifBuilder notify(RestMcuPinNotifyCondition supOrEqual, int i) {
        this.notify = new RestMcuPinNotify(supOrEqual, i);
        return this;
    }
}
