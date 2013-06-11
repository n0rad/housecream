package net.awired.housecream.server.api.domain;

import lombok.Data;

@Data
public class PointState {

    private long pointId;
    private float value;

    public PointState(long pointId, float value) {
        this.pointId = pointId;
        this.value = value;
    }

}
