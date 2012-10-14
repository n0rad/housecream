package net.awired.housecream.server.engine;

import com.google.common.base.Objects;

public class ConsequenceAction {

    private final long pointId;
    private final float value;

    public ConsequenceAction(long pointId, float value) {
        this.pointId = pointId;
        this.value = value;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("pointId", pointId) //
                .add("value", value) //
                .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (pointId ^ (pointId >>> 32));
        result = prime * result + Float.floatToIntBits(value);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConsequenceAction other = (ConsequenceAction) obj;
        if (pointId != other.pointId) {
            return false;
        }
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value)) {
            return false;
        }
        return true;
    }

    public long getPointId() {
        return pointId;
    }

    public float getValue() {
        return value;
    }

}
