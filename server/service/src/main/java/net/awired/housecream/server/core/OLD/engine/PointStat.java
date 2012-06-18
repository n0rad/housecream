package net.awired.housecream.server.core.OLD.engine;

public class PointStat {
    int pointId;
    float value;

    public PointStat(int pointId, float value) {
        this.pointId = pointId;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }
}
