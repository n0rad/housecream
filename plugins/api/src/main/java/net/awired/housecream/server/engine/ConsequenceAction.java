package net.awired.housecream.server.engine;

public class ConsequenceAction {

    private final long pointId;
    private final float value;

    public ConsequenceAction(long pointId, float value) {
        this.pointId = pointId;
        this.value = value;
    }

    public long getPointId() {
        return pointId;
    }

    public float getValue() {
        return value;
    }

}
