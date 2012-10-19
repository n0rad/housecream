package net.awired.housecream.server.engine;

public class Flag {

    private long pointId;

    public Flag(long pointId) {
        this.pointId = pointId;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }
}
