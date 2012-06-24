package net.awired.housecream.server.common.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event {
    private long pointId;
    private float value;

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public long getPointId() {
        return pointId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
