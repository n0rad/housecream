package net.awired.housecream.server.it.builder;

import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;

public class LineNotifBuilder {

    private int id;
    private RestMcuLineNotify notify;
    private float oldValue;
    private String source;
    private float value;

    public RestMcuLineNotification build() {
        RestMcuLineNotification pinNotification = new RestMcuLineNotification();
        pinNotification.setId(id);
        pinNotification.setNotify(notify);
        pinNotification.setOldValue(oldValue);
        pinNotification.setSource(source);
        pinNotification.setValue(value);
        return pinNotification;
    }

    public LineNotifBuilder id(int id) {
        this.id = id;
        return this;
    }

}
