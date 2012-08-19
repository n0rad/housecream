package net.awired.housecream.server.common.domain;

import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement
public class Event {
    private long pointId;
    private float value;

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("pointId", pointId) //
                .add("value", value) //
                .toString();
    }

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
