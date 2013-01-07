package net.awired.housecream.server.engine;

public class OutEvent {

    private final long outPointId;
    private final Float value;

    public OutEvent(long outPointId, Float value) {
        this.outPointId = outPointId;
        this.value = value;
    }

    public long getOutPointId() {
        return outPointId;
    }

    public Float getValue() {
        return value;
    }

}
