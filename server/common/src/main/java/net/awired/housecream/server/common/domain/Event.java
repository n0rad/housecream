package net.awired.housecream.server.common.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event {
    private int pointId;
    private float value;

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getPointId() {
        return pointId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
