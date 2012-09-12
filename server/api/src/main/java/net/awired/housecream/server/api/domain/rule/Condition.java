package net.awired.housecream.server.api.domain.rule;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Condition extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private long pointId;
    private float value;
    @NotNull
    private ConditionType type;

    public Condition() {
    }

    public Condition(long pointId, float value, ConditionType type) {
        this.pointId = pointId;
        this.value = value;
        this.type = type;
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

    public ConditionType getType() {
        return type;
    }

    public void setType(ConditionType type) {
        this.type = type;
    }

}
