package net.awired.housecream.server.api.domain;

import com.google.common.base.Objects;

public class PointState {

    private long pointId;
    private float value;

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("pointId", pointId) //
                .add("value", value) //
                .toString();
    }

    public PointState(long pointId, float value) {
        this.pointId = pointId;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }
}
