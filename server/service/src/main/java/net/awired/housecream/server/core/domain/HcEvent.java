package net.awired.housecream.server.core.domain;

import net.awired.housecream.server.common.domain.Point;
import net.awired.housecream.server.common.domain.Status;

public class HcEvent {
    private Point point;
    private Status status;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
