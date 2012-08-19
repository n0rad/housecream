package net.awired.housecream.server.it.builder;

import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotify;

public class PinNotifBuilder {

    private int id;
    private RestMcuPinNotify notify;
    private float oldValue;
    private String source;
    private float value;

    public RestMcuPinNotification build() {
        RestMcuPinNotification pinNotification = new RestMcuPinNotification();
        pinNotification.setId(id);
        pinNotification.setNotify(notify);
        pinNotification.setOldValue(oldValue);
        pinNotification.setSource(source);
        pinNotification.setValue(value);
        return pinNotification;
    }

    public PinNotifBuilder id(int id) {
        this.id = id;
        return this;
    }

}
